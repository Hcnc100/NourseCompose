package com.nullpointer.noursecompose.ui.screen.measure

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.ui.share.mpGraph.MpGraphAndroid

@OptIn(ExperimentalFoundationApi::class)
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
    modifier: Modifier = Modifier
) {

    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            LazyVerticalGrid(
                state = listState,
                modifier = modifier,
                contentPadding = PaddingValues(2.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                columns = GridCells.Adaptive(dimensionResource(id = R.dimen.width_row_measure)),
            ) {
                item(
                    key = "graph-header",
                    span = { GridItemSpan(maxLineSpan) },
                ) {
                    MpGraphAndroid(
                        list = listMeasure.reversed(),
                        minValue = minValue,
                        maxValue = maxValues,
                        suffixMeasure = suffixMeasure,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.height_graph_measure))
                    )
                }
                items(listMeasure, key = { it.id }) { measure ->
                    ItemMeasure(
                        measure = measure,
                        nameMeasure = nameMeasure,
                        suffixMeasure = suffixMeasure,
                        isSelectedEnable = isSelectedEnable,
                        changeSelectState = changeSelectState,
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }

        }
        else -> {
            Row(modifier = modifier.fillMaxSize()) {
                MpGraphAndroid(
                    list = listMeasure.reversed(),
                    minValue = minValue,
                    maxValue = maxValues,
                    suffixMeasure = suffixMeasure,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.height_graph_measure))
                        .weight(.5f)
                )
                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Adaptive(dimensionResource(id = R.dimen.width_row_measure)),
                    contentPadding = PaddingValues(2.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(.5f)
                ) {
                    items(listMeasure, key = { it.id }) { measure ->
                        ItemMeasure(
                            modifier = Modifier.animateItemPlacement(),
                            nameMeasure = nameMeasure,
                            suffixMeasure = suffixMeasure,
                            measure = measure,
                            isSelectedEnable = isSelectedEnable,
                            changeSelectState = changeSelectState
                        )
                    }
                }
            }
        }
    }
}



