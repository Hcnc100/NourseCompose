package com.nullpointer.noursecompose.ui.screen.measure

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.ui.screen.measure.componets.ItemMeasure
import com.nullpointer.noursecompose.ui.share.mpGraph.MpGraphAndroid

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GraphAndTable(
    listMeasure: List<SimpleMeasure>,
    suffixMeasure:String,
    nameMeasure:String,
    minValue:Float,
    maxValues: Float
) {
    Scaffold(
        topBar = {
            MpGraphAndroid(
                list = listMeasure,
                minValue = minValue,
                maxValue = maxValues,
                suffixMeasure = suffixMeasure,
                modifier = Modifier.fillMaxWidth().height(250.dp)
            )
        }
    ) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(dimensionResource(id = R.dimen.size_row_measure)),
            contentPadding = PaddingValues(4.dp),
        ) {
            items(count = listMeasure.size) { index ->
                val measure=listMeasure[index]
                ItemMeasure(
                    nameMeasure,
                    measure.toString()
                )
            }
        }
    }
}