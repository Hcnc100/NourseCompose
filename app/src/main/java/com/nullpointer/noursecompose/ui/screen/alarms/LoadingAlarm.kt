package com.nullpointer.noursecompose.ui.screen.alarms

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.valentinilk.shimmer.shimmer

@Composable
fun LoadingAlarm(
    modifier: Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(dimensionResource(id = R.dimen.size_row_alarm)),
    ) {
        items(10, key = { it }) {
            ItemAlarmLoading()
        }
    }
}

@Composable
private fun ItemAlarmLoading(
    darkTheme: Boolean = isSystemInDarkTheme(),
) {
    val colorShimmer by remember {
        derivedStateOf { if (darkTheme) Color.DarkGray else Color.LightGray }
    }

    Card(
        modifier = Modifier.padding(2.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier.weight(2f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .shimmer()
                        .background(colorShimmer)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .shimmer()
                        .background(colorShimmer)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .shimmer()
                        .background(colorShimmer)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .shimmer()
                        .background(colorShimmer)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(5.dp))
                    .shimmer()
                    .background(colorShimmer)

            )
        }
    }
}