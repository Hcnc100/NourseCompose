package com.nullpointer.noursecompose.ui.screen.home.oxygen

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
fun OxygenScreen(
    measureViewModel: MeasureViewModel = hiltViewModel(),
) {
    val listOxygenState = measureViewModel.listOxygen.collectAsState()
    val (isShowDialog, changeVisibleDialog) = rememberSaveable { mutableStateOf(false) }
    GraphAndTable(listMeasure = listOxygenState.value,
        descriptionAddNewMeasure = stringResource(id = R.string.description_add_oxygen),
        suffixMeasure = stringResource(id = R.string.suffix_oxygen),
        nameMeasure = stringResource(id = R.string.name_oxygen),
        minValue = SimpleMeasure.minValueOxygen.toFloat(),
        maxValues = SimpleMeasure.maxValueOxygen.toFloat(),
        actionAdd = {
            measureViewModel.addNewMeasure(SimpleMeasure.createFake(1, MeasureType.OXYGEN)
                .first())
//            changeVisibleDialog(true)
        }
    )

    if (isShowDialog) {
        DialogAddMeasure(
            nameMeasure = stringResource(id = R.string.name_oxygen),
            measureFullSuffix = stringResource(id = R.string.suffix_oxygen),
            actionHiddenDialog = { changeVisibleDialog(false) },
            actionAdd = {})
    }
}