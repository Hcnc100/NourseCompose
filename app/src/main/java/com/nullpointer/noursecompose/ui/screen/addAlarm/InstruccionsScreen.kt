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
fun InstruccionScreen() {
    ContentPage(title = "Puedes agregar instrucciones is asi lo deseas") {
        val (textSaved, changeText) = rememberSaveable { mutableStateOf("") }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            OutlinedTextField(value = textSaved,
                onValueChange = changeText,
                label = { Text(text = "Descripcion") },
                placeholder = { Text(text = "Describe el recordatorio") },
                modifier = Modifier
                    .height(150.dp))
        }

    }
}