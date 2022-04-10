package com.nullpointer.noursecompose.ui.screen.measure

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyGridState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    actionAdd: () -> Unit,
    actionDeleter: () -> Unit,
    descriptionAddNewMeasure: String,
    descriptionDeleterMeasure: String,
    isSelectedEnable: Boolean,
    changeSelectState: (ItemSelected) -> Unit,
    listState: LazyGridState,
) {
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
        floatingActionButton = {

                ButtonToggleAddRemove(
                    isVisible = !listState.isScrollInProgress,
                    isSelectedEnable = isSelectedEnable,
                    descriptionButtonAdd = descriptionAddNewMeasure,
                    actionAdd = actionAdd,
                    descriptionButtonRemove = descriptionDeleterMeasure,
                    actionRemove = actionDeleter
                )


        }
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

