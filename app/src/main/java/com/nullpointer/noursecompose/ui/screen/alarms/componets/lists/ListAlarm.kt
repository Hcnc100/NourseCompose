package com.nullpointer.noursecompose.ui.screen.alarms.componets.lists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.ui.screen.alarms.componets.items.ItemAlarm
import com.nullpointer.noursecompose.ui.screen.alarms.componets.items.ItemLoadAlarm
import com.nullpointer.noursecompose.ui.screen.empty.EmptyScreen
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer

@Composable
fun ListLoadAlarm(
    modifier: Modifier,
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Adaptive(dimensionResource(id = R.dimen.size_row_alarm))
    ) {
        items(10, key = { it }) {
            ItemLoadAlarm(shimmer)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListSuccessAlarm(
    modifier: Modifier = Modifier,
    listAlarm: List<Alarm>,
    listState: LazyGridState,
    isSelectedEnable: Boolean,
    simpleClickAlarm: (Alarm) -> Unit,
    changeSelectState: (ItemSelected) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Adaptive(dimensionResource(id = R.dimen.size_row_alarm))
    ) {
        items(
            items = listAlarm,
            key = { it.id }
        ) { alarm ->
            ItemAlarm(
                alarm = alarm,
                isSelectedEnable = isSelectedEnable,
                changeSelectState = changeSelectState,
                actionClickSimple = simpleClickAlarm,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}

@Composable
fun ListEmptyAlarm(
    modifier: Modifier
) {
    EmptyScreen(
        modifier = modifier,
        animation = R.raw.empty3,
        textEmpty = stringResource(R.string.message_empty_alarm_screen)
    )
}