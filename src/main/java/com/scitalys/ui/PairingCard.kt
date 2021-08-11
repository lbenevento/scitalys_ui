package com.scitalys.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.scitalys.ui.theme.ScitalysTheme
import com.scitalys.bp_traits.Pairing
import com.scitalys.bp_traits.Specimen
import com.scitalys.bp_traits.Trait
import com.scitalys.bp_traits.samples.pairing1

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun PairingCard(
    pairing: Pairing,
    modifier: Modifier = Modifier,
    oddsMode: Int = ODDS_MODE_FRACTION,
    pairingCardState: PairingCardState,
    onExpandClick: (state: PairingCardState) -> Unit,
    elevation: Dp = 4.dp,
    borderWidth: Dp = 1.dp,
    onChipClick: (trait: Trait) -> Unit
) {
    val transition = updateTransition(
        targetState = pairingCardState,
        label = "Pairing Card Transition"
    )
    val cardPadding by transition.animateDp(
        label = "Padding Animation",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        }
    ) { state ->
        when (state) {
            PairingCardState.Collapsed -> 12.dp
            PairingCardState.Expanded -> 2.dp
        }
    }
    val cardElevation by transition.animateDp(label = "Elevation Animation") { state ->
        when (state) {
            PairingCardState.Collapsed -> 0.dp
            PairingCardState.Expanded -> elevation
        }
    }
    val strokeWidth by transition.animateDp(label = "Stroke Width Animation") { state ->
        when (state) {
            PairingCardState.Collapsed -> 0.dp
            PairingCardState.Expanded -> borderWidth
        }
    }
    val chevronRotation by transition.animateFloat(
        label = "Chevron Animation",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        }
    ) { state ->
        when (state) {
            PairingCardState.Collapsed -> 0f
            PairingCardState.Expanded -> 90f
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        border = if (strokeWidth == 0.dp) null
        else BorderStroke(strokeWidth, MaterialTheme.colors.primaryVariant),
        modifier = modifier
            .padding(horizontal = cardPadding.coerceAtLeast(0.dp))
            .shadow(
                elevation = cardElevation,
                shape = RoundedCornerShape(20.dp),
                clip = cardElevation == 0.dp
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable { onExpandClick(pairingCardState) }
            .animateContentSize(
                animationSpec = spring()
            )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Header(
                male = pairing.male,
                female = pairing.female,
                chevronRotation = chevronRotation,
                onChipClick = onChipClick
            )
            if (pairingCardState == PairingCardState.Expanded) {
                Body(
                    offspring = pairing.offspringMap,
                    oddsOutOf = pairing.totalPossibilities,
                    oddsMode = oddsMode,
                    onChipClick = onChipClick
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@ExperimentalMaterialApi
@Composable
fun Body(
    offspring: Map<Specimen, Int>,
    oddsOutOf: Int,
    oddsMode: Int,
    onChipClick: (trait: Trait) -> Unit
) {
    Column {
        Spacer(Modifier.height(15.dp))

        val lastPosition = offspring.size - 1
        var currentPosition = 0

        offspring.forEach {
            val (specimen, incidence) = it

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
            // Add spacer only if it is not the last specimen in the list.
            if (currentPosition != lastPosition) {
                Spacer(Modifier.height(10.dp))
            }
            currentPosition++
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun Header(
    male: Specimen,
    female: Specimen,
    chevronRotation: Float,
    onChipClick: (trait: Trait) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = stringResource(R.string.pairing_card_expand_arrow_description),
            tint = MaterialTheme.colors.primaryVariant,
            modifier = Modifier
                .padding(end = 10.dp)
                .rotate(chevronRotation)
        )
        Column {
            SpecimenRow(
                specimen = male,
                onChipClick = onChipClick
            )
            Spacer(Modifier.height(10.dp))
            SpecimenRow(
                specimen = female,
                onChipClick = onChipClick
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun SpecimenRow(
    specimen: Specimen,
    strokeWidth: Dp = 1.dp,
    onChipClick: (trait: Trait) -> Unit
) {
    ChipGroup {
        specimen.traits.forEach { (trait, probability) ->
            TraitChip(
                trait = trait,
                probability = probability,
                strokeWidth = strokeWidth,
                modifier = Modifier
                    .padding(end = 5.dp),
                onClick = onChipClick
            )
        }
    }
}

enum class PairingCardState {
    Collapsed,
    Expanded
}

const val ODDS_MODE_FRACTION = 0x01
const val ODDS_MODE_PERCENTAGE = 0x02
const val ODDS_MODE_BOTH = 0x03

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview(name = "Collapsed ꞏ Day")
@Composable
fun PairingCardCollapsedDayPreview() {

    ScitalysTheme {
        var state by remember {
            mutableStateOf(PairingCardState.Collapsed)
        }
        PairingCard(
            pairing1,
            pairingCardState = state,
            onExpandClick = {
                state = when (it) {
                    PairingCardState.Expanded -> PairingCardState.Collapsed
                    PairingCardState.Collapsed -> PairingCardState.Expanded
                }
            },
            onChipClick = {},
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview(name = "Expanded ꞏ Night ꞏ Fraction", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PairingCardExpandedNightFractionPreview() {

    ScitalysTheme {
        var state by remember {
            mutableStateOf(PairingCardState.Expanded)
        }
        PairingCard(
            pairing1,
            pairingCardState = state,
            onExpandClick = {
                state = when (it) {
                    PairingCardState.Expanded -> PairingCardState.Collapsed
                    PairingCardState.Collapsed -> PairingCardState.Expanded
                }
            },
            onChipClick = {},
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview(name = "Expanded ꞏ Night ꞏ Both", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PairingCardExpandedDayBothPreview() {

    ScitalysTheme {
        var state by remember {
            mutableStateOf(PairingCardState.Expanded)
        }
        PairingCard(
            pairing1,
            oddsMode = ODDS_MODE_BOTH,
            pairingCardState = state,
            onExpandClick = {
                state = when (it) {
                    PairingCardState.Expanded -> PairingCardState.Collapsed
                    PairingCardState.Collapsed -> PairingCardState.Expanded
                }
            },
            onChipClick = {},
        )
    }
}