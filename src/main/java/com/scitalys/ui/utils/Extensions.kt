package com.scitalys.ui.utils

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue

inline val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    ).toInt()
inline val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
    val inverseRatio = 1f - ratio

    val a = (Color.alpha(color1) * inverseRatio) + (Color.alpha(color2) * ratio)
    val r = (Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio)
    val g = (Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio)
    val b = (Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio)

    return Color.argb(a.toInt(), r.toInt(), g.toInt(), b.toInt())
}

inline fun getValueAnimator(
    forward: Boolean = true,
    duration: Long,
    interpolator: TimeInterpolator,
    crossinline updateListener: (progress: Float) -> Unit
): ValueAnimator {
    val a =
        if (forward) ValueAnimator.ofFloat(0f, 1f)
        else ValueAnimator.ofFloat(1f, 0f)
    a.addUpdateListener { updateListener(it.animatedValue as Float) }
    a.duration = duration
    a.interpolator = interpolator
    return a
}