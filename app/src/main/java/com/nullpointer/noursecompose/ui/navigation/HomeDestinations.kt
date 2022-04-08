package com.nullpointer.noursecompose.ui.navigation

import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.ui.screen.home.destinations.AlarmScreenDestination
import com.nullpointer.noursecompose.ui.screen.home.destinations.OxygenScreenDestination
import com.nullpointer.noursecompose.ui.screen.home.destinations.TempScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class HomeDestinations(
    val direction: DirectionDestinationSpec,
    val title: Int,
    val icon: Int,
    val description:Int
) {
    AlarmScreen(
        direction = AlarmScreenDestination,
        title = R.string.title_nav_alarm,
        icon = R.drawable.ic_alarm,
        description = R.string.description_nav_alarm
    ),

    OxygenScreen(
        direction = OxygenScreenDestination,
        title = R.string.title_nav_oxygen,
        icon = R.drawable.ic_oxygen,
        description = R.string.description_nav_oxygen
    ),

    TempScreen(
        direction = TempScreenDestination,
        title = R.string.title_nav_temp,
        icon = R.drawable.ic_temp,
        description = R.string.description_nav_temp
    );

    companion object {
        private val listDestinations = listOf(
            AlarmScreen,
            OxygenScreen,
            TempScreen
        )

        fun isHomeRoute(route: String?): Boolean {
            if (route == null) return false
            return listDestinations.find { it.direction.route == route } != null
        }
    }

}