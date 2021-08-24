package com.scitalys.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Filter
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.scitalys.ui.theme.ScitalysTheme

@ExperimentalAnimationApi
@Composable
fun FiltersFab() {
    var isExpanded by remember { mutableStateOf(false) }


    val transition = updateTransition(targetState = isExpanded, label = "Main")
    val corners by transition.animateInt(label = "Corners") {
        if (it) 0 else 50
    }
    val offset by transition.animateIntOffset(label = "Offset") {
        if (it) IntOffset(-100, -100) else IntOffset(0, 0)
    }
    FiltersFab(
        shape = RoundedCornerShape(corners),
        modifier = Modifier.offset { offset }
    ) {
        Column {
            if (isExpanded) {
                Filters()
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = if (isExpanded) Modifier
                    .animateContentSize()
                    .fillMaxWidth()
                else Modifier
                    .animateContentSize()
            ) {
                AnimatedVisibility(visible = isExpanded) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Rounded.Close),
                        contentDescription = null,
                    )
                }
                Icon(
                    painter = rememberVectorPainter(image = Icons.Rounded.FilterAlt),
                    contentDescription = null,
                    modifier = Modifier.clickable { isExpanded = !isExpanded }
                )
            }
        }
    }
}

@Composable
fun Filters() {
    var state by remember { mutableStateOf(0) }
    val titles = listOf("Filters", "Sorted By")
    Column {
        TabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title) },
                    selected = state == index,
                    onClick = { state = index }
                )
            }
        }
        when (state) {
            0 -> FiltersTabContent()
            1 -> SortedByTabContent()
        }
    }
}

@Composable
fun FiltersTabContent() {
    Text(text = "Filters")
}

@Composable
fun SortedByTabContent() {
    Text(text = "Sorted by")
}

@Composable
fun FiltersFab(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
    backgroundColor: Color = MaterialTheme.colors.secondary,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation.elevation(interactionSource).value
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(MaterialTheme.typography.button) {
                Box(
                    modifier = Modifier
                        .defaultMinSize(minWidth = FabSize, minHeight = FabSize),
                    contentAlignment = Alignment.Center
                ) { content() }
            }
        }
    }
}

private val FabSize = 56.dp
private val ExtendedFabSize = 48.dp
private val ExtendedFabIconPadding = 12.dp
private val ExtendedFabTextPadding = 20.dp

@ExperimentalAnimationApi
@Preview
@Composable
fun FiltersFabPreview() {
    ScitalysTheme {
        FiltersFab()
    }
}

@Preview
@Composable
fun FiltersPreview() {
    ScitalysTheme {
        Filters()
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun FiltersFabInsideScaffoldPreview() {
    ScitalysTheme {
        Scaffold(
            floatingActionButton = { FiltersFab() }
        ) {

        }
    }
}