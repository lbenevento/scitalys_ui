package com.scitalys.ui.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.google.android.material.chip.Chip
import com.scitalys.bp_traits.Morph
import com.scitalys.bp_traits.Trait
import com.scitalys.ui.R
import kotlin.math.floor

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

val Float.sp: Float get() = if (this == 0f) 0f else floor(fontDensity * this.toDouble()).toFloat()

fun createChipFromTrait(
    context: Context,
    trait: Pair<Trait, Float>,
    stroke: Boolean = true,
    textSize: Float? = null
): Chip {

    val chip = Chip(context)

    val actualTrait = trait.first

    val coallelicBG: Int
    val coallelicStroke: Int
    val coallelicText: Int

    val recessiveBG: Int
    val recessiveStroke: Int
    val recessiveText: Int

    val codominantBG: Int
    val codominantStroke: Int
    val codominantText: Int
    if (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
        coallelicBG = context.getColor(R.color.whiteOverlay_5)
        coallelicStroke = context.getColor(R.color.secondary_200)
        coallelicText = context.getColor(R.color.secondary_200)

        recessiveBG = context.getColor(R.color.whiteOverlay_5)
        recessiveStroke = context.getColor(R.color.primary_200)
        recessiveText = context.getColor(R.color.primary_200)

        codominantBG = context.getColor(R.color.whiteOverlay_5)
        codominantStroke = context.getColor(R.color.primary_500)
        codominantText = context.getColor(R.color.primary_500)
    } else {
        coallelicBG = context.getColor(R.color.secondary_200)
        coallelicStroke = context.getColor(R.color.secondary_500)
        coallelicText = context.getColor(R.color.secondary_050)

        recessiveBG = context.getColor(R.color.primary_050)
        recessiveStroke = context.getColor(R.color.primary_200)
        recessiveText = context.getColor(R.color.primary_500)

        codominantBG = context.getColor(R.color.primary_200)
        codominantStroke = context.getColor(R.color.primary_500)
        codominantText = context.getColor(R.color.primary_050)
    }

    if (actualTrait.geneLG1 != null && actualTrait.geneLG2 != null) {

        chip.chipBackgroundColor = ColorStateList.valueOf(coallelicBG)
        chip.chipStrokeColor = ColorStateList.valueOf(coallelicStroke)
        chip.setTextColor(coallelicText)

    } else if (actualTrait.geneLG1 == null && actualTrait.geneLG2 != null) {

        chip.chipBackgroundColor = ColorStateList.valueOf(recessiveBG)
        chip.chipStrokeColor = ColorStateList.valueOf(recessiveStroke)
        chip.setTextColor(recessiveText)

    } else {

        chip.chipBackgroundColor = ColorStateList.valueOf(codominantBG)
        chip.chipStrokeColor = ColorStateList.valueOf(codominantStroke)
        chip.setTextColor(codominantText)

    }

    if (trait.second != 1f) {
        chip.text = context.getString(
            R.string.hetProbability,
            (trait.second * 100).toInt(),
            trait.first.formattedString
        )
    } else {
        chip.text = trait.first.formattedString
    }

    if (stroke) {
        chip.chipStrokeWidth = context.resources.getDimension(R.dimen.chip_stroke)
    }

    if (textSize != null) {
        chip.textSize = textSize
    } else {
        chip.textSize = 13f
    }

    chip.setEnsureMinTouchTargetSize(false)

    chip.setPadding(
        5,
        5,
        5,
        5,
    )
    chip.textStartPadding = 15f
    chip.textEndPadding = 15f
    chip.chipEndPadding = 0f
    chip.chipStartPadding = 0f
    chip.iconStartPadding = 0f
    chip.textAlignment = View.TEXT_ALIGNMENT_CENTER

    chip.minWidth = 0
    chip.minimumWidth = 0

    chip.minHeight = 0
    chip.chipMinHeight = 0f

    return chip
}

fun createChipFromMorph(
    context: Context,
    morphPair: Pair<Morph, Float>,
    stroke: Boolean = true,
    textSize: Float? = null
): Chip {

    val chip = Chip(context)

    val morph = morphPair.first

    val coallelicBG: Int
    val coallelicStroke: Int
    val coallelicText: Int

    val recessiveBG: Int
    val recessiveStroke: Int
    val recessiveText: Int

    val codominantBG: Int
    val codominantStroke: Int
    val codominantText: Int
    if (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
        coallelicBG = context.getColor(R.color.whiteOverlay_5)
        coallelicStroke = context.getColor(R.color.secondary_200)
        coallelicText = context.getColor(R.color.secondary_200)

        recessiveBG = context.getColor(R.color.whiteOverlay_5)
        recessiveStroke = context.getColor(R.color.primary_200)
        recessiveText = context.getColor(R.color.primary_200)

        codominantBG = context.getColor(R.color.whiteOverlay_5)
        codominantStroke = context.getColor(R.color.primary_500)
        codominantText = context.getColor(R.color.primary_500)
    } else {
        coallelicBG = context.getColor(R.color.secondary_200)
        coallelicStroke = context.getColor(R.color.secondary_500)
        coallelicText = context.getColor(R.color.secondary_050)

        recessiveBG = context.getColor(R.color.primary_050)
        recessiveStroke = context.getColor(R.color.primary_200)
        recessiveText = context.getColor(R.color.primary_500)

        codominantBG = context.getColor(R.color.primary_200)
        codominantStroke = context.getColor(R.color.primary_500)
        codominantText = context.getColor(R.color.primary_050)
    }

    val locus1 = morph.mutations.first().locus1
    val locus2 = morph.mutations.first().locus2
    if (locus1 != null && locus2 != null) {

        chip.chipBackgroundColor = ColorStateList.valueOf(coallelicBG)
        chip.chipStrokeColor = ColorStateList.valueOf(coallelicStroke)
        chip.setTextColor(coallelicText)

    } else if (locus1 == null && locus2 != null) {

        chip.chipBackgroundColor = ColorStateList.valueOf(recessiveBG)
        chip.chipStrokeColor = ColorStateList.valueOf(recessiveStroke)
        chip.setTextColor(recessiveText)

    } else {

        chip.chipBackgroundColor = ColorStateList.valueOf(codominantBG)
        chip.chipStrokeColor = ColorStateList.valueOf(codominantStroke)
        chip.setTextColor(codominantText)

    }

    if (morphPair.second != 1f) {
        chip.text = context.getString(
            R.string.hetProbability,
            (morphPair.second * 100).toInt(),
            morphPair.first.formattedString
        )
    } else {
        chip.text = morphPair.first.formattedString
    }

    if (stroke) {
        chip.chipStrokeWidth = context.resources.getDimension(R.dimen.chip_stroke)
    }

    if (textSize != null) {
        chip.textSize = textSize
    } else {
        chip.textSize = 13f
    }

    chip.setEnsureMinTouchTargetSize(false)

    chip.setPadding(
        5,
        5,
        5,
        5,
    )
    chip.textStartPadding = 15f
    chip.textEndPadding = 15f
    chip.chipEndPadding = 0f
    chip.chipStartPadding = 0f
    chip.iconStartPadding = 0f
    chip.textAlignment = View.TEXT_ALIGNMENT_CENTER

    chip.minWidth = 0
    chip.minimumWidth = 0

    chip.minHeight = 0
    chip.chipMinHeight = 0f

    return chip
}

inline val density
    get() = Resources.getSystem().displayMetrics.density
inline val fontDensity
    get() = Resources.getSystem().displayMetrics.scaledDensity