package com.nullpointer.noursecompose.ui.screen.oxygen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
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
import com.nullpointer.noursecompose.ui.share.ButtonToggleAddRemove
import com.nullpointer.noursecompose.ui.states.MeasureScreenState
import com.nullpointer.noursecompose.ui.states.rememberMeasureScreenState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@HomeNavGraph
@Destination
@Composable
fun OxygenScreen(
    selectionViewModel: SelectionViewModel,
    measureViewModel: MeasureViewModel = shareViewModel(),
    measureScreenState: MeasureScreenState = rememberMeasureScreenState()
) {
    val listOxygenState by measureViewModel.listOxygen.collectAsState()
    val (isShowDialog, changeVisibleDialog) = rememberSaveable { mutableStateOf(false) }

    BackHandler(selectionViewModel.isSelectedEnable, selectionViewModel::clearSelection)

    Scaffold(
        scaffoldState = measureScreenState.scaffoldState,
        floatingActionButton = {
            ButtonToggleAddRemove(
                isVisible = !measureScreenState.isScrollInProgress,
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
        ListOxygenState(
            listOxygen = listOxygenState,
            lazyGridState = measureScreenState.lazyGridState,
            isSelectedEnable = selectionViewModel.isSelectedEnable,
            changeSelect = selectionViewModel::changeItemSelected,
            modifier = Modifier.padding(it)
        )
    }
    if (isShowDialog) {
        DialogAddMeasure(
            nameMeasure = stringResource(id = R.string.name_oxygen),
            measureFullSuffix = stringResource(id = R.string.suffix_oxygen),
            actionHiddenDialog = { changeVisibleDialog(false) },
            actionAdd = {
                measureViewModel.addNewMeasure(SimpleMeasure(it, MeasureType.OXYGEN))
                measureScreenState.scrollToFirst()
            })
    }
}

@Composable
private fun ListOxygenState(
    listOxygen: Resource<List<SimpleMeasure>>,
    lazyGridState: LazyGridState,
    isSelectedEnable: Boolean,
    changeSelect: (ItemSelected) -> Unit,
    modifier: Modifier = Modifier
) {
    when (listOxygen) {
        is Resource.Success -> {
            if (listOxygen.data.isEmpty()) {
                EmptyScreen(
                    modifier = modifier,
                    animation = R.raw.empty2,
                    textEmpty = stringResource(R.string.message_empty_measure_oxygen)
                )
            } else {
                GraphAndTable(
                    modifier = modifier,
                    listMeasure = listOxygen.data,
                    suffixMeasure = stringResource(id = R.string.suffix_oxygen),
                    nameMeasure = stringResource(id = R.string.name_oxygen),
                    minValue = MeasureType.OXYGEN.minValue,
                    maxValues = MeasureType.OXYGEN.maxValue,
                    isSelectedEnable = isSelectedEnable,
                    changeSelectState = changeSelect,
                    listState = lazyGridState,
                )
            }
        }
        else -> {}
    }
}