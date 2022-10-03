package com.nullpointer.noursecompose.ui.screen.oxygen

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nullpointer.noursecompose.actions.ActionMeasureScreen.*
import com.nullpointer.noursecompose.core.utils.shareViewModel
import com.nullpointer.noursecompose.models.measure.MeasureType.OXYGEN
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
fun OxygenScreen(
    measureViewModel: MeasureViewModel = shareViewModel(),
    selectionViewModel: SelectionViewModel = shareViewModel(),
    measureScreenState: SelectedScreenState = rememberSelectedScreenState()
) {
    val listOxygenState by measureViewModel.listOxygen.collectAsState()

    BackHandler(selectionViewModel.isSelectedEnable, selectionViewModel::clearSelection)

    MeasureScreen(
        measureType = OXYGEN,
        measureSelected = null,
        listMeasure = listOxygenState,
        scaffoldState = measureScreenState.scaffoldState,
        lazyGridState = measureScreenState.lazyGridState,
        measureProperty = measureViewModel.measureInputProperty,
        isShowAddDialog = measureViewModel.isShowDialogAddMeasure,
        isSelectedEnable = selectionViewModel.isSelectedEnable,
        isScrollInProgress = measureScreenState.isScrollInProgress,
        actionMeasure = { action, measure ->
            when (action) {
                SHOW_ADD_DIALOG -> measureViewModel.showDialogInputMeasure(OXYGEN)
                HIDE_ADD_DIALOG -> measureViewModel.hiddeDialogInputMeasure()
                ADD_NEW_MEASURE -> {
                    measureViewModel.addNewMeasure(OXYGEN, measureScreenState::scrollToFirst)
                }
                DELETER_LIST_MEASURE -> {
                    measureViewModel.deleterListMeasure(
                        selectionViewModel.getListSelectionAndClear()
                    )
                }
                CHANGE_MEASURE_SELECT -> {
                    measure?.let { selectionViewModel.changeItemSelected(it) }
                }
            }
        },
    )
}
