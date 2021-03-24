package com.scitalys.ui

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.chip.Chip
import com.scitalys.bp_traits.Trait

class TraitChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.traitChipStyle
) : Chip(context, attrs, defStyleAttr) {

    private var trait: Trait? = null

    private var codominantBG: Int
    private var codominantText: Int
    private var codominantStroke: Int

    private var recessiveBG: Int
    private var recessiveText: Int
    private var recessiveStroke: Int

    private var coallelicBG: Int
    private var coallelicText: Int
    private var coallelicStroke: Int

    private var chipTopPadding: Float
    private var chipBottomPadding: Float

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TraitChip,
            defStyleAttr, R.style.Widget_ScitalysComponents_TraiChip
        ).apply {
            try {
                codominantBG = getColor(
                    R.styleable.TraitChip_colorCodominantBackground,
                    0
                )
                codominantText = getColor(
                    R.styleable.TraitChip_colorCodominantText,
                    0
                )
                codominantStroke = getColor(
                    R.styleable.TraitChip_colorCodominantStroke,
                    0
                )

                recessiveBG = getColor(
                    R.styleable.TraitChip_colorRecessiveBackground,
                    0
                )
                recessiveText = getColor(
                    R.styleable.TraitChip_colorRecessiveText,
                    0
                )
                recessiveStroke = getColor(
                    R.styleable.TraitChip_colorRecessiveStroke,
                    0
                )

                coallelicBG = getColor(
                    R.styleable.TraitChip_colorCoallelicBackground,
                    0
                )
                coallelicText = getColor(
                    R.styleable.TraitChip_colorCoallelicText,
                    0
                )
                coallelicStroke = getColor(
                    R.styleable.TraitChip_colorCoallelicStroke,
                    0
                )
                /**
                 * Paddings
                 */
                chipTopPadding = getDimension(
                    R.styleable.TraitChip_chipTopPadding,
                    0F
                )
                chipBottomPadding = getDimension(
                    R.styleable.TraitChip_chipBottomPadding,
                    0F
                )
            } finally {
                recycle()
            }
        }
        trait?.let {
            setupChip(it)
        }
    }

    fun setTrait(_trait: Trait) {
        trait = _trait
        setupChip(_trait)
    }

    fun getTrait(): Trait? {
        return trait
    }

    private fun setupChip(_trait: Trait) {
        this.text = _trait.formattedString

        val bgColor: ColorStateList
        val strokeColor: ColorStateList
        val textColor: Int

//        when (_trait.geneType) {
//            GeneType.CODOMINANT -> {
//                bgColor = ColorStateList.valueOf(this.codominantBG)
//                strokeColor = ColorStateList.valueOf(codominantStroke)
//                textColor = this.codominantBG
//            }
//            GeneType.DOMINANT -> {
//                bgColor = ColorStateList.valueOf(this.codominantBG)
//                strokeColor = ColorStateList.valueOf(codominantStroke)
//                textColor = this.codominantBG
//            }
//            GeneType.RECESSIVE -> {
//                bgColor = ColorStateList.valueOf(recessiveBG)
//                strokeColor = ColorStateList.valueOf(recessiveStroke)
//                textColor = this.recessiveStroke
//            }
//            GeneType.COALLELIC -> {
//                bgColor = ColorStateList.valueOf(this.coallelicBG)
//                strokeColor = ColorStateList.valueOf(coallelicStroke)
//                textColor = this.coallelicText
//            }
//            else -> throw IllegalStateException("geneType cannot be ${_trait.geneType}")
//        }

        /**
         * Super
         */
        if (_trait.geneLG1 != null && _trait.geneLG2 != null) {
            bgColor = ColorStateList.valueOf(this.coallelicBG)
            strokeColor = ColorStateList.valueOf(coallelicStroke)
            textColor = this.coallelicText
        }
        /**
         * Het
         */
        else if (_trait.geneLG1 == null && _trait.geneLG2 != null) {
            bgColor = ColorStateList.valueOf(recessiveBG)
            strokeColor = ColorStateList.valueOf(recessiveStroke)
            textColor = this.recessiveText
        }
        /**
         * Codom
         */
        else {
            bgColor = ColorStateList.valueOf(this.codominantBG)
            strokeColor = ColorStateList.valueOf(codominantStroke)
            textColor = this.codominantText
        }

        this.chipBackgroundColor = bgColor
        this.chipStrokeColor = strokeColor
        this.setTextColor(textColor)

//        chip.setEnsureMinTouchTargetSize(false)

        /**
         * Paddings
         */
        val paddingTop = paddingTop + chipTopPadding
        val paddingBottom = paddingBottom + chipBottomPadding

        /**
         * Support for RTL languages.
         */
//        val config = resources.configuration
//        if (config.layoutDirection == View.LAYOUT_DIRECTION_LTR) {
//            paddingLeft = chipStartPadding
//            paddingRight = chipEndPadding
//        } else {
//            paddingLeft = chipEndPadding
//            paddingRight = chipStartPadding
//        }

        this.setPadding(
            paddingLeft,
            paddingTop.toInt(),
            paddingRight,
            paddingBottom.toInt(),
        )

//        chip.textStartPadding = 15f
//        chip.textEndPadding = 15f
//        chip.chipEndPadding = 0f
//        chip.chipStartPadding = 0f
//        chip.textAlignment = TEXT_ALIGNMENT_CENTER
//
//        chip.minWidth = 0
//
//        chip.minHeight = 0
//        chip.chipMinHeight = 0f
    }
}