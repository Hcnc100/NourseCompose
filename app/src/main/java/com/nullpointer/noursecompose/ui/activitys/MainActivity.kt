package com.nullpointer.noursecompose.ui.activitys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.ui.navigation.HomeDestinations
import com.nullpointer.noursecompose.ui.screen.home.NavGraphs
import com.nullpointer.noursecompose.ui.screen.home.navDestination
import com.nullpointer.noursecompose.ui.share.mpGraph.SimpleToolbar
import com.nullpointer.noursecompose.ui.share.mpGraph.ToolbarBack
import com.nullpointer.noursecompose.ui.theme.NourseComposeTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigateTo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NourseComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {

                    val navController = rememberNavController()
                    var isHomeRoute by remember { mutableStateOf(false) }

                    navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
                        isHomeRoute = HomeDestinations.isHomeRoute(navDestination.route)
                    }

                    Scaffold(
                        bottomBar = {  AnimatedVisibility(
                            visible = isHomeRoute,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it }),
                        ) {
                            ButtonNavigation(
                                navController = navController
                            )
                        } }, topBar = {
                            AnimatedVisibility(
                                visible = isHomeRoute,
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { it }),
                            ) {
                                SimpleToolbar(title = stringResource(id = R.string.app_name))
                            }
                        }
                    ){ innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            DestinationsNavHost(
                                navController = navController,
                                navGraph = NavGraphs.root,
//                                dependenciesContainerBuilder = {
//                                    dependency(authViewModel)
//                                }
                            )
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
                        navController.navigateTo(destination.direction) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(painterResource(id = destination.icon), stringResource(id = destination.description)) },
                    label = { Text(stringResource(id = destination.title)) },
                )
            }
        }
    }
}
