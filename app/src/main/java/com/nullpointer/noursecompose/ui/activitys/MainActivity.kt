package com.nullpointer.noursecompose.ui.activitys

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.cutoutPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.services.AlarmReceiver
import com.nullpointer.noursecompose.ui.navigation.HomeDestinations
import com.nullpointer.noursecompose.ui.screen.NavGraphs
import com.nullpointer.noursecompose.ui.screen.destinations.ConfigScreenDestination
import com.nullpointer.noursecompose.ui.screen.destinations.LogsScreensDestination
import com.nullpointer.noursecompose.ui.screen.navDestination
import com.nullpointer.noursecompose.ui.share.mpGraph.SelectionMenuToolbar
import com.nullpointer.noursecompose.ui.theme.NourseComposeTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val selectionViewModel: SelectionViewModel by viewModels()
    private val alarmViewModel: AlarmViewModel by viewModels()
    private var loading = true

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean("KEY_LOADING", loading)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splash = installSplashScreen()
        loading = savedInstanceState?.getBoolean("KEY_LOADING") ?: true
        splash.setKeepOnScreenCondition { loading }
        // ! remove the decoration for use "ProvideWindowInsets"
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            NourseComposeTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    LaunchedEffect(alarmViewModel.listAlarm) {
                        alarmViewModel.listAlarm.takeWhile { loading }.collect {
                            delay(200)
                            loading = it != null
                        }
                    }
                    val configuration = LocalConfiguration.current
                    var modifier = Modifier.fillMaxSize()
                    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        modifier = modifier.cutoutPadding()
                    }
                    Surface(modifier = modifier,
                        color = MaterialTheme.colors.background) {
                        val context = LocalContext.current
                        val navController = rememberNavController()
                        var isHomeRoute by remember { mutableStateOf(false) }

                        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
                            isHomeRoute = HomeDestinations.isHomeRoute(navDestination.route)
                        }

                        Scaffold(
                            modifier = Modifier
                                .navigationBarsWithImePadding()
                                .statusBarsPadding(),
                            bottomBar = {
                                AnimatedVisibility(
                                    visible = isHomeRoute,
                                    enter = slideInVertically(initialOffsetY = { it }),
                                    exit = slideOutVertically(targetOffsetY = { it }),
                                ) {
                                    ButtonNavigation(
                                        navController = navController
                                    )
                                }
                            }, topBar = {
                                AnimatedVisibility(
                                    visible = isHomeRoute,
                                    enter = slideInVertically(initialOffsetY = { it }),
                                    exit = slideOutVertically(targetOffsetY = { it }),
                                ) {
                                    SelectionMenuToolbar(
                                        titleDefault = stringResource(id = R.string.app_name),
                                        numberSelection = selectionViewModel.numberSelection,
                                        actionClear = selectionViewModel::clearSelection,
                                        titleSelection = context.resources.getQuantityString(
                                            R.plurals.selected_items,
                                            selectionViewModel.numberSelection,
                                            selectionViewModel.numberSelection),
                                        goToRegistry = {
                                            navController.navigateTo(LogsScreensDestination)
                                        },
                                        goToConfig = {
                                            navController.navigateTo(ConfigScreenDestination)
                                        },
                                        reInitAlarms = {
                                            val intent =
                                                Intent(context, AlarmReceiver::class.java).apply {
                                                    action =
                                                        "com.nullpointer.noursecompose.android.action.broadcast"
                                                }
                                            applicationContext.sendBroadcast(intent)
                                        }
                                    )
                                }
                            }
                        ) { innerPadding ->
                            Box(modifier = Modifier.padding(innerPadding)) {
                                DestinationsNavHost(
                                    navController = navController,
                                    navGraph = NavGraphs.root,
                                    dependenciesContainerBuilder = {
                                        dependency(selectionViewModel)
                                        dependency(alarmViewModel)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ButtonNavigation(
        navController: NavController,
    ) {
        val currentDestination = navController.currentBackStackEntryAsState()
            .value?.navDestination

        BottomNavigation {
            HomeDestinations.values().forEach { destination ->
                BottomNavigationItem(
                    selected = currentDestination == destination.direction,
                    onClick = {
                        // * clear selection if destination change
                        if (currentDestination != destination.direction)
                            selectionViewModel.clearSelection()
                        // * navigate to destiny
                        navController.navigateTo(destination.direction) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(painterResource(id = destination.icon),
                            stringResource(id = destination.description))
                    },
                    label = { Text(stringResource(id = destination.title)) },
                )
            }
        }
    }
}
