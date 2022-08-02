package com.nullpointer.noursecompose.ui.screen.measure

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.measure.SimpleMeasure

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemMeasure(
    nameMeasure: String,
    suffixMeasure: String,
    measure: SimpleMeasure,
    isSelectedEnable: Boolean,
    changeSelectState: (ItemSelected) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectColor by remember(measure.isSelected) {
        derivedStateOf {
            if (measure.isSelected) Color.Cyan.copy(alpha = 0.5f) else Color.Unspecified
        }
    }

    Card(
        modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .drawBehind {
                    drawRect(selectColor)
                }
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