package com.nullpointer.noursecompose.ui.screen.empty

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.nullpointer.noursecompose.ui.share.lottieFiles.LottieContainer

@Composable
fun EmptyScreen(
    @RawRes
    animation: Int,
    textEmpty: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LottieContainer(
            modifier = Modifier
                .weight(.8f)
                .fillMaxWidth(),
            animation = animation
        )
        Box(
            modifier = Modifier
                .weight(.2f)
                .fillMaxWidth()
        ) {
            Text(textEmpty,
                modifier = Modifier.fillMaxWidth(.8f).align(Alignment.TopCenter),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Center
                )
        }
    }

}