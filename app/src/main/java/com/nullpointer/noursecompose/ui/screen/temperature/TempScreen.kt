package com.nullpointer.noursecompose.ui.screen.temperature

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nullpointer.noursecompose.actions.ActionMeasureScreen
import com.nullpointer.noursecompose.actions.ActionMeasureScreen.*
import com.nullpointer.noursecompose.core.utils.shareViewModel
import com.nullpointer.noursecompose.models.measure.MeasureType.TEMPERATURE
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.presentation.MeasureViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.navigation.HomeNavGraph
import com.nullpointer.noursecompose.ui.screen.measure.MeasureScreen
import com.nullpointer.noursecompose.ui.states.SelectedScreenState
import com.nullpointer.noursecompose.ui.states.rememberSelectedScreenState
import com.ramcosta.composedestinations.annotation.Destination

@HomeNavGraph
@Destination
@Composable
fun TempScreen(
    measureViewModel: MeasureViewModel = shareViewModel(),
    selectionViewModel: SelectionViewModel = shareViewModel(),
    measureScreenState: SelectedScreenState = rememberSelectedScreenState()
) {
    val listTempState by measureViewModel.listTemp.collectAsState()

    BackHandler(selectionViewModel.isSelectedEnable, selectionViewModel::clearSelection)

    MeasureScreen(
        measureSelected = null,
        measureType = TEMPERATURE,
        listMeasure = listTempState,
        scaffoldState = measureScreenState.scaffoldState,
        lazyGridState = measureScreenState.lazyGridState,
        measureProperty = measureViewModel.measureInputProperty,
        isShowAddDialog = measureViewModel.isShowDialogAddMeasure,
        isSelectedEnable = selectionViewModel.isSelectedEnable,
        isScrollInProgress = measureScreenState.isScrollInProgress,
        actionMeasure = { actionMeasureScreen: ActionMeasureScreen, simpleMeasure: SimpleMeasure? ->
            when (actionMeasureScreen) {
                SHOW_ADD_DIALOG -> measureViewModel.showDialogInputMeasure(TEMPERATURE)
                HIDE_ADD_DIALOG -> measureViewModel.hiddeDialogInputMeasure()
                ADD_NEW_MEASURE -> {
                    measureViewModel.addNewMeasure(
                        TEMPERATURE,
                        callbackSuccess = measureScreenState::scrollToFirst
                    )
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
        },
    )
}

