package com.nullpointer.noursecompose.ui.screen.temperature

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.actions.ActionMeasureScreen
import com.nullpointer.noursecompose.actions.ActionMeasureScreen.*
import com.nullpointer.noursecompose.core.states.Resource
import com.nullpointer.noursecompose.core.utils.shareViewModel
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.presentation.MeasureViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.dialogs.DialogAddMeasure
import com.nullpointer.noursecompose.ui.navigation.HomeNavGraph
import com.nullpointer.noursecompose.ui.screen.empty.EmptyScreen
import com.nullpointer.noursecompose.ui.screen.measure.GraphAndTable
import com.nullpointer.noursecompose.ui.screen.measure.LoadingItemMeasure
import com.nullpointer.noursecompose.ui.share.ButtonToggleAddRemove
import com.nullpointer.noursecompose.ui.states.SelectedScreenState
import com.nullpointer.noursecompose.ui.states.rememberSelectedScreenState
import com.ramcosta.composedestinations.annotation.Destination

@HomeNavGraph
@Destination
@Composable
fun TempScreen(
    selectionViewModel: SelectionViewModel = shareViewModel(),
    measureViewModel: MeasureViewModel = shareViewModel(),
    measureScreenState: SelectedScreenState = rememberSelectedScreenState()
) {
    val listTempState by measureViewModel.listTemp.collectAsState()
    var isShowDialog by rememberSaveable { mutableStateOf(false) }

    BackHandler(selectionViewModel.isSelectedEnable) {
        selectionViewModel.clearSelection()
    }

    TempScreen(
        isShowDialog = isShowDialog,
        isSelectedEnable = selectionViewModel.isSelectedEnable,
        isScrollInProgress = measureScreenState.isScrollInProgress,
        lazyGridState = measureScreenState.lazyGridState,
        scaffoldState = measureScreenState.scaffoldState,
        listOxygen = listTempState,
        actionOxygen = { actionMeasureScreen: ActionMeasureScreen, value: Float?, simpleMeasure: SimpleMeasure? ->
            when (actionMeasureScreen) {
                SHOW_ADD_DIALOG -> isShowDialog = true
                HIDE_ADD_DIALOG -> isShowDialog = false
                ADD_NEW_MEASURE -> {
                    value?.let {
                        measureViewModel.addNewMeasure(
                            SimpleMeasure(
                                value,
                                MeasureType.TEMPERATURE
                            )
                        )
                        measureScreenState.scrollToFirst()
                    }
                }
                DELETER_LIST_MEASURE -> {
                    measureViewModel.deleterListMeasure(
                        selectionViewModel.getListSelectionAndClear()
                    )
                }
                CHANGE_MEASURE_SELECT -> {
                    simpleMeasure?.let { selectionViewModel.changeItemSelected(it) }
                }
            }
        }
    )
}

@Composable
private fun TempScreen(
    isShowDialog: Boolean,
    isSelectedEnable: Boolean,
    isScrollInProgress: Boolean,
    lazyGridState: LazyGridState,
    scaffoldState: ScaffoldState,
    listOxygen: Resource<List<SimpleMeasure>>,
    actionOxygen: (ActionMeasureScreen, Float?, SimpleMeasure?) -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            ButtonToggleAddRemove(
                isVisible = !isScrollInProgress,
                isSelectedEnable = isSelectedEnable,
                descriptionButtonAdd = stringResource(id = R.string.description_add_temp),
                actionAdd = { actionOxygen(SHOW_ADD_DIALOG, null, null) },
                descriptionButtonRemove = stringResource(id = R.string.description_remove_temp),
                actionRemove = { actionOxygen(DELETER_LIST_MEASURE, null, null) }
            )
        }
    ) { paddingValues ->
        ListTempState(
            listTemp = listOxygen,
            lazyGridState = lazyGridState,
            isSelectedEnable = isSelectedEnable,
            modifier = Modifier.padding(paddingValues),
            changeSelect = { actionOxygen(CHANGE_MEASURE_SELECT, null, it as SimpleMeasure) }
        )
    }
    if (isShowDialog) {
        DialogAddMeasure(
            nameMeasure = stringResource(id = R.string.name_oxygen),
            measureFullSuffix = stringResource(id = R.string.suffix_oxygen),
            actionHiddenDialog = { actionOxygen(HIDE_ADD_DIALOG, null, null) },
            actionAdd = { actionOxygen(ADD_NEW_MEASURE, it, null) })
    }
}


@Composable
private fun ListTempState(
    listTemp: Resource<List<SimpleMeasure>>,
    lazyGridState: LazyGridState,
    isSelectedEnable: Boolean,
    changeSelect: (ItemSelected) -> Unit,
    modifier: Modifier = Modifier
) {
    when (listTemp) {
        is Resource.Success -> {
            if (listTemp.data.isEmpty()) {
                EmptyScreen(
                    animation = R.raw.empty1,
                    textEmpty = stringResource(R.string.message_empty_temp),
                    modifier = modifier
                )
            } else {
                GraphAndTable(
                    modifier = modifier,
                    listMeasure = listTemp.data,
                    suffixMeasure = stringResource(id = R.string.suffix_temp),
                    nameMeasure = stringResource(id = R.string.name_temp),
                    minValue = MeasureType.TEMPERATURE.minValue,
                    maxValues = MeasureType.TEMPERATURE.maxValue,
                    isSelectedEnable = isSelectedEnable,
                    changeSelectState = changeSelect,
                    listState = lazyGridState,
                )
            }

        }
        else -> LoadingItemMeasure(modifier = modifier)
    }
}