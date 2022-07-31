package com.nullpointer.noursecompose.ui.activitys

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.screen.NavGraphs
import com.nullpointer.noursecompose.ui.states.rememberRootAppState
import com.nullpointer.noursecompose.ui.theme.NourseComposeTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val selectionViewModel: SelectionViewModel by viewModels()
    private val alarmViewModel: AlarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isSplash = true
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                isSplash
            }
        }
        lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO) {
                withContext(Dispatchers.IO) { delay(1000) }
                isSplash = false
            }
        }
        setContent {
            NourseComposeTheme {
                val rootAppState= rememberRootAppState()
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

