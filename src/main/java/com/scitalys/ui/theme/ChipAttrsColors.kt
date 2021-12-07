package com.scitalys.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color


val lightChipsColors = ChipsColors(
    codominant = object: ChipAttrsColors {
        override val text = md_theme_light_onPrimary
        override val border = md_theme_light_primary
        override val background = md_theme_light_primaryContainer
    },
    coallelic = object: ChipAttrsColors {
        override val text = md_theme_light_onSecondary
        override val border = md_theme_light_secondary
        override val background = md_theme_light_secondaryContainer
    },
    recessiveHet = object: ChipAttrsColors {
        override val text = md_theme_light_primary
        override val border = md_theme_light_primaryContainer
        override val background = md_theme_light_onPrimary
    }
)
val darkChipColors = ChipsColors(
    codominant = object: ChipAttrsColors {
        override val text = md_theme_light_primary
        override val border = md_theme_light_primary
        override val background = Color(0xFF121212)
    },
    coallelic = object: ChipAttrsColors {
        override val text = md_theme_light_secondaryContainer
        override val border = md_theme_light_secondaryContainer
        override val background = Color(0xFF121212)
    },
    recessiveHet = object: ChipAttrsColors {
        override val text = md_theme_light_primaryContainer
        override val border = md_theme_light_primaryContainer
        override val background = Color(0xFF121212)
    }
)

@Stable
class ChipsColors(
    val codominant: ChipAttrsColors,
    val coallelic: ChipAttrsColors,
    val recessiveHet: ChipAttrsColors
)

interface ChipAttrsColors {
    val text: Color
    val border: Color
    val background: Color
}