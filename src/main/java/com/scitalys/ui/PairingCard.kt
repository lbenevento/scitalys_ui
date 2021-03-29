package com.scitalys.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import com.google.android.material.card.MaterialCardView
import com.scitalys.bp_traits.models.Pairing
import com.scitalys.ui.databinding.PairingCardBinding
import com.scitalys.ui.utils.*

class PairingCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.pairingCardStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: PairingCardBinding

    private var collapsedWidth = -1
    private var expandedWidth = -1

    private var collapsedHeight = -1
    private var expandedHeight = -1

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

    private val _backgroundColor: ColorStateList
    private val _collapsedBackgroundColor: Int
    private val _expandedBackgroundColor: Int

    private var _collapsedElevation: Float
    private val _expandedElevation: Float
    private val _cardCornerRadius: Float
    private val _chevronTint: Int

    private var isFirstTime = true

    private var _pairing: Pairing? = null

    private lateinit var animator: ValueAnimator

    private val onSEExpandListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
            expandView.visibility = View.VISIBLE
            _isExpanding = true
        }

        override fun onAnimationEnd(animation: Animator?) {
            _isExpanded = true
            _isExpanding = false
        }

        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationRepeat(animation: Animator?) {}
    }

    private val onSECollapseListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
            _isCollapsing = true
        }

        override fun onAnimationEnd(animation: Animator?) {
            expandView.visibility = View.GONE
            _isExpanded = false
            _isCollapsing = false
        }

        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationRepeat(animation: Animator?) {}
    }

    /**
     * Binding variables
     */
    private val chevron: ImageView
    private val expandView: FrameLayout
    private val header: FrameLayout
    private val scaleContainer: ConstraintLayout

    private val isNightMode: Boolean
    private val bgOverlay: GradientDrawable = GradientDrawable()

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.pairing_card,
            this,
            true
        )
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
            defStyleAttr, R.style.Widget_ScitalysComponents_PairingCard
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
                _cardCornerRadius = getDimension(
                    R.styleable.pairingCard_cardCornerRadius,
                    4f.dp
                )
                _collapsedElevation = getDimension(
                    R.styleable.pairingCard_collapsedCardElevation,
                    1f.dp
                )
                _expandedElevation = getDimension(
                    R.styleable.pairingCard_expandedCardElevation,
                    4f.dp
                )
                if (this.hasValue(R.styleable.pairingCard_backgroundColor)) {
                    _backgroundColor = getColorStateList(
                        R.styleable.pairingCard_backgroundColor
                    )!!
                } else {
                    throw Resources.NotFoundException("Cannot get background color")
                }
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

        this.strokeColor = Color.TRANSPARENT
        this.strokeWidth = _strokeWidth.toInt()
        this.cardElevation = _collapsedElevation
        this.radius = _cardCornerRadius

        _collapsedBackgroundColor = _backgroundColor.defaultColor
        _expandedBackgroundColor = _backgroundColor.getColorForState(
            intArrayOf(android.R.attr.state_expanded),
            _collapsedBackgroundColor
        )
        this.setCardBackgroundColor(_collapsedBackgroundColor)

        chevron.setColorFilter(_chevronTint)


        isNightMode =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        if (isNightMode) {
            bgOverlay.cornerRadius = _cardCornerRadius
            bgOverlay.setColor(getOverlayColor(_collapsedElevation))
            scaleContainer.background = bgOverlay
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            heightMeasureSpec
        )

        post {
            if (collapsedWidth < 0 || expandedWidth < 0) {
                collapsedWidth =
                    MeasureSpec.getSize(widthMeasureSpec) - 24.dp - paddingLeft - paddingRight
                expandedWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
            }

            if (collapsedHeight < 0) {
                expandView.visibility = View.GONE
                this.measure(
                    MeasureSpec.makeMeasureSpec(collapsedWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                collapsedHeight = this.measuredHeight
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

            if (collapsedWidth > 0 && expandedWidth > 0 && isFirstTime) {
                this.layoutParams.width = collapsedWidth
                isFirstTime = false
            }
        }
    }

    fun setPairing(pairing: Pairing) {
        _pairing = pairing

    }

    fun getPairing(): Pairing? {
        return _pairing
    }

    fun addHeader(view: View) {
        header.addView(view)
    }

    fun removeHeader() {
        header.removeAllViews()
    }

    fun addBody(view: View) {
        expandView.addView(view)
    }

    fun removeBody() {
        expandView.removeAllViews()
    }

    fun expand(animate: Boolean = true) {
        resize(true, animate)
    }

    fun collapse(animate: Boolean = true) {
        resize(false, animate)
    }

    /**
     * BG Color
     */
    fun getBackgroundColorStateList(): ColorStateList {
        return _backgroundColor
    }

    private fun resize(expand: Boolean, animate: Boolean) {

        if (animate) {
            if (::animator.isInitialized) {
                if (animator.isRunning) {
                    // Should be expanded, so reverse the animation if it is collapsing
                    if (expand && _isCollapsing) {
                        animator.setOnSEForExpand()
                    }
                    // Should be collapsed, so reverse the animation if it is expanding
                    else if (!expand && _isExpanding) {
                        animator.setOnSEForCollapse()
                    }
                    animator.reverse()
                } else {
                    // Should be expanded, so set proper onStart, onEnd and float values
                    if (expand && !_isExpanded) {
                        animator.setFloatValues(0f, 1f)
                        animator.setOnSEForExpand()
                    }
                    // Should be collapsed, se set proper onStart, onEnd and float values
                    else if (!expand && _isExpanded) {
                        animator.setFloatValues(1f, 0f)
                        animator.setOnSEForCollapse()
                    }
                    animator.start()
                }
            } else {
                animator = getNewAnimator(expand)
                animator.start()
            }
        } else {
            expandView.visibility =
                if (expand && expandedHeight >= 0) View.VISIBLE else View.GONE
            setResizeProgress(if (expand) 1f else 0f)
        }
    }

    private fun setResizeProgress(progress: Float) {
        if (expandedHeight > 0 && collapsedHeight > 0) {
            layoutParams.height =
                (collapsedHeight + (expandedHeight - collapsedHeight) * progress).toInt()
        }
        layoutParams.width =
            (collapsedWidth + (expandedWidth - collapsedWidth) * progress).toInt()



        this.setCardBackgroundColor(
            blendColors(
                _collapsedBackgroundColor,
                _expandedBackgroundColor,
                progress
            )
        )

        chevron.rotation = 90 * progress

        expandView.alpha = progress

        strokeColor = blendColors(Color.TRANSPARENT, _strokeColor, progress)
        cardElevation = _collapsedElevation + (_expandedElevation - _collapsedElevation) * progress

        if (isNightMode) {
            bgOverlay.setColor(getOverlayColor(cardElevation))
            scaleContainer.background = bgOverlay
        }

        requestLayout()
    }

    private fun getNewAnimator(expand: Boolean): ValueAnimator {
        animator = getValueAnimator(
            expand, expandDuration, AccelerateDecelerateInterpolator()
        ) { progress ->
            setResizeProgress(progress)
        }

        if (expand) animator.setOnSEForExpand()
        else animator.setOnSEForCollapse()

        return animator
    }

    private fun Animator.setOnSEForExpand() {
        this.removeListener(onSECollapseListener)
        this.addListener(onSEExpandListener)
    }

    private fun Animator.setOnSEForCollapse() {
        this.removeListener(onSEExpandListener)
        this.addListener(onSECollapseListener)
    }

//    private fun TypedArray.getExpandedBackgroundColor(): Color {
//        val colorStateList = this.getColorStateList(R.styleable.pairingCard_backgroundColor)
//            ?: throw Resources.NotFoundException("Could not load background colors for PairingCard.")
//
//
//    }

    private fun getOverlayColor(elevation: Float): Int {

        val onSurface = Color.WHITE

        val alpha: Float = getOverlayAlpha(elevation)
        println(alpha)
        return ColorUtils.setAlphaComponent(onSurface, (alpha * 255f).toInt())

    }

}