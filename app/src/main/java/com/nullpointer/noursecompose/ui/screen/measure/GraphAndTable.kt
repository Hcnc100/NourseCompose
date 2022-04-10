package com.nullpointer.noursecompose.ui.screen.measure

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyGridState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.ui.screen.measure.componets.ItemMeasure
import com.nullpointer.noursecompose.ui.share.mpGraph.MpGraphAndroid

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GraphAndTable(
    listMeasure: List<SimpleMeasure>,
    suffixMeasure: String,
    nameMeasure: String,
    descriptionAddNewMeasure:String,
    minValue: Float,
    maxValues: Float,
    actionAdd: () -> Unit,
    isSelectedEnable: Boolean,
    changeSelectState: (ItemSelected) -> Unit,
    listState: LazyGridState
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
            FloatingActionButton(onClick = actionAdd) {
                Icon(painterResource(id = R.drawable.ic_add),
                    contentDescription = descriptionAddNewMeasure)
            }
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