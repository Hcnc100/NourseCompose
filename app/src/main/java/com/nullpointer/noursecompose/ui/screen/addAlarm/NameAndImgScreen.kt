package com.nullpointer.noursecompose.ui.screen.addAlarm

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.airbnb.lottie.model.content.CircleShape
import com.nullpointer.noursecompose.R

@Composable
fun NameAndImgScreen(
    actionNext: () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {

        Text(text = "Agrega una nombre a la alarma y una imagen, para recordarlo mas faicl",
            style = MaterialTheme.typography.h5, modifier = Modifier
                .padding(30.dp)
                .weight(1f))



        Column(modifier = Modifier
            .fillMaxWidth()
            .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Card(backgroundColor = Color.LightGray, shape = RoundedCornerShape(10.dp)) {
                Image(painter = rememberImagePainter(R.drawable.ic_image),
                    contentDescription = "",
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }


            Spacer(modifier = Modifier.height(50.dp))
            val (textSaved, changeText) = rememberSaveable { mutableStateOf("") }
            OutlinedTextField(value = textSaved,
                onValueChange = changeText,
                label = { Text(text = "Nombre del recordatorio") },
                modifier = Modifier.align(Alignment.CenterHorizontally))
        }


        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            Button(onClick = actionNext, modifier = Modifier.align(Alignment.BottomEnd)) {
                Text("Siguiente")
            }
        }


    }
}