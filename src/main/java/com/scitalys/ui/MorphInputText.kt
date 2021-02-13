package com.scitalys.ui

import android.content.Context
import android.content.res.ColorStateList
import android.text.*
import android.util.AttributeSet
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Filterable
import android.widget.ListAdapter
import androidx.core.graphics.ColorUtils
import com.google.android.material.chip.ChipDrawable
import com.scitalys.bp_traits.Trait
import com.scitalys.ui.morphInputText.NoDoubleCommaInputFilter
import com.scitalys.ui.morphInputText.NoSpaceCommaTokenizer
import com.scitalys.ui.utils.dp
import com.scitalys.ui.utils.getColorFromAttr
import kotlin.properties.Delegates

class MorphInputText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.autoCompleteTextViewStyle
) : androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView(context, attrs, defStyleAttr) {

    val chips: Array<ChipSpan>
        get() = editableText.getSpans(0, editableText.length, ChipSpan::class.java)

    private val _textWatchers = mutableListOf<TextWatcher>()

    private val _tokenizer = NoSpaceCommaTokenizer
    private var _chipWatchers: ArrayList<ChipWatcher> = ArrayList()
    val chipWatchers: List<ChipWatcher>
        get() = _chipWatchers

    private var _chipBackgroundColor by Delegates.notNull<Int>()
    private var _chipStrokeColor by Delegates.notNull<Int>()
    private var _chipTextColor by Delegates.notNull<Int>()

    private var _allowedChipStrings = mutableListOf<String>()
    private var _chipStartMargin = 0
    private var _chipStrokeWidth = 0f

    init {
        isSaveEnabled = true
        if (_textWatchers.isNullOrEmpty()) {
            _textWatchers.add(MorphInputTextWatcher())
        }
        this.setTokenizer(_tokenizer)
        this.inputType = EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        this.gravity = Gravity.CENTER_VERTICAL

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.morphInputText,
            0, 0
        ).apply {
            try {
                _chipBackgroundColor = getColor(
                    R.styleable.morphInputText_chipBackgroundColor,
                    ColorUtils.setAlphaComponent(
                        context.getColorFromAttr(R.attr.colorOnSurface),
                        25
                    )
                )
                _chipStrokeColor = getColor(
                    R.styleable.morphInputText_chipStrokeColor,
                    context.getColorFromAttr(R.attr.colorOnSurface)
                )
                _chipTextColor = getColor(
                    R.styleable.morphInputText_chipTextColor,
                    ColorUtils.setAlphaComponent(
                        context.getColorFromAttr(R.attr.colorOnSurface),
                        196
                    )
                )

                _chipStartMargin = getInteger(R.styleable.morphInputText_chipStartMargin, 10)
                _chipStrokeWidth = getDimension(R.styleable.morphInputText_chipStrokeWidth, 1f)
            } finally {
                recycle()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        _textWatchers.forEach {
            this.addTextChangedListener(it)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _textWatchers.forEach {
            this.removeTextChangedListener(it)
        }
    }

    fun removeChipChangedListener(chipWatcher: ChipWatcher) {
        _chipWatchers.remove(chipWatcher)
    }

    fun addChipChangedlistener(chipWatcher: ChipWatcher) {
        _chipWatchers.add(chipWatcher)
    }

    fun clear() {
        text.clear()
    }

    override fun setFilters(filters: Array<out InputFilter>?) {
        super.setFilters(filters?.let { arrayOf(*it) }?.plus(NoDoubleCommaInputFilter))
    }

    override fun <T> setAdapter(adapter: T) where T : ListAdapter?, T : Filterable? {
        adapter?.let {
            for (i in 0 until adapter.count) {

                val chip = adapter.getItem(i).toString()

                _allowedChipStrings.add(chip)
            }
        }
        super.setAdapter(adapter)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_DEL) {
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (deleteChip()) {
                    return super.dispatchKeyEvent(event)
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private fun deleteChip(): Boolean {
        val editable = editableText
        val spans = editable.getSpans(0, editable.length, ChipSpan::class.java)
        for (chipSpan in spans) {
            if (editable.getSpanEnd(chipSpan) == selectionStart && selectionStart == selectionEnd) {

                editable.removeSpan(chipSpan)
                sendBeforeChipChanged(chipSpan, chips.size, chips.size - 1)

                return true
            }
        }
        return false
    }

    fun addChip(chipText: SpannableString, start: Int, end: Int) {

        /**
         * Chip to be added if there is no matching trait already in the editText.
         */
        val newChip = createChipSpan(chipText)

        var joinedTrait: Trait? = null
        var chipToBeRemoved: ChipSpan? = null
        /**
         * Go chip by chip and check if the traits have just 2 mutations (if they do they can be
         * hypothetically joined).
         */
        for (chip in chips) {
            val trait = chip.getTrait()
            val newTrait = newChip.getTrait()
            if (trait != null && newTrait != null) {
                val totalMutations = listOfNotNull(
                    trait.geneLG1,
                    trait.geneLG2,
                    newTrait.geneLG1,
                    newTrait.geneLG2
                )
                if (totalMutations.size == 2) {
                    joinedTrait = Trait.fromValue(totalMutations[0], totalMutations[1])
                    if (joinedTrait != null){
                        chipToBeRemoved = chip
                        break
                    }
                }
            }
        }

        if (joinedTrait != null) {
            val spannedString = SpannableString(joinedTrait.formattedString)
            val joinedChip = createChipSpan(spannedString)
            var newText = ""
            (chips.toList() - chipToBeRemoved!!).forEach {
                newText += it.text + ","
            }
            newText = newText + joinedChip.text + ","
            editableText.replace(0, editableText.length, newText)
        } else {

            sendBeforeChipChanged(newChip, chips.size, chips.size + 1)
            editableText.setSpan(
                newChip,
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

    }

    /**
     * Create chip span with specified parameters.
     */
    private fun createChipSpan(chipText: SpannableString): ChipSpan {
        val chip = ChipDrawable.createFromAttributes(
            context,
            null,
            0,
            R.style.Widget_MaterialComponents_Chip_Filter
        )
        chip.text = chipText
        chip.setTextAppearanceResource(R.style.TextAppearance_App_StandaloneChip)
        chip.chipBackgroundColor = ColorStateList.valueOf(_chipBackgroundColor)
        chip.chipStrokeColor = ColorStateList.valueOf(_chipStrokeColor)
        chip.chipStrokeWidth = _chipStrokeWidth
        chip.chipMinHeight = chip.chipMinHeight - 10.dp
        chip.textStartPadding = 10f
        chip.textEndPadding = 5f
        chip.setBounds(
            _chipStartMargin,
            0,
            chip.intrinsicWidth + _chipStartMargin,
            chip.intrinsicHeight
        )
        return ChipSpan(chip, Trait.getTraitFromString(chipText.toString()))
    }

    /**
     * Notify every subscribed chipWatcher of the chip being added
     */
    private fun sendBeforeChipChanged(
        changeChip: ChipSpan,
        chipCountBefore: Int,
        chipCountAfter: Int
    ) {
        chipWatchers.forEach { chipWatcher ->
            chipWatcher.onChipChanged(
                changeChip,
                chips.toList(),
                chipCountBefore,
                chipCountAfter
            )
        }
    }

    inner class MorphInputTextWatcher : TextWatcher {

        override fun beforeTextChanged(
            charSequence: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {

        }

        override fun onTextChanged(
            charSequence: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {

        }

        override fun afterTextChanged(editable: Editable) {

            var previousToken = -1
            /**
             * Search for commas
             */
            editable.forEachIndexed { index, char ->
                if (char == ',') {

                    var isTokenUsed = false

                    /**
                     * Check if current comma is already been used as another spannable token
                     */
                    for (i in chips.indices) {
                        if (editable.getSpanEnd(chips[i]) == index + 1) {
                            isTokenUsed = true
                            break
                        }
                    }
                    /**
                     * If it was not already used, add chip span using current comma as token.
                     */
                    if (!isTokenUsed) {
                        val chipSpannable =
                            SpannableString(editable.subSequence(previousToken + 1, index))
                        addChip(chipSpannable, previousToken + 1, index + 1)
                    }

                    previousToken = index
                }
            }
        }
    }
}

interface ChipWatcher {

    fun onChipChanged(
        chip: ChipSpan,
        chipList: List<ChipSpan>,
        chipCountBefore: Int,
        chipCountAfter: Int
    ): Boolean {
        return true
    }
}