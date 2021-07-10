package com.scitalys.ui.morphInputText

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.ChipGroup
import com.scitalys.bp_traits.Trait
import com.scitalys.bp_traits.models.Specimen
import com.scitalys.ui.R
import com.scitalys.ui.utils.createChipFromTrait
import com.scitalys.ui.utils.dp
import com.scitalys.ui.utils.sp
import java.util.*

/**
 * This class represents the dataset for the array.
 */

data class ChipifyArrayObject(
    val specimens: List<Specimen>,
    val traits: List<Trait>
)

class ChipifyArrayAdapter(
    private val context: Context,
    specimens: List<Specimen>,
    traits: List<Trait>
) : BaseAdapter(), Filterable {

    private val textSize = 6f.sp

    val lock = Object()

    val originalObjects = ChipifyArrayObject(
        specimens,
        traits
    )
    private val isSpecimensSectionEnabled = specimens.isNotEmpty()

    var objects = ChipifyArrayObject(
        specimens,
        traits
    )
    lateinit var filter: ArrayFilter


    /**
     * Total count is the sum of specimens.size and traits.size.
     */
    override fun getCount(): Int {
        return if (isSpecimensSectionEnabled) {
            objects.specimens.size + objects.traits.size + 2
        } else {
            objects.traits.size + 1
        }
    }

    override fun getItem(position: Int): String? {
        // Define positions
        val specimensTitlePosition: Int
        val specimensPositions: IntRange
        val traitsTitlePosition: Int

        if (isSpecimensSectionEnabled) {
            specimensTitlePosition = 0
            specimensPositions = 1..objects.specimens.size
            traitsTitlePosition = objects.specimens.size + 1
        } else {
            specimensTitlePosition = -1
            specimensPositions = -1..-1
            traitsTitlePosition = 0
        }
        return when (position) {
            specimensTitlePosition -> null
            traitsTitlePosition -> null
            in specimensPositions -> {
                val traitsList = objects.specimens[position - 1].traits.keys.toList()
                var string = ""
                traitsList.forEachIndexed { index, trait ->
                    string += if (index == traitsList.size - 1) {
                        trait.formattedString
                    } else {
                        trait.formattedString + ","
                    }
                }
                return string
            }
            else -> {
                // If specimensSection is enabled it subtract objects.specimens.size + 2 from position.
                // + 2 is because there are two section titles.
                // Else it just subtract one to it because there is only one title.
                val traitsIndex = if (isSpecimensSectionEnabled) {
                    position - objects.specimens.size - 2
                } else {
                    position - 1
                }
                objects.traits[traitsIndex].formattedString
            }
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflater = LayoutInflater.from(context)

        // Define positions
        val specimensTitlePosition: Int
        val specimensPositions: IntRange
        val traitsTitlePosition: Int

        if (isSpecimensSectionEnabled) {
            specimensTitlePosition = 0
            specimensPositions = 1..objects.specimens.size
            traitsTitlePosition = objects.specimens.size + 1
        } else {
            specimensTitlePosition = -1
            specimensPositions = -1..-1
            traitsTitlePosition = 0
        }

        return when (position) {
            specimensTitlePosition -> inflater.createSectionTitle(
                context.getString(R.string.specimen_colon),
                parent,
                isListEmpty = objects.specimens.isEmpty(),
                spacer = false
            )
            in specimensPositions -> inflater.createSpecimenItem(
                objects.specimens[position - 1]
            )
            traitsTitlePosition -> inflater.createSectionTitle(
                context.getString(R.string.traits_colon),
                parent,
                isListEmpty = objects.traits.isEmpty(),
                spacer = isSpecimensSectionEnabled
            )
            else -> {
                val traitsIndex = if (isSpecimensSectionEnabled) {
                    position - objects.specimens.size - 2
                } else {
                    position - 1
                }
                inflater.createTraitItem(objects.traits[traitsIndex])
            }
        }
    }

    override fun getFilter(): Filter {
        if (::filter.isInitialized) {
            return filter
        }
        filter = ArrayFilter()
        return filter
    }


    private fun LayoutInflater.createSectionTitle(
        title: String,
        parent: ViewGroup?,
        isListEmpty: Boolean,
        spacer: Boolean
    ): View {
        val view = this.inflate(
            R.layout.dropdown_menu_popup_section_title,
            parent,
            false
        )

        view.findViewById<TextView>(R.id.text_view).text = title
        if (isListEmpty) {
            view.findViewById<TextView>(R.id.no_specimen_found).visibility = View.VISIBLE
        }
        view.findViewById<View>(R.id.spacer).visibility = if (spacer) View.VISIBLE else View.GONE
        view.setOnClickListener { }
        return view
    }

    private fun LayoutInflater.createSpecimenItem(
        specimen: Specimen
    ): View {
        val constraintLayout = ConstraintLayout(context)
        val chipGroup = ChipGroup(context)

        for (trait in specimen.traits.keys) {
            chipGroup.addView(
                createChipFromTrait(
                    context,
                    Pair(trait, 1f),
                    textSize = textSize
                ).apply {
                    isClickable = false
                    isFocusable = false
                }
            )
        }
        constraintLayout.addView(chipGroup)

        constraintLayout.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chipGroup.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            this.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            this.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            this.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            this.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

            this.topMargin = 5.dp
            this.bottomMargin = 5.dp
        }

        return constraintLayout
    }

    private fun LayoutInflater.createTraitItem(
        trait: Trait
    ): View {

        val constraintLayout = ConstraintLayout(context)
        val chip = createChipFromTrait(
            context,
            Pair(trait, 1f),
            textSize = textSize
        )
        chip.apply {
            isClickable = false
            isFocusable = false
            isFocusableInTouchMode = false
            setOnCheckedChangeListener(null)
        }

        constraintLayout.addView(chip)
        constraintLayout.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        chip.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            this.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            this.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            this.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            this.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

            this.topMargin = 5.dp
            this.bottomMargin = 5.dp
        }
        return constraintLayout
    }

    inner class ArrayFilter : Filter() {

        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()
            if (prefix.isNullOrEmpty()) {
                val objects: ChipifyArrayObject
                synchronized(lock) { objects = originalObjects }
                results.values = objects
                results.count = objects.specimens.size + objects.traits.size - 1
            } else {
                val prefixString = prefix.toString().lowercase(Locale.getDefault())
                val objects: ChipifyArrayObject
                synchronized(lock) { objects = originalObjects }
                val count = objects.specimens.size + objects.traits.size - 1
                val newSpecimens = mutableListOf<Specimen>()
                val newTraits = mutableListOf<Trait>()
                for (i in 0 until count) {
                    // Check specimens
                    if (i < objects.specimens.size) {
                        val specimen = objects.specimens[i]

                        var specimenText = ""
                        specimen.traits.keys.forEach {
                            specimenText += it.formattedString.lowercase(Locale.getDefault()) + " "
                        }

                        // First match against the whole, non-splitted value
                        if (specimenText.startsWith(prefixString)) {
                            newSpecimens.add(specimen)
                        } else {
                            val words = specimenText.split(" ").toTypedArray()
                            words.forEach { word ->
                                if (word.startsWith(prefixString)) {
                                    newSpecimens.add(specimen)
                                }
                            }
                        }
                    }
                    // Check traits
                    else {
                        val trait = objects.traits[i - objects.specimens.size]
                        val traitText = trait.formattedString.lowercase(Locale.getDefault())

                        // First match against the whole, non-splitted value
                        if (traitText.startsWith(prefixString)) {
                            newTraits.add(trait)
                        } else {
                            val words = traitText.split(" ").toTypedArray()
                            words.forEach { word ->
                                if (word.startsWith(prefixString)) {
                                    newTraits.add(trait)
                                }
                            }
                        }
                    }
                }
                results.values = ChipifyArrayObject(newSpecimens, newTraits)
                results.count = newSpecimens.size + newTraits.size
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            objects =
                (results.values ?: ChipifyArrayObject(listOf(), listOf())) as ChipifyArrayObject
            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }


    }
}