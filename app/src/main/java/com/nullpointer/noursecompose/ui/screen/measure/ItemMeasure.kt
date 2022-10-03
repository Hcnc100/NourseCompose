package com.nullpointer.noursecompose.ui.screen.measure

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.measure.SimpleMeasure

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemMeasure(
    nameMeasure: String,
    suffixMeasure: String,
    measure: SimpleMeasure,
    isSelectedEnable: Boolean,
    modifier: Modifier = Modifier,
    changeSelectState: (ItemSelected) -> Unit,
) {
    val selectColor by animateColorAsState(
        if (measure.isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
    )

    Surface(
        modifier = modifier,
        color = selectColor,
        shape = MaterialTheme.shapes.small,
        elevation = dimensionResource(id = R.dimen.elevation_surface)
    ) {
        Column(
            modifier = Modifier
                .combinedClickable(
                    onClick = { if (isSelectedEnable) changeSelectState(measure) },
                    onLongClick = { if (!isSelectedEnable) changeSelectState(measure) },
                )
                .padding(10.dp)

        ) {
            Text(nameMeasure, style = MaterialTheme.typography.caption)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "${measure.value} ${suffixMeasure[0]}",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W600
            )
        }
    }
}