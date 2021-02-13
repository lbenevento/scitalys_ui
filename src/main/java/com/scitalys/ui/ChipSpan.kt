package com.scitalys.ui

import android.graphics.*
import android.text.style.ImageSpan
import com.google.android.material.chip.ChipDrawable
import com.scitalys.bp_traits.Trait

class ChipSpan(
    drawable: ChipDrawable,
    trait: Trait?,
    val verticalAlignmentSpan: Int = ALIGN_BOTTOM
) : ImageSpan(drawable) {

    val text: String
        get() = (drawable as ChipDrawable).text.toString()

    private var initialDescent = 0
    private var extraSpace = 0

    private var _trait: Trait? = trait

    fun getTrait(): Trait? {
        return _trait
    }

    fun setTrait(trait: Trait) {
        _trait = trait
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {

        val rect = drawable.bounds

//        if (fm != null) {
//            fm.ascent = -rect.bottom
//            fm.descent = 0
//
//            fm.top = fm.ascent
//            fm.bottom = 0
//        }

        if (fm != null) {
            if (rect.bottom - (fm.descent - fm.ascent) >= 0){
                initialDescent = fm.descent
                extraSpace = rect.bottom - (fm.descent - fm.ascent)
            }

            fm.descent = extraSpace / 2 + initialDescent
            fm.bottom = fm.descent

            fm.ascent = -rect.bottom + fm.descent
            fm.top = fm.ascent
        }

        return rect.right// + offset

    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {

        val b = drawable

        canvas.save()

        var transY = bottom - b.bounds.bottom
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.fontMetricsInt.descent
        } else if (mVerticalAlignment == ALIGN_CENTER) {
            transY = (bottom - top) / 2 - b.bounds.height() / 2
        }

        canvas.translate(x, transY.toFloat())
        b.draw(canvas)
        canvas.restore()

    }


}