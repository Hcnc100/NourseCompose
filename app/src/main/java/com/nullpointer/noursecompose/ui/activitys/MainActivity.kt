package com.nullpointer.noursecompose.ui.activitys

import android.os.Bundle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.navigation.HomeDestinations
import com.nullpointer.noursecompose.ui.screen.NavGraphs
import com.nullpointer.noursecompose.ui.screen.destinations.LogsScreensDestination
import com.nullpointer.noursecompose.ui.screen.navDestination
import com.nullpointer.noursecompose.ui.share.mpGraph.SelectionMenuToolbar
import com.nullpointer.noursecompose.ui.theme.NourseComposeTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val selectionViewModel: SelectionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NourseComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {

                    val context = LocalContext.current
                    val navController = rememberNavController()
                    var isHomeRoute by remember { mutableStateOf(false) }

                    navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
                        isHomeRoute = HomeDestinations.isHomeRoute(navDestination.route)
                    }

                    Scaffold(
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
                                    goToRegistry = {navController.navigateTo(LogsScreensDestination)}
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
                                }
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
