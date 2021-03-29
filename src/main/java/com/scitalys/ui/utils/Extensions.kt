package com.scitalys.ui.utils

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.FloatRange

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

fun getOverlayAlpha(elevation: Float): Float {
    return when (val elevationInDp = elevation / 1f.dp) {

        in 24f..Float.MAX_VALUE -> .16f
        in 16f..24f -> elevationInDp.map(16f..24f, 0.15f..0.16f)
        in 12f..16f -> elevationInDp.map(12f..16f, 0.14f..0.15f)
        in 8f..12f -> elevationInDp.map(8f..12f, 0.12f..0.14f)
        in 6f..8f -> elevationInDp.map(6f..8f, 0.11f..0.12f)
        in 4f..6f -> elevationInDp.map(4f..6f, 0.09f..0.11f)
        in 3f..4f -> elevationInDp.map(3f..4f,0.08f..0.09f)
        in 2f..3f -> elevationInDp.map(2f..3f,0.07f..0.08f)
        in 1f..2f -> elevationInDp.map(1f..2f,0.05f..0.07f)
        in 0f..1f -> elevationInDp.map(0f..1f,0f..0.05f)
        else -> throw IllegalStateException("Elevation cannot be negative. Was: $elevation")

    }
}

fun Float.map(
    originalRange: ClosedFloatingPointRange<Float>,
    targetRange: ClosedFloatingPointRange<Float>
): Float {
    val originalRangeSpan = originalRange.endInclusive - originalRange.start
    val targetRangeSpan = targetRange.endInclusive - targetRange.start
    return ((this - originalRange.start) * targetRangeSpan) / originalRangeSpan + targetRange.start
}