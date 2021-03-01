package com.scitalys.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.FloatValueHolder
import com.google.android.material.card.MaterialCardView
import com.scitalys.ui.databinding.PairingCardBinding
import com.scitalys.ui.utils.blendColors
import com.scitalys.ui.utils.dp
import com.scitalys.ui.utils.getColorFromAttr
import com.scitalys.ui.utils.getValueAnimator


class PairingCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: PairingCardBinding

    private var collapsedWidth = -1
    private var expandedWidth = -1

    private var collapsedHeight = -1
    private var expandedHeight = -1

    private var expandViewHeight = -1

    private val collapsedBg: Int
    private val expandedBg: Int

    private val animationPlaybackSpeed = 0.8f
    private val expandDuration: Long
        get() = (300L / animationPlaybackSpeed).toLong()

    private var _isExpanded = false
    val isExpanded: Boolean
        get() = _isExpanded

    private var _isExpanding = false
    val isExpanding: Boolean
        get() = _isExpanding
    private var _isCollapsing = true
    val isCollapsing: Boolean
        get() = _isCollapsing

    private val _strokeColor: Int
    private val _strokeWidth: Float
    private val _cardElevation: Float
    private val _chevronTint: Int

    private var isFirstTime = true

    /**
     * Binding variables
     */
    private val cardContentContainer: FrameLayout
    private val cardView: MaterialCardView
    private val chevron: ImageView
    private val expandView: FrameLayout
    private val header: FrameLayout
    private val scaleContainer: ConstraintLayout

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.pairing_card,
            this,
            true
        )
        cardContentContainer = binding.cardContentContainer
        cardView = binding.cardView
        chevron = binding.chevron
        expandView = binding.expandView
        header = binding.header
        scaleContainer = binding.scaleContainer

        /**
         * Get Styled Attributes
         */
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.pairingCard,
            0, 0
        ).apply {
            try {
                _strokeColor = getColor(
                    R.styleable.pairingCard_strokeColor,
                    ColorUtils.setAlphaComponent(
                        context.getColorFromAttr(R.attr.colorOnSurface),
                        196
                    )
                )
                _strokeWidth = getDimension(
                    R.styleable.pairingCard_strokeWidth,
                    1f.dp
                )
                _cardElevation = getDimension(
                    R.styleable.pairingCard_cardElevation,
                    1f.dp
                )
                collapsedBg = getColor(
                    R.styleable.pairingCard_collapsedBackgroundColor,
                    ColorUtils.setAlphaComponent(
                        context.getColorFromAttr(R.attr.colorOnSurface),
                        25
                    )
                )
                expandedBg = getColor(
                    R.styleable.pairingCard_expandedBackgroundColor,
                    ColorUtils.setAlphaComponent(
                        context.getColorFromAttr(R.attr.colorOnSurface),
                        25
                    )
                )
                _chevronTint = getColor(
                    R.styleable.pairingCard_chevronTint,
                    ColorUtils.setAlphaComponent(
                        context.getColorFromAttr(R.attr.colorOnSurface),
                        200
                    )
                )
            } finally {
                recycle()
            }
        }

        /**
         * Set retrieved styled attributes
         */

        cardView.strokeColor = Color.TRANSPARENT
        cardView.elevation = 0f
        cardView.strokeWidth = _strokeWidth.toInt()
        cardView.setCardBackgroundColor(ColorStateList.valueOf(collapsedBg))
        chevron.setColorFilter(_chevronTint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        if (collapsedWidth < 0 || expandedWidth < 0) {
            collapsedWidth = MeasureSpec.getSize(widthMeasureSpec) - 24.dp - paddingLeft - paddingRight
            expandedWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        }

        if (collapsedHeight < 0) {
            expandView.visibility = View.GONE
            cardView.measure(
                MeasureSpec.makeMeasureSpec(collapsedWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            collapsedHeight = cardView.measuredHeight
        }
        if (expandedHeight < 0) {
            expandView.visibility = View.VISIBLE
            expandView.measure(
                MeasureSpec.makeMeasureSpec(scaleContainer.measuredWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            expandedHeight = collapsedHeight + expandView.measuredHeight
            expandView.visibility = View.GONE
        }

        if(collapsedWidth > 0 && expandedWidth > 0 && isFirstTime){
            cardView.layoutParams.width = collapsedWidth
            isFirstTime = false
        }

        super.onMeasure(
            widthMeasureSpec,
            heightMeasureSpec
        )
    }

    fun addHeader(view: View) { header.addView(view) }
    fun removeHeader() { header.removeAllViews() }

    fun addBody(view: View) { expandView.addView(view) }
    fun removeBody() { expandView.removeAllViews() }

    fun expand(animate: Boolean = true) { resize(true, animate) }
    fun collapse(animate: Boolean = true) { resize(false, animate) }

    /**
     * Stroke Color
     */
    fun setStrokeColor(strokeColor: Int) {
        cardView.strokeColor = strokeColor
    }
    fun getStrokeColor(): Int = _strokeColor

    /**
     * Stroke Width
     */
    fun setStrokeWidth(strokeWidth: Int) {
        cardView.strokeWidth = strokeWidth
    }
    fun getStrokeWidth(): Float = _strokeWidth

    /**
     * Elevation
     */
    override fun setElevation(elevation: Float) {
        cardView.elevation = elevation
    }
    override fun getElevation(): Float = _cardElevation

    private fun resize(expand: Boolean, animate: Boolean) {

        val startingHeight = binding.cardView.height

        if (animate) {
            val animator = getValueAnimator(
                expand, expandDuration, AccelerateDecelerateInterpolator()
            ) { progress ->
                setResizeProgress(progress, startingHeight)
            }

            if (expand) {
                animator.doOnStart {
                    expandView.visibility = View.VISIBLE
                    _isExpanding = true
                }
                animator.doOnEnd {
                    _isExpanded = true
                    _isExpanding = false
                }
            } else {
                animator.doOnStart {
                    _isCollapsing = true
                }
                animator.doOnEnd {
                    expandView.visibility = View.GONE
                    _isExpanded = false
                    _isCollapsing = false
                }
            }

            animator.start()
        } else {
            expandView.visibility =
                if (expand && expandedHeight >= 0) View.VISIBLE else View.GONE
            setResizeProgress(if (expand) 1f else 0f, startingHeight)
        }
    }

    private fun setResizeProgress(progress: Float, startingHeight: Int) {
        if (expandedHeight > 0 && collapsedHeight > 0) {
            cardView.layoutParams.height =
                (collapsedHeight + (expandedHeight - startingHeight) * progress).toInt()
        }
        cardView.layoutParams.width =
            (collapsedWidth + (expandedWidth - collapsedWidth) * progress).toInt()

        cardContentContainer.setBackgroundColor(blendColors(collapsedBg, expandedBg, progress))

        this.chevron.rotation = 90 * progress

        expandView.alpha = progress

        cardView.strokeColor = blendColors(Color.TRANSPARENT, _strokeColor, progress)
        cardView.elevation = cardView.elevation * progress

        cardView.requestLayout()
    }
}