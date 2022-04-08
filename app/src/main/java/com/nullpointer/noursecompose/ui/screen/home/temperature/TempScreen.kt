package com.nullpointer.noursecompose.ui.screen.home.temperature

import androidx.compose.runtime.Composable
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.ui.screen.measure.GraphAndTable

@Composable
fun TempScreen() {
    val listTemp = SimpleMeasure.createFake(20, MeasureType.TEMPERATURE)
    GraphAndTable(listMeasure = listTemp,
        suffixMeasure = SimpleMeasure.suffixTemp,
        nameMeasure = "Temperature",
        minValue = SimpleMeasure.minValueTemp.toFloat(),
        maxValues = SimpleMeasure.maxValueTemp.toFloat())
}