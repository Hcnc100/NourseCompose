package com.nullpointer.noursecompose.ui.screen.home.temperature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.presentation.MeasureViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.dialogs.DialogAddMeasure
import com.nullpointer.noursecompose.ui.screen.empty.EmptyScreen
import com.nullpointer.noursecompose.ui.screen.measure.GraphAndTable
import com.nullpointer.noursecompose.ui.share.backHandler.BackHandler
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun TempScreen(
    measureViewModel: MeasureViewModel = hiltViewModel(),
    selectionViewModel: SelectionViewModel,
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyGridState()
    val listTempState = measureViewModel.listTemp.collectAsState().value
    val (isShowDialog, changeVisibleDialog) = rememberSaveable { mutableStateOf(false) }

    when {
        listTempState == null -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
        listTempState.isEmpty() -> EmptyScreen(animation = R.raw.empty1,
            textEmpty = "No hay medidas de temperatura")
        listTempState.isNotEmpty() -> GraphAndTable(listMeasure = listTempState,
            descriptionAddNewMeasure = stringResource(id = R.string.description_add_temp),
            suffixMeasure = stringResource(id = R.string.suffix_temp),
            nameMeasure = stringResource(id = R.string.name_temp),
            minValue = SimpleMeasure.minValueTemp.toFloat(),
            maxValues = SimpleMeasure.maxValueTemp.toFloat(),
            actionAdd = { changeVisibleDialog(true) },
            isSelectedEnable = selectionViewModel.isSelectedEnable,
            changeSelectState = selectionViewModel::changeItemSelected,
            listState = listState,
            actionDeleter = {
                measureViewModel.deleterListMeasure(
                    selectionViewModel.getListMeasureAndClear()
                )
            },
            descriptionDeleterMeasure =stringResource(id = R.string.description_remove_temp)
        )
    }


    BackHandler(selectionViewModel.isSelectedEnable) {
        selectionViewModel.clearSelection()
    }

    if (isShowDialog) {
        DialogAddMeasure(
            nameMeasure = stringResource(id = R.string.name_temp),
            measureFullSuffix = stringResource(id = R.string.suffix_temp),
            actionHiddenDialog = { changeVisibleDialog(false) },
            actionAdd = {
                measureViewModel.addNewMeasure(SimpleMeasure(it, MeasureType.TEMPERATURE))
                scope.launch { listState.animateScrollToItem(0) }
            })
    }
}