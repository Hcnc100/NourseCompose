package com.nullpointer.noursecompose.ui.screen.logs

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
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun LoadingLogsScreen(
    modifier: Modifier = Modifier
) {
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(dimensionResource(id = R.dimen.size_row_log))
    ) {
        items(15, key = { it }) {
            ItemLoadLog(shimmerInstance)
        }
    }
}

@Composable
private fun ItemLoadLog(
    shimmerInstance: Shimmer,
    modifier: Modifier = Modifier,
    isDark: Boolean = isSystemInDarkTheme(),
) {

    val color by remember {
        derivedStateOf { if (isDark) Color.LightGray else Color.DarkGray }
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .shimmer(shimmerInstance)
                    .background(color)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .shimmer(shimmerInstance)
                    .background(color)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .shimmer(shimmerInstance)
                    .background(color)
            )
        }
    }
}