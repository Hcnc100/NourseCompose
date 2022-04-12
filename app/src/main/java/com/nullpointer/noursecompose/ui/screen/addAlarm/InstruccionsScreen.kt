package com.nullpointer.noursecompose.ui.screen.addAlarm

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.nullpointer.noursecompose.R

@Composable
fun InstruccionScreen(
    actionNext: () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize()) {

        Text(text = "Puedes agregar instrucciones is asi lo deseas",
            style = MaterialTheme.typography.h5, modifier = Modifier
                .padding(30.dp)
                .weight(1f))



        Box(Modifier
            .weight(2f)
            .fillMaxWidth(), contentAlignment = Alignment.Center) {
            val (textSaved, changeText) = rememberSaveable { mutableStateOf("") }
            OutlinedTextField(value = textSaved,
                onValueChange = changeText,
                label = { Text(text = "Descripcion") },
                placeholder = { Text(text = "Describe el recordatorio") },
                modifier = Modifier
                    .height(150.dp))
        }



        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.BottomEnd)) {
                Text("Siguiente")
            }
        }


    }
}