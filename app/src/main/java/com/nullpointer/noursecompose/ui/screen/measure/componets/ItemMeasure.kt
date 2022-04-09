package com.nullpointer.noursecompose.ui.screen.measure.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.models.measure.SimpleMeasure

@Composable
fun ItemMeasure(
    nameMeasure:String,
    suffixMeasure:String,
    measure:SimpleMeasure,
) {
    Card(modifier = Modifier.padding(4.dp), shape = RoundedCornerShape(10.dp)) {
        Column (modifier = Modifier.padding(10.dp)){
            Text(nameMeasure, style = MaterialTheme.typography.caption)
            Spacer(modifier = Modifier.height(20.dp))
            Text("${measure.value} ${suffixMeasure[0]}", style = MaterialTheme.typography.body1, fontWeight = FontWeight.W600)
        }
    }
}