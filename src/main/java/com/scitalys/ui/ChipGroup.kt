package com.scitalys.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.scitalys.ui.theme.ScitalysTheme
import com.scitalys.bp_traits.Trait

@ExperimentalMaterialApi
@Composable
fun ChipGroup(
    modifier: Modifier = Modifier,
    verticalContentSpacing: Dp = 6.dp,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        Layout(
            modifier = modifier,
            content = content
        ) { measurables, constraints ->

            var rows = 1

            val rowWidths = mutableListOf<Int>()
            val rowHeights = mutableListOf<Int>()

            var rowWidth = 0
            val placeables = measurables.mapIndexed { _, measurable ->
                val placeable = measurable.measure(constraints)

                if (rowWidth == 0 || rowWidth + placeable.width < constraints.maxWidth) {
                    rowWidth += placeable.width

                    // Initialize rowHeights for this row.
                    if (rowHeights.size < rows) {
                        rowHeights.add(0)
                    }

                    // Check if new placeable is higher than the last higher one
                    if (rowHeights[rows - 1] < placeable.height) {
                        // Assign the new height to the current row
                        rowHeights[rows - 1] = placeable.height
                    }
                } else {
                    rowWidths.add(rowWidth)
                    rowWidth = placeable.width

                    rows += 1

                    rowHeights.add(placeable.height)
                }

                placeable
            }

            // Add last row which cannot ever exceed constraint width by definition to rowWidths
            rowWidths.add(rowWidth)

            // Grid's height is the sum of the maximum elements per row plus verticalContentSpacing
            // times the number of spaces there has to be.
            val height = rowHeights.sum() + verticalContentSpacing.roundToPx() * (rowHeights.size - 1)
                .coerceIn(constraints.minHeight, constraints.maxHeight)
            // Grid's width is the widest row.
            val width = rowWidths.maxOrNull()
                ?.coerceIn(constraints.minWidth, constraints.maxWidth) ?: constraints.minWidth

            // Y of each row, based on the height accumulation of previous rows
            val rowY = IntArray(rows) { 0 }
            for (i in 1 until rows) {
                rowY[i] = rowY[i - 1] + rowHeights[i - 1]
            }

            layout(width, height) {
                var totalRowWidth = 0
                var placedUpToY = 0
                var placedUpToRow = 1
                placeables.forEach { placeable ->

                    val placeableWidth = placeable.width

                    if (totalRowWidth == 0 || totalRowWidth + placeableWidth <= width) {

                        placeable.placeRelative(x = totalRowWidth, y = placedUpToY)
                        totalRowWidth += placeableWidth

                    } else {

                        totalRowWidth = 0
                        val rowHeightWithSpacing =
                            rowHeights[placedUpToRow - 1] + verticalContentSpacing.roundToPx()

                        placeable.placeRelative(x = totalRowWidth, y = rowHeightWithSpacing)

                        placedUpToY += rowHeightWithSpacing
                        placedUpToRow++
                    }
                }
            }

        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun ChipGroupPreview() {
    ScitalysTheme {
        ChipGroup {
            listOf(Trait.ENCHI, Trait.SUPER_PASTEL, Trait.PIED, Trait.CHAMPAGNE).forEach {
                TraitChip(trait = it)
            }
        }
    }
}