package com.nullpointer.noursecompose.ui.screen.main


import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.shareViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.services.alarms.AlarmReceiver
import com.nullpointer.noursecompose.ui.interfaces.ActionRootDestinations
import com.nullpointer.noursecompose.ui.navigation.HomeDestinations
import com.nullpointer.noursecompose.ui.navigation.MainNavGraph
import com.nullpointer.noursecompose.ui.screen.NavGraphs
import com.nullpointer.noursecompose.ui.screen.destinations.ConfigScreenDestination
import com.nullpointer.noursecompose.ui.screen.destinations.LogsScreensDestination
import com.nullpointer.noursecompose.ui.screen.main.ToolbarActions.*
import com.nullpointer.noursecompose.ui.share.SelectToolbar
import com.nullpointer.noursecompose.ui.states.MainScreenState
import com.nullpointer.noursecompose.ui.states.rememberMainScreenState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate

@MainNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    actionRootDestinations: ActionRootDestinations,
    selectViewModel: SelectionViewModel = shareViewModel(),
    mainScreenState: MainScreenState = rememberMainScreenState(),
) {
    Scaffold(
        topBar = {
            SelectToolbar(
                titleDefault = R.string.app_name,
                titleSelection = R.plurals.selected_items,
                numberSelection = selectViewModel.numberSelection,
                actionToolbar = {action->
                    when(action){
                        CLEAR -> selectViewModel.clearSelection()
                        SETTINGS -> actionRootDestinations.changeRoot(ConfigScreenDestination)
                        LOGS -> actionRootDestinations.changeRoot(LogsScreensDestination)
                        RE_INIT -> {
                            val intent = Intent(
                                mainScreenState.context,
                                AlarmReceiver::class.java
                            )
                            intent.action= AlarmReceiver.KEY_RESTORE
                            mainScreenState.context.applicationContext.sendBroadcast(intent)
                        }
                    }
                }
            )
        },
        bottomBar = {
            MainButtonNavigation(
                navController = mainScreenState.navController,
                actionClear = selectViewModel::clearSelection
            )
        }
    ) { innerPadding ->
        DestinationsNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = mainScreenState.navController,
            engine = mainScreenState.navHostEngine,
            navGraph = NavGraphs.home,
            startRoute = NavGraphs.home.startRoute,
            dependenciesContainerBuilder = {
                dependency(actionRootDestinations)
                dependency(selectViewModel)
            }
        )
    }
}

@Composable
private fun MainButtonNavigation(
    navController: NavController,
    actionClear: () -> Unit
) {
    val currentDestination = navController.currentBackStackEntryAsState()
        .value?.destination
    BottomNavigation {
        HomeDestinations.values().forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination?.route == destination.direction.route,
                onClick = {
                    if (currentDestination?.route != destination.direction.route) actionClear()
                    navController.navigate(destination.direction) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painterResource(id = destination.icon),
                        stringResource(id = destination.description)
                    )
                },
                label = { Text(stringResource(id = destination.title)) },
                selectedContentColor = Color.Red.copy(0.6f),
                unselectedContentColor = Color.White,
                alwaysShowLabel = false,
                modifier = Modifier.background(MaterialTheme.colors.primary)
            )
        }
    }
}