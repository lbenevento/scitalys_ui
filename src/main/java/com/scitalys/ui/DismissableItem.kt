package com.scitalys.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.scitalys.ui.utils.blendColors

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun DismissableItem(
    dismissState: DismissState,
    dismissDirections: Set<DismissDirection> = setOf(DismissDirection.EndToStart),
    content: @Composable () -> Unit
) {
    val swipeProgress = dismissState.progress
    val progress = if (swipeProgress.to == DismissValue.DismissedToStart) swipeProgress.fraction else 0f
    val targetColor = MaterialTheme.colorScheme.error
    val backgroundAnim = remember {
        Animatable(initialValue = Color.White)
    }
    LaunchedEffect(key1 = progress) {
        backgroundAnim.snapTo(
            if (progress < .3) {
                blendColors(Color.White, Color.LightGray, progress * (1f / .3f))
            } else {
                blendColors(Color.LightGray, targetColor, (progress - .3f) * (1f / .7f))
            }
        )
    }
    SwipeToDismiss(
        state = dismissState,
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            //TODO("Generalize it to work with both directions or just one of them")
//            val color by animateColorAsState(
//                when (dismissState.targetValue) {
//                    DismissValue.Default -> Color.LightGray
//                    DismissValue.DismissedToEnd -> Color.Green
//                    DismissValue.DismissedToStart -> MaterialTheme.colors.error
//                }
//            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Done
                DismissDirection.EndToStart -> Icons.Default.Delete
            }
            val iconColor = when (dismissState.targetValue) {
                DismissValue.Default -> Color.Gray
                DismissValue.DismissedToEnd -> Color.Blue
                DismissValue.DismissedToStart -> Color.LightGray
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1.25f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(backgroundAnim.value)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = "Localized description",
                    modifier = Modifier.scale(scale),
                    tint = iconColor
                )
            }
        },
        directions = dismissDirections
    ) {
        AnimatedVisibility(visible = !dismissState.isDismissed(DismissDirection.EndToStart)) {
            content()
        }
    }
}