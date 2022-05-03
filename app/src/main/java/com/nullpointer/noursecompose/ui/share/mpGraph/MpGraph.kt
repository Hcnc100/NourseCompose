package com.nullpointer.noursecompose.ui.share.mpGraph

import android.content.Context
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.measure.SimpleMeasure


@Composable
fun MpGraphAndroid(
    list: List<SimpleMeasure>,
    modifier: Modifier = Modifier,
    minValue: Float,
    maxValue: Float,
    suffixMeasure: String,
) {
    // * get colors for the graph
    val primaryColor = MaterialTheme.colors.primaryVariant.toArgb()
    val secondaryColor = MaterialTheme.colors.primary.toArgb()
    val textColor = MaterialTheme.colors.onBackground.toArgb()
    AndroidView(
        // * launch every update
        update = {
            updateValuesGraph(
                context = it.context,
                lineChart = it,
                list = list,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                colorText = textColor,
                suffixMeasure = suffixMeasure
            )
        },
        modifier = modifier,
        // * launch when create
        // ! only draw limits once
        factory = { context ->
            LineChart(context).apply {
                setupGraph(this)
                drawLimits(
                    lineChart = this,
                    minValue = minValue,
                    maxValue = maxValue,
                    colorText = textColor,
                    context = context
                )
            }
        },
    )
}


fun drawLimits(
    context: Context,
    lineChart: LineChart,
    minValue: Float,
    maxValue: Float,
    colorText: Int,
) = with(lineChart) {
    // * draw limit lower
    LimitLine(minValue, context.getString(R.string.text_graph_min_value)).apply {
        enableDashedLine(10f, 15f, 0f)
        lineWidth = 2f
    }.also {
        axisLeft.addLimitLine(it)
        it.textColor = colorText
    }
    // * draw limit upper
    LimitLine(maxValue, context.getString(R.string.text_graph_max_value)).apply {
        enableDashedLine(10f, 15f, 0f)
        lineWidth = 2f
    }.also {
        axisLeft.addLimitLine(it)
        it.textColor = colorText
    }
}


fun setupGraph(graph: LineChart) = with(graph) {
    // * config graph and hidden labels
    description.isEnabled = false
    legend.isEnabled = false

    // * config axis
    xAxis.setDrawGridLines(false)
    xAxis.setDrawLabels(false)
    axisRight.setDrawLabels(false)
    axisRight.setDrawGridLines(false)
    axisLeft.setDrawGridLines(false)

    // * config container
    setDrawBorders(true)
}

fun updateValuesGraph(
    context: Context,
    lineChart: LineChart,
    list: List<SimpleMeasure>,
    primaryColor: Int,
    secondaryColor: Int,
    colorText: Int,
    suffixMeasure: String,
) = with(lineChart) {

    // * create all entry
    val listAllMeasure = list.mapIndexed { index, simpleMeasure ->
        Entry(index.toFloat(), simpleMeasure.value)
    }
    // * create dataset
    val dataSet = LineDataSet(listAllMeasure, "").apply {
        valueTextColor = colorText
        color = primaryColor
        setDrawCircles(true)
        setCircleColor(color)
        mode = LineDataSet.Mode.LINEAR
        setDrawFilled(true)
        fillColor = secondaryColor
        axisLeft.textColor = colorText
    }
    // ! remove old marker
    invalidate()
    highlightValue(null)
    data = LineData(dataSet)
    marker = CustomMarkerView(list, context, R.layout.custom_marker_measure, suffixMeasure)

}
