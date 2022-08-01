package com.nullpointer.noursecompose.ui.screen.temperature

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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

@HomeNavGraph
@Destination
@Composable
fun TempScreen(
    selectionViewModel: SelectionViewModel,
    measureViewModel: MeasureViewModel = shareViewModel(),
    measureScreenState: MeasureScreenState = rememberMeasureScreenState()
) {
    val listTempState by measureViewModel.listTemp.collectAsState()
    val (isShowDialog, changeVisibleDialog) = rememberSaveable { mutableStateOf(false) }

    BackHandler(selectionViewModel.isSelectedEnable) {
        selectionViewModel.clearSelection()
    }
    Scaffold(
        scaffoldState = measureScreenState.scaffoldState,
        floatingActionButton = {
            ButtonToggleAddRemove(
                isVisible = !measureScreenState.isScrollInProgress,
                isSelectedEnable = selectionViewModel.isSelectedEnable,
                descriptionButtonAdd = stringResource(id = R.string.description_add_temp),
                actionAdd = { changeVisibleDialog(true) },
                descriptionButtonRemove = stringResource(id = R.string.description_remove_temp),
                actionRemove = {
                    measureViewModel.deleterListMeasure(
                        selectionViewModel.getListSelectionAndClear()
                    )
                }
            )
        }
    ) {
        ListStateTemperature(
            listTempState = listTempState,
            lazyGridState = measureScreenState.lazyGridState,
            isSelectedEnable = selectionViewModel.isSelectedEnable,
            changeSelect = selectionViewModel::changeItemSelected,
            modifier = Modifier.padding(it)
        )
    }

    if (isShowDialog) {
        DialogAddMeasure(
            nameMeasure = stringResource(id = R.string.name_temp),
            measureFullSuffix = stringResource(id = R.string.suffix_temp),
            actionHiddenDialog = { changeVisibleDialog(false) },
            actionAdd = {
                measureViewModel.addNewMeasure(SimpleMeasure(it, MeasureType.TEMPERATURE))
                measureScreenState.scrollToFirst()
            })
    }
}

@Composable
private fun ListStateTemperature(
    listTempState: Resource<List<SimpleMeasure>>,
    lazyGridState: LazyGridState,
    isSelectedEnable: Boolean,
    changeSelect: (ItemSelected) -> Unit,
    modifier: Modifier = Modifier
) {
    when (listTempState) {
        is Resource.Success -> {
            if (listTempState.data.isEmpty()) {
                EmptyScreen(
                    animation = R.raw.empty1,
                    textEmpty = stringResource(R.string.message_empty_temp),
                    modifier = modifier
                )
            } else {
                GraphAndTable(
                    modifier = modifier,
                    listMeasure = listTempState.data,
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
        else -> {}
    }
}