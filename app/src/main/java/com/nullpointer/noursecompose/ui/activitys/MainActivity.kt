package com.nullpointer.noursecompose.ui.activitys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nullpointer.noursecompose.ui.screen.home.oxygen.OxygenScreen
import com.nullpointer.noursecompose.ui.screen.home.temperature.TempScreen
import com.nullpointer.noursecompose.ui.screen.measure.GraphAndTable
import com.nullpointer.noursecompose.ui.theme.NourseComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NourseComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    TempScreen()
                }
            }
        }
    }
}
