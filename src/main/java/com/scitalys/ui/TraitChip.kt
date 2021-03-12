package com.scitalys.ui

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.chip.Chip
import com.scitalys.bp_traits.GeneType
import com.scitalys.bp_traits.Trait
import java.lang.IllegalStateException

//    R.attr.colorCodomBackground,
//    R.attr.colorCodomText,
//    R.attr.colorCodomStroke,
//
//    R.attr.colorHetBackground,
//    R.attr.colorHetText,
//    R.attr.colorHetStroke,
//
//    R.attr.colorCoallelicBackground,
//    R.attr.colorCoallelicText,
//    R.attr.colorCoallelicStroke
//)

class TraitChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
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

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TraitChip,
            0, R.style.Widget_ScitalysComponents_TraiChip
        ).apply {
            try {
                codominantBG = getColor(
                    R.styleable.TraitChip_colorCodomBackground,
                    0
                )
                codominantText = getColor(
                    R.styleable.TraitChip_colorCodomText,
                    0
                )
                codominantStroke = getColor(
                    R.styleable.TraitChip_colorCodomStroke,
                    0
                )

                recessiveBG = getColor(
                    R.styleable.TraitChip_colorHetBackground,
                    0
                )
                recessiveText = getColor(
                    R.styleable.TraitChip_colorHetText,
                    0
                )
                recessiveStroke = getColor(
                    R.styleable.TraitChip_colorHetStroke,
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

        when (_trait.geneType) {
            GeneType.CODOMINANT -> {
                bgColor = ColorStateList.valueOf(this.codominantBG)
                strokeColor = ColorStateList.valueOf(codominantStroke)
                textColor = this.codominantBG
            }
            GeneType.DOMINANT -> {
                bgColor = ColorStateList.valueOf(this.codominantBG)
                strokeColor = ColorStateList.valueOf(codominantStroke)
                textColor = this.codominantBG
            }
            GeneType.RECESSIVE -> {
                bgColor = ColorStateList.valueOf(recessiveBG)
                strokeColor = ColorStateList.valueOf(recessiveStroke)
                textColor = this.recessiveStroke
            }
            GeneType.COALLELIC -> {
                bgColor = ColorStateList.valueOf(this.coallelicBG)
                strokeColor = ColorStateList.valueOf(coallelicStroke)
                textColor = this.coallelicText
            }
            else -> throw IllegalStateException("geneType cannot be ${_trait.geneType}")
        }

        this.chipBackgroundColor = bgColor
        this.chipStrokeColor = strokeColor
        this.setTextColor(textColor)
    }
}