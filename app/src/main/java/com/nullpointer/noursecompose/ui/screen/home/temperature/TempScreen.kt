package com.nullpointer.noursecompose.ui.screen.home.temperature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.presentation.MeasureViewModel
import com.nullpointer.noursecompose.ui.dialogs.DialogAddMeasure
import com.nullpointer.noursecompose.ui.screen.measure.GraphAndTable
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun TempScreen(
    measureViewModel: MeasureViewModel = hiltViewModel(),
) {
    val listTempState = measureViewModel.listTemp.collectAsState()

    val (isShowDialog, changeVisibleDialog) = rememberSaveable { mutableStateOf(false) }
    GraphAndTable(listMeasure = listTempState.value,
        descriptionAddNewMeasure = stringResource(id = R.string.description_add_temp),
        suffixMeasure = stringResource(id = R.string.suffix_temp),
        nameMeasure = stringResource(id = R.string.name_temp),
        minValue = SimpleMeasure.minValueTemp.toFloat(),
        maxValues = SimpleMeasure.maxValueTemp.toFloat(),
        actionAdd = {
            measureViewModel.addNewMeasure(SimpleMeasure.createFake(1, MeasureType.TEMPERATURE)
                .first())
            // changeVisibleDialog(true)
        }
    )

    if (isShowDialog) {
        DialogAddMeasure(
            nameMeasure = stringResource(id = R.string.name_temp),
            measureFullSuffix = stringResource(id = R.string.suffix_temp),
            actionHiddenDialog = { changeVisibleDialog(false) },
            actionAdd = {})
    }
}