package com.nullpointer.noursecompose.ui.screen.measure

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nullpointer.noursecompose.actions.ActionMeasureScreen
import com.nullpointer.noursecompose.actions.ActionMeasureScreen.*
import com.nullpointer.noursecompose.core.delegates.PropertySavableString
import com.nullpointer.noursecompose.core.states.Resource
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.ui.dialogs.DialogAddMeasure
import com.nullpointer.noursecompose.ui.screen.empty.EmptyScreen
import com.nullpointer.noursecompose.ui.share.ButtonToggleAddRemove

@Composable
fun MeasureScreen(
    isShowAddDialog: Boolean,
    measureType: MeasureType,
    isSelectedEnable: Boolean,
    scaffoldState: ScaffoldState,
    isScrollInProgress: Boolean,
    lazyGridState: LazyGridState,
    measureSelected: SimpleMeasure?,
    listMeasure: Resource<List<SimpleMeasure>>,
    actionMeasure: (ActionMeasureScreen, SimpleMeasure?) -> Unit,
    measureProperty: PropertySavableString?
) {

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            ButtonToggleAddRemove(
                isVisible = !isScrollInProgress,
                isSelectedEnable = isSelectedEnable,
                actionAdd = { actionMeasure(SHOW_ADD_DIALOG, null) },
                actionRemove = { actionMeasure(DELETER_LIST_MEASURE, null) },
                descriptionButtonAdd = stringResource(id = measureType.descriptionAdd),
                descriptionButtonRemove = stringResource(id = measureType.descriptionRemove)
            )
        }
    ) { paddingValues ->
        ListMeasureState(
            listTemp = listMeasure,
            measureType = measureType,
            lazyGridState = lazyGridState,
            isSelectedEnable = isSelectedEnable,
            modifier = Modifier.padding(paddingValues),
            changeSelect = { actionMeasure(CHANGE_MEASURE_SELECT, it as SimpleMeasure) }
        )
    }
    if (isShowAddDialog) {
        DialogAddMeasure(
            measureFullSuffix = stringResource(id = measureType.suffix),
            actionHiddenDialog = { actionMeasure(HIDE_ADD_DIALOG, null) },
            actionAdd = { actionMeasure(ADD_NEW_MEASURE, null) },
            measureProperty = measureProperty!!
        )
    }

    measureSelected?.let {

    }
}


@Composable
private fun ListMeasureState(
    measureType: MeasureType,
    isSelectedEnable: Boolean,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    changeSelect: (ItemSelected) -> Unit,
    listTemp: Resource<List<SimpleMeasure>>
) {
    when (listTemp) {
        is Resource.Success -> {
            if (listTemp.data.isEmpty()) {
                EmptyScreen(
                    modifier = modifier,
                    animation = measureType.animationEmpty,
                    textEmpty = stringResource(measureType.textEmpty)
                )
            } else {
                GraphAndTable(
                    modifier = modifier,
                    listMeasure = listTemp.data,
                    suffixMeasure = stringResource(measureType.suffix),
                    nameMeasure = stringResource(measureType.nameMeasure),
                    minValue = measureType.minValue,
                    maxValues = measureType.maxValue,
                    isSelectedEnable = isSelectedEnable,
                    changeSelectState = changeSelect,
                    listState = lazyGridState,
                )
            }

        }
        else -> LoadingItemMeasure(modifier = modifier)
    }
}