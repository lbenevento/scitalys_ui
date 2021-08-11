package com.scitalys.android.ballscalculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.scitalys.ui.theme.*

@Composable
fun ScitalysTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}

private val LightColors = lightColors(
    primary = Green400,
    primaryVariant = Green900,
    onPrimary = Color.White,
    secondary = Purple300,
    secondaryVariant = Purple600,
    onSecondary = Color.White,
    onSurface = Green900,
    error = RedError
)

private val DarkColors = darkColors(
    primary = Purple400,
    primaryVariant = Purple200,
    onPrimary = Color.Black,
    secondary = Green400,
    onSecondary = Color.Black,
    onSurface = Purple400,
    error = RedErrorDark
)