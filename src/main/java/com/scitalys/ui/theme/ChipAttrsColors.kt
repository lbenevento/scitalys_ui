package com.scitalys.ui.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color


val lightChipsColors = ChipsColors(
    codominant = object: ChipAttrsColors {
        override val text = Green050
        override val border = Green500
        override val background = Green200
    },
    coallelic = object: ChipAttrsColors {
        override val text = Purple050
        override val border = Purple500
        override val background = Purple300
    },
    recessiveHet = object: ChipAttrsColors {
        override val text = Green500
        override val border = Green200
        override val background = Green050
    }
)
val darkChipColors = ChipsColors(
    codominant = object: ChipAttrsColors {
        override val text = Green500
        override val border = Green500
        override val background = Color(0xFF121212)
    },
    coallelic = object: ChipAttrsColors {
        override val text = Purple200
        override val border = Purple200
        override val background = Color(0xFF121212)
    },
    recessiveHet = object: ChipAttrsColors {
        override val text = Green200
        override val border = Green200
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