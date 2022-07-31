package com.nullpointer.noursecompose.ui.screen.home.oxygen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.presentation.MeasureViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.dialogs.DialogAddMeasure
import com.nullpointer.noursecompose.ui.navigation.HomeNavGraph
import com.nullpointer.noursecompose.ui.screen.empty.EmptyScreen
import com.nullpointer.noursecompose.ui.screen.measure.GraphAndTable
import com.nullpointer.noursecompose.ui.share.ButtonToggleAddRemove
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@HomeNavGraph
@Destination
@Composable
fun OxygenScreen(
    measureViewModel: MeasureViewModel = hiltViewModel(),
    selectionViewModel: SelectionViewModel,
) {
    val listState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    val listOxygenState = measureViewModel.listOxygen.collectAsState().value
    val (isShowDialog, changeVisibleDialog) = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ButtonToggleAddRemove(
                isVisible = !listState.isScrollInProgress,
                isSelectedEnable = selectionViewModel.isSelectedEnable,
                descriptionButtonAdd = stringResource(id = R.string.description_add_oxygen),
                actionAdd = { changeVisibleDialog(true) },
                descriptionButtonRemove = stringResource(id = R.string.description_remove_oxygen),
                actionRemove = {
                    measureViewModel.deleterListMeasure(
                        selectionViewModel.getListSelectionAndClear()
                    )
                }
            )
        }
    ) {
        when {
            listOxygenState == null -> Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            listOxygenState.isEmpty() -> EmptyScreen(animation = R.raw.empty2,
                textEmpty = stringResource(R.string.message_empty_measure_oxygen))

            listOxygenState.isNotEmpty() -> GraphAndTable(
                listMeasure = listOxygenState,
                suffixMeasure = stringResource(id = R.string.suffix_oxygen),
                nameMeasure = stringResource(id = R.string.name_oxygen),
                minValue = MeasureType.TEMPERATURE.minValue,
                maxValues = MeasureType.TEMPERATURE.maxValue,
                isSelectedEnable = selectionViewModel.isSelectedEnable,
                changeSelectState = selectionViewModel::changeItemSelected,
                listState = listState,
            )
        }

    }




    BackHandler(selectionViewModel.isSelectedEnable) {
        selectionViewModel.clearSelection()
    }

    if (isShowDialog) {
        DialogAddMeasure(
            nameMeasure = stringResource(id = R.string.name_oxygen),
            measureFullSuffix = stringResource(id = R.string.suffix_oxygen),
            actionHiddenDialog = { changeVisibleDialog(false) },
            actionAdd = {
                measureViewModel.addNewMeasure(SimpleMeasure(it, MeasureType.OXYGEN))
                scope.launch { listState.animateScrollToItem(0) }
            })
    }
}