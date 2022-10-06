package com.nullpointer.noursecompose.ui.screen.logs.componets.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.registry.Log
import com.nullpointer.noursecompose.ui.screen.logs.componets.items.ItemLoadLog
import com.nullpointer.noursecompose.ui.screen.logs.componets.items.ItemLog
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer

@Composable
fun LoadLogsScreen(
    modifier: Modifier = Modifier
) {
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(dimensionResource(id = R.dimen.width_row_log)),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(15, key = { it }) {
            ItemLoadLog(shimmerInstance)
        }
    }
}

@Composable
fun SuccessLogsScreen(
    listLog: List<Log>,
    isSelectEnable: Boolean,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    actionClickLog: (Log) -> Unit,
    changeItemSelected: (ItemSelected) -> Unit
) {

    LazyVerticalGrid(
        state = lazyGridState,
        modifier = modifier,
        columns = GridCells.Adaptive(dimensionResource(id = R.dimen.width_row_log)),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(listLog) { log ->
            ItemLog(
                log = log,
                actionClick = actionClickLog,
                changeItemSelected = changeItemSelected,
                isSelectEnable = isSelectEnable
            )
        }
    }
}