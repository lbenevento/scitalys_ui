package com.scitalys.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scitalys.bp_traits.*
import com.scitalys.ui.theme.ScitalysTheme
import com.scitalys.ui.theme.*

@Composable
fun TraitChip(
    trait: Trait,
    modifier: Modifier = Modifier,
    probability: Float = 1f,
    strokeWidth: Dp = 1.dp,
    fontSize: TextUnit = 14.sp,
    textPadding: PaddingValues = PaddingValues(
        start = 6.dp,
        top = 1.dp,
        end = 6.dp,
        bottom = 1.dp
    ),
    onClick: ((trait: Trait) -> Unit)? = null
) {
    val colors = if (isSystemInDarkTheme()) {
        if (trait.isHetRecessive()) {
            darkChipColors.recessiveHet
        } else if (trait.isHomoCodominant() || trait.isHomoRecessive()) {
            darkChipColors.coallelic
        } else {
            darkChipColors.codominant
        }
    } else {
        if (trait.isHetRecessive()) {
            lightChipsColors.recessiveHet
        } else if (trait.isHomoCodominant() || trait.isHomoRecessive()) {
            lightChipsColors.coallelic
        } else {
            lightChipsColors.codominant
        }
    }


    val traitChipContent: @Composable () -> Unit = {
        val text = if (probability != 1f) {
            stringResource(id = R.string.hetProbability)
                .format((probability * 100).toInt(), trait.formattedString)
        } else {
            trait.formattedString
        }
        Text(
            text = text,
            fontSize = fontSize,
            style = MaterialTheme.typography.labelSmall,
            color = colors.text,
            modifier = Modifier.padding(textPadding)
        )
    }

    if (onClick != null) {
        Surface(
            modifier = modifier,
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(50),
            color = colors.background,
            border = BorderStroke(
                width = strokeWidth,
                color = colors.border
            ),
            onClick = { onClick(trait) }
        ) { traitChipContent() }
    } else {
        Surface(
            modifier = modifier,
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(50),
            color = colors.background,
            border = BorderStroke(
                width = strokeWidth,
                color = colors.border
            )
        ) { traitChipContent() }
    }
}

@Composable
fun TraitChip(
    morph: Morph,
    modifier: Modifier = Modifier,
    probability: Float = 1f,
    strokeWidth: Dp = 1.dp,
    fontSize: TextUnit = 14.sp,
    textPadding: PaddingValues = PaddingValues(
        start = 6.dp,
        top = 1.dp,
        end = 6.dp,
        bottom = 1.dp
    ),
    onClick: ((morph: Morph) -> Unit)? = null
) {
    val colors = if (isSystemInDarkTheme()) {
        if (morph.isHetRecessive()) {
            darkChipColors.recessiveHet
        } else if (morph.isHomoCodominant() || morph.isHomoRecessive()) {
            darkChipColors.coallelic
        } else {
            darkChipColors.codominant
        }
    } else {
        if (morph.isHetRecessive()) {
            lightChipsColors.recessiveHet
        } else if (morph.isHomoCodominant() || morph.isHomoRecessive()) {
            lightChipsColors.coallelic
        } else {
            lightChipsColors.codominant
        }
    }


    val traitChipContent: @Composable () -> Unit = {
        val text = if (probability != 1f) {
            stringResource(id = R.string.hetProbability)
                .format((probability * 100).toInt(), morph.formattedString)
        } else {
            morph.formattedString
        }
        Text(
            text = text,
            fontSize = fontSize,
            style = MaterialTheme.typography.labelSmall,
            color = colors.text,
            modifier = Modifier.padding(textPadding)
        )
    }

    if (onClick != null) {
        Surface(
            modifier = modifier,
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(50),
            color = colors.background,
            border = BorderStroke(
                width = strokeWidth,
                color = colors.border
            ),
            onClick = { onClick(morph) }
        ) { traitChipContent() }
    } else {
        Surface(
            modifier = modifier,
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(50),
            color = colors.background,
            border = BorderStroke(
                width = strokeWidth,
                color = colors.border
            )
        ) { traitChipContent() }
    }
}

@Preview(name = "TraitChip ꞏ Codominant ꞏ Day")
@Composable
fun TraitChipCodominantDayPreview() {
    ScitalysTheme {
        TraitChip(trait = Trait.PASTEL)
    }
}

@Preview(name = "TraitChip ꞏ Het ꞏ Day")
@Composable
fun TraitChipHetDayPreview() {
    ScitalysTheme {
        TraitChip(trait = Trait.HET_PIED)
    }
}

@Preview(name = "TraitChip ꞏ Coallelic ꞏ Day")
@Composable
fun TraitChipCoallelicDayPreview() {
    ScitalysTheme {
        TraitChip(trait = Trait.SUPER_PASTEL)
    }
}

@Preview(name = "TraitChip ꞏ Probable Het ꞏ Day")
@Composable
fun TraitChipProbableHetDayPreview() {
    ScitalysTheme {
        TraitChip(trait = Trait.HET_GHOST, probability = .5f)
    }
}

@Preview(name = "TraitChip ꞏ Codominant ꞏ Night", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TraitChipCodominantNightPreview() {
    ScitalysTheme {
        TraitChip(trait = Trait.PASTEL)
    }
}

@Preview(name = "TraitChip ꞏ Het ꞏ Night", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TraitChipHetNightPreview() {
    ScitalysTheme {
        TraitChip(trait = Trait.HET_PIED)
    }
}

@Preview(name = "TraitChip ꞏ Coallelic ꞏ Night", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TraitChipCoallelicNightPreview() {
    ScitalysTheme {
        TraitChip(trait = Trait.SUPER_PASTEL)
    }
}

@Preview(name = "TraitChip ꞏ Probable Het ꞏ Night", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TraitChipProbableHetNightPreview() {
    ScitalysTheme {
        TraitChip(trait = Trait.HET_GHOST, probability = .5f)
    }
}