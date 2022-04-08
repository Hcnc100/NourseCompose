package com.nullpointer.noursecompose.ui.share.mpGraph

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.measure.SimpleMeasure


@SuppressLint("ViewConstructor")
class CustomMarkerView(
    private val listMeasure: List<SimpleMeasure>,
    context: Context,
    layoutId: Int,
    private val measureSuffix: String,
) :
    MarkerView(context, layoutId) {

    private val textDate: TextView = findViewById(R.id.textDateMarker)
    private val textValue: TextView = findViewById(R.id.textValueMarker)

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) {
            return
        }
        val currentIndex = e.x.toInt()
        val measure = listMeasure[currentIndex]
        textDate.text = "2012/12/12"
        textValue.text = "${measure.value} $measureSuffix"
    }
}