package com.scitalys.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.scitalys.bp_traits.*
import com.scitalys.ui.theme.ScitalysTheme

@ExperimentalMaterialApi
@Composable
fun OddsSpecimenRow(
    specimen: Specimen,
    oddsMode: Int,
    incidence: Int,
    oddsOutOf: Int,
    onChipClick: (Morph) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Column {
            if (oddsMode and ODDS_MODE_FRACTION == ODDS_MODE_FRACTION) {
                Text(
                    text = stringResource(id = R.string.fraction)
                        .format(incidence, oddsOutOf),
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.width(34.dp)
                )
            }
            if (oddsMode and ODDS_MODE_PERCENTAGE == ODDS_MODE_PERCENTAGE) {
                Text(
                    text = stringResource(id = R.string.percentage)
                        .format(incidence * 100 / oddsOutOf),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.width(34.dp)
                )
            }
        }
        SpecimenRow(
            specimen = specimen,
            strokeWidth = 0.dp,
            onChipClick = onChipClick
        )

    }
}


@ExperimentalMaterialApi
@Composable
fun SpecimenRow(
    specimen: Specimen,
    strokeWidth: Dp = 1.dp,
    onChipClick: (morph: Morph) -> Unit
) {
    ChipGroup {
        specimen.morph.forEach { (lociPair, probability) ->
            TraitChip(
                morph = Morph.values().first { it.mutations == setOf(lociPair) },
                probability = probability,
                strokeWidth = strokeWidth,
                modifier = Modifier
                    .padding(end = 5.dp),
                onClick = onChipClick
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview(
    name = "OddsSpecimenRow ꞏ Day ꞏ Fraction",
    group = "OddsSpecimenRow"
)
@Composable
fun OddsSpecimenRowDayFraction() {
    ScitalysTheme {
        OddsSpecimenRow(
            specimen = Specimen(
                morphMap = mutableMapOf(
                    LociPair(Mutation.ENCHI) to 1f,
                    LociPair(Mutation.PASTEL) to 1f,
                    LociPair(Mutation.HET_PIED, Mutation.HET_PIED) to 1f
                )
            ),
            oddsMode = ODDS_MODE_FRACTION,
            incidence = 1,
            oddsOutOf = 4,
            onChipClick = { }
        )
    }
}

@ExperimentalMaterialApi
@Preview(
    name = "OddsSpecimenRow ꞏ Night ꞏ Both",
    group = "OddsSpecimenRow",
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun OddsSpecimenRowNightBoth() {
    ScitalysTheme {
        OddsSpecimenRow(
            specimen = Specimen(
                morphMap = mutableMapOf(
                    LociPair(Mutation.ENCHI) to 1f,
                    LociPair(Mutation.PASTEL) to 1f,
                    LociPair(Mutation.HET_PIED, Mutation.HET_PIED) to 1f
                )
            ),
            oddsMode = ODDS_MODE_FRACTION,
            incidence = 1,
            oddsOutOf = 4,
            onChipClick = { }
        )
    }
}


@ExperimentalMaterialApi
@Preview(
    name = "SpecimenRow ꞏ Day",
    group = "SpecimenRow"
)
@Composable
private fun SpecimenRowDay() {
    ScitalysTheme {
        SpecimenRow(
            specimen = Specimen(
                morphMap = mutableMapOf(
                    LociPair(Mutation.ENCHI) to 1f,
                    LociPair(Mutation.PASTEL) to 1f,
                    LociPair(Mutation.HET_PIED, Mutation.HET_PIED) to 1f
                )
            ),
            onChipClick = { }
        )
    }
}

@ExperimentalMaterialApi
@Preview(
    name = "SpecimenRow ꞏ Night",
    group = "SpecimenRow",
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
private fun SpecimenRowNight() {
    ScitalysTheme {
        SpecimenRow(
            specimen = Specimen(
                morphMap = mutableMapOf(
                    LociPair(Mutation.ENCHI) to 1f,
                    LociPair(Mutation.PASTEL) to 1f,
                    LociPair(Mutation.HET_PIED, Mutation.HET_PIED) to 1f
                )
            ),
            onChipClick = { }
        )
    }

}