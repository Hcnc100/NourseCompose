package com.nullpointer.noursecompose.ui.screen.measure

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.valentinilk.shimmer.shimmer

@Composable
fun LoadingItemMeasure(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    val color by remember {
        derivedStateOf { if (darkTheme) Color.DarkGray else Color.LightGray }
    }
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row(modifier = modifier.fillMaxSize()) {
                GraphFake(modifier = Modifier.weight(.5f),
                    background = color)
                LazyVerticalGrid(
                    modifier = Modifier.weight(.5f),
                    columns = GridCells.Adaptive(dimensionResource(id = R.dimen.size_row_measure))) {
                    items(10, key = { it }) {
                        ItemMeasure(background = color)
                    }
                }
            }
        }
        else -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Adaptive(dimensionResource(id = R.dimen.size_row_measure))
            ) {
                item(key = "graph-fake", span = { GridItemSpan(maxLineSpan) }) {
                    GraphFake(background = color)
                }
                items(10, key = { it }) {
                    ItemMeasure(background = color)
                }
            }
        }
    }

}

@Composable
private fun GraphFake(
    modifier: Modifier = Modifier,
    background: Color
) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .height(dimensionResource(id = R.dimen.height_graph_measure))
            .clip(RoundedCornerShape(5.dp))
            .shimmer()
            .background(background)
    )
}

@Composable
private fun ItemMeasure(
    modifier: Modifier = Modifier,
    background: Color
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .height(15.dp)
                    .width(50.dp)
                    .shimmer()
                    .background(background)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .height(20.dp)
                    .width(70.dp)
                    .shimmer()
                    .background(background)
            )
        }
    }
}