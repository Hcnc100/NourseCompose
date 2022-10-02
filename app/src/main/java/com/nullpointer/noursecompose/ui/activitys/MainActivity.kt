package com.nullpointer.noursecompose.ui.activitys

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nullpointer.noursecompose.ui.screen.NavGraphs
import com.nullpointer.noursecompose.ui.states.rememberRootAppState
import com.nullpointer.noursecompose.ui.theme.NourseComposeTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            NourseComposeTheme {
                val rootAppState = rememberRootAppState()
                DestinationsNavHost(
                    navGraph = NavGraphs.main,
                    startRoute = NavGraphs.main.startRoute,
                    navController = rootAppState.navController,
                    engine = rootAppState.navHostEngine,
                    dependenciesContainerBuilder = { dependency(rootAppState.rootActions) }
                )
            }
        }
    }
}

