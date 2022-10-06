package com.nullpointer.noursecompose.ui.screen.logs.componets.items


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.myShimmer
import com.valentinilk.shimmer.Shimmer

@Composable
fun ItemLoadLog(
    shimmerInstance: Shimmer,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .height(dimensionResource(id = R.dimen.height_row_log)),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .myShimmer(shimmerInstance)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .myShimmer(shimmerInstance)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .myShimmer(shimmerInstance)
            )
        }
    }
}