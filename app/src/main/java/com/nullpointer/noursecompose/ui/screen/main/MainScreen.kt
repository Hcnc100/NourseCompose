package com.nullpointer.noursecompose.ui.screen.main

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
import com.nullpointer.noursecompose.ui.interfaces.ActionRootDestinations
import com.nullpointer.noursecompose.ui.navigation.HomeDestinations
import com.nullpointer.noursecompose.ui.navigation.MainNavGraph
import com.nullpointer.noursecompose.ui.screen.NavGraphs
import com.nullpointer.noursecompose.ui.share.SimpleToolbar
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
    mainScreenState: MainScreenState = rememberMainScreenState()
) {
    Scaffold(
        topBar = { SimpleToolbar(title = stringResource(id = R.string.app_name)) },
        bottomBar = { MainButtonNavigation(navController = mainScreenState.navController) }
    ) { innerPadding ->
        DestinationsNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = mainScreenState.navController,
            engine = mainScreenState.navHostEngine,
            navGraph = NavGraphs.home,
            startRoute = NavGraphs.home.startRoute,
            dependenciesContainerBuilder = { dependency(actionRootDestinations) }
        )
    }
}

@Composable
private fun MainButtonNavigation(
    navController: NavController,
) {
    val currentDestination = navController.currentBackStackEntryAsState()
        .value?.destination
    BottomNavigation {
        HomeDestinations.values().forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination?.route == destination.direction.route,
                onClick = {
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