package com.nullpointer.noursecompose.ui.screen.home.oxygen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.ui.dialogs.DialogAddMeasure
import com.nullpointer.noursecompose.ui.screen.measure.GraphAndTable
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun OxygenScreen() {
    val listOxygen = SimpleMeasure.createFake(20, MeasureType.OXYGEN)
    val (isShowDialog, changeVisibleDialog) = rememberSaveable { mutableStateOf(false) }
    GraphAndTable(listMeasure = listOxygen,
        descriptionAddNewMeasure = stringResource(id = R.string.description_add_oxygen),
        suffixMeasure = stringResource(id = R.string.suffix_oxygen),
        nameMeasure = stringResource(id = R.string.name_oxygen),
        minValue = SimpleMeasure.minValueOxygen.toFloat(),
        maxValues = SimpleMeasure.maxValueOxygen.toFloat(),
        actionAdd = {changeVisibleDialog(true)}
    )

    if (isShowDialog) {
        DialogAddMeasure(
            nameMeasure = stringResource(id = R.string.name_oxygen),
            measureFullSuffix = stringResource(id = R.string.suffix_oxygen),
            actionHiddenDialog = { changeVisibleDialog(false) },
            actionAdd = {})
    }
}