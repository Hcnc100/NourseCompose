package com.nullpointer.noursecompose.ui.screen.home.oxygen

import androidx.compose.runtime.Composable
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.ui.screen.measure.GraphAndTable

@Composable
fun OxygenScreen() {
    val listOxygen = SimpleMeasure.createFake(20, MeasureType.OXYGEN)
    GraphAndTable(listMeasure = listOxygen,
        suffixMeasure = SimpleMeasure.suffixOxygen,
        nameMeasure = "Oxygen",
        minValue = SimpleMeasure.minValueOxygen.toFloat(),
        maxValues = SimpleMeasure.maxValueOxygen.toFloat())
}