package com.nullpointer.noursecompose.ui.screen.oxygen.components

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.ui.screen.empty.EmptyScreen
import com.nullpointer.noursecompose.ui.screen.measure.GraphAndTable

@Composable
fun ListSuccessOxygen(
    modifier: Modifier = Modifier,
    isSelectedEnable: Boolean,
    changeSelectState: (ItemSelected) -> Unit,
    listState: LazyGridState,
    listOxygen: List<SimpleMeasure>
) {
    GraphAndTable(
        modifier = modifier,
        listState = listState,
        listMeasure = listOxygen,
        isSelectedEnable = isSelectedEnable,
        changeSelectState = changeSelectState,
        minValue = MeasureType.OXYGEN.minValue,
        maxValues = MeasureType.OXYGEN.maxValue,
        nameMeasure = stringResource(id = R.string.name_oxygen),
        suffixMeasure = stringResource(id = R.string.suffix_oxygen),
    )
}

@Composable
fun ListEmptyOxygen(
    modifier: Modifier = Modifier
) {
    EmptyScreen(
        modifier = modifier,
        animation = R.raw.empty2,
        textEmpty = stringResource(R.string.message_empty_measure_oxygen)
    )
}