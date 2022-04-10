package com.nullpointer.noursecompose.ui.share

import androidx.compose.animation.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ButtonToggleAddRemove(
    isVisible: Boolean,
    isSelectedEnable: Boolean,
    descriptionButtonAdd: String,
    actionAdd: () -> Unit,
    descriptionButtonRemove: String,
    actionRemove: () -> Unit,
) {

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        FabAnimation(
            isVisible = !isSelectedEnable,
            icon = Icons.Default.Add,
            description = descriptionButtonAdd,
            action = actionAdd
        )

        FabAnimation(
            isVisible = isSelectedEnable,
            icon = Icons.Default.Delete,
            description = descriptionButtonRemove,
            action = actionRemove
        )
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FabAnimation(
    isVisible: Boolean,
    icon: ImageVector,
    description: String,
    action: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        FloatingActionButton(onClick = { action() }) {
            Icon(imageVector = icon,
                contentDescription = description
            )
        }
    }
}
