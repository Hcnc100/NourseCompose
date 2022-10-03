package com.nullpointer.noursecompose.ui.screen.alarms.componets.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.core.utils.myShimmer
import com.valentinilk.shimmer.Shimmer

@Composable
fun ItemLoadAlarm(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ImageFakeAlarm(
                modifier = Modifier.weight(2F),
                shimmer = shimmer
            )
            InfoFakeAlarm(
                shimmer = shimmer,
                modifier = Modifier.weight(3F)
            )
        }
    }
}

@Composable
private fun InfoFakeAlarm(
    modifier: Modifier = Modifier,
    shimmer: Shimmer
) {
    Column(
        modifier = modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .myShimmer(shimmer)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .myShimmer(shimmer)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .myShimmer(shimmer)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .myShimmer(shimmer)
        )

    }
}


@Composable
private fun ImageFakeAlarm(
    modifier: Modifier = Modifier,
    shimmer: Shimmer
) {
    Box(
        modifier = modifier
            .height(110.dp)
            .myShimmer(shimmer)
    )
}