package com.scitalys.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.SpannableString
import android.text.style.ReplacementSpan
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.AnnotatedString.Builder
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.buildSpannedString
import androidx.core.text.set
import com.scitalys.ui.theme.ScitalysTheme
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun MorphInputText(
    modifier: Modifier = Modifier
) {
    var text by remember {
        mutableStateOf("")
    }
    var dropdownExpanded by remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
        },
        modifier = modifier,
        trailingIcon = {
            IconButton(onClick = { dropdownExpanded = !dropdownExpanded }) {
                Icon(
                    painter = rememberVectorPainter(Icons.Rounded.ArrowDropDown),
                    contentDescription = stringResource(id = R.string.image_des_arrow_drop_down)
                )
            }
        },
        visualTransformation = ChipsVisualTransformations(),
        shape = RoundedCornerShape(50),
    )
}

class ChipsVisualTransformations() : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        var previousToken = -1
        /**
         * Search for commas
         */
        text.forEachIndexed { index, char ->
            if (char == ',') {

                var isTokenUsed = false

                /**
                 * Check if current comma is already been used as another spannable token
                 */
//                for (i in chips.indices) {
//                    if (editable.getSpanEnd(chips[i]) == index + 1) {
//                        isTokenUsed = true
//                        break
//                    }
//                }
                /**
                 * If it was not already used, add chip span using current comma as token.
                 */
                if (!isTokenUsed) {
                    val chipSpannable =
                        SpannableString(text.subSequence(previousToken + 1, index))
//                    addChip(chipSpannable, previousToken + 1, index + 1)
                }

                previousToken = index
            }
        }
        return TransformedText(
            text = with(Builder("Text")) {
                buildSpannedString {
                    append("Text")
                    this[0, 3] = ChipSpanV2()
                }

                toAnnotatedString()
            },
            offsetMapping = object: OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return offset
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return offset
                }

            }
        )
    }

}

class ChipSpanV2() : ReplacementSpan() {

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {

//        val rect = drawable.bounds
//
//        if (fm != null) {
//            if (rect.bottom - (fm.descent - fm.ascent) >= 0){
//                initialDescent = fm.descent
//                extraSpace = rect.bottom - (fm.descent - fm.ascent)
//            }
//
//            fm.descent = extraSpace / 2 + initialDescent
//            fm.bottom = fm.descent
//
//            fm.ascent = -rect.bottom + fm.descent
//            fm.top = fm.ascent
//        }

//        return rect.right// + offset
        return paint.measureText(text as String?).roundToInt()
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

        canvas.save()

        val textLength = x + paint.measureText(text.toString())
        val chipHeight = paint.textSize * 2.25
        val textOffsetVertical = paint.textSize * 1.45

        val chip = RectF(x, y.toFloat(), textLength, (y + chipHeight).toFloat())
        paint.color = android.graphics.Color.BLUE
        canvas.drawRoundRect(
            chip, 20f, 20f, paint
        )

        paint.color = android.graphics.Color.WHITE
        canvas.drawText(
            text!!.toString(),
            x,
            (y + textOffsetVertical).toFloat(),
            paint
        )

//        var transY = bottom - b.bounds.bottom
//        if (mVerticalAlignment == ImageSpan.ALIGN_BASELINE) {
//            transY -= paint.fontMetricsInt.descent
//        } else if (mVerticalAlignment == ImageSpan.ALIGN_CENTER) {
//            transY = (bottom - top) / 2 - b.bounds.height() / 2
//        }
//
//        canvas.translate(x, transY.toFloat())
//        b.draw(canvas)
        canvas.restore()
    }

}

@ExperimentalMaterialApi
@Preview(
    name = "Day",
    showBackground = true
)
@Composable
private fun Day() {
    ScitalysTheme {
        MorphInputText(
            modifier = Modifier.padding(10.dp)
        )
    }
}

//@Preview
//@Composable
//fun ChipSpanPreview() {
//    ScitalysTheme {
//        Text(
//            with(Builder("Text")) {
//                buildSpannedString {
//                    with(style = ChipSpanV2()) {
//                        append("Text")
//                    }
//
//                    this[0, 3] = ChipSpanV2()
//                }
//                toAnnotatedString()
//            }
//
//        )
//    }
//}