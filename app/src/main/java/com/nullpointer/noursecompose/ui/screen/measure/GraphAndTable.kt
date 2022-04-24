package com.nullpointer.noursecompose.ui.screen.measure

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyGridState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.ui.screen.measure.componets.ItemMeasure
import com.nullpointer.noursecompose.ui.share.ButtonToggleAddRemove
import com.nullpointer.noursecompose.ui.share.mpGraph.MpGraphAndroid

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun GraphAndTable(
    listMeasure: List<SimpleMeasure>,
    suffixMeasure: String,
    nameMeasure: String,
    minValue: Float,
    maxValues: Float,
    isSelectedEnable: Boolean,
    changeSelectState: (ItemSelected) -> Unit,
    listState: LazyGridState,
) {

    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Scaffold(
                topBar = {
                    MpGraphAndroid(
                        list = listMeasure.reversed(),
                        minValue = minValue,
                        maxValue = maxValues,
                        suffixMeasure = suffixMeasure,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                },
            ) {
                LazyVerticalGrid(
                    state = listState,
                    cells = GridCells.Adaptive(dimensionResource(id = R.dimen.size_row_measure)),
                    contentPadding = PaddingValues(4.dp),
                ) {
                    items(count = listMeasure.size) { index ->
                        ItemMeasure(
                            nameMeasure,
                            suffixMeasure,
                            listMeasure[index],
                            isSelectedEnable,
                            changeSelectState
                        )
                    }
                }
            }
        }
        // ? Configuration.ORIENTATION_PORTRAIT
        else -> {
           Row(modifier = Modifier.fillMaxSize()) {
               MpGraphAndroid(
                   list = listMeasure.reversed(),
                   minValue = minValue,
                   maxValue = maxValues,
                   suffixMeasure = suffixMeasure,
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(250.dp).weight(.5f)
               )
               LazyVerticalGrid(
                   state = listState,
                   cells = GridCells.Adaptive(dimensionResource(id = R.dimen.size_row_measure)),
                   contentPadding = PaddingValues(4.dp),
                   modifier = Modifier.weight(.5f)
               ) {
                   items(count = listMeasure.size) { index ->
                       ItemMeasure(
                           nameMeasure,
                           suffixMeasure,
                           listMeasure[index],
                           isSelectedEnable,
                           changeSelectState
                       )
                   }
               }
           }
        }
    }
}



