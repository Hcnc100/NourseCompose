package com.nullpointer.noursecompose.ui.share.mpGraph

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nullpointer.noursecompose.R


@Composable
fun ToolbarBack(title: String, actionBack: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        navigationIcon = {
            IconButton(onClick = actionBack) {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_back),
                    stringResource(id = R.string.description_arrow_back)
                )
            }
        })
}


@Composable
fun SelectionMenuToolbar(
    titleDefault: String,
    titleSelection: String,
    numberSelection: Int,
    actionClear: () -> Unit,
    goToRegistry: () -> Unit,
    goToConfig: () -> Unit,
    reInitAlarms: () -> Unit,
) {
    val (showMenu, changeVisibleMenu) = rememberSaveable { mutableStateOf(false) }
    TopAppBar(
        backgroundColor = if (numberSelection == 0) MaterialTheme.colors.primarySurface else MaterialTheme.colors.primary,
        title = {
            Text(if (numberSelection == 0) titleDefault else titleSelection)
        },
        actions = {
            if (numberSelection != 0) {
                IconButton(onClick = actionClear) {
                    Icon(painterResource(id = R.drawable.ic_clear),
                        contentDescription = stringResource(R.string.description_clear_selection))
                }
            } else {
                IconButton(onClick = { changeVisibleMenu(!showMenu) }) {
                    Icon(painterResource(id = R.drawable.ic_menu),
                        contentDescription = stringResource(R.string.description_icon_options))
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { changeVisibleMenu(false) }
                ) {
                    DropdownMenuItem(onClick = goToRegistry) {
                        Text(text = stringResource(R.string.option_logs))
                    }
                    DropdownMenuItem(onClick = goToConfig) {
                        Text(text = stringResource(R.string.option_config))
                    }
                    DropdownMenuItem(onClick = {
                        reInitAlarms()
                        changeVisibleMenu(false)
                    }) {
                        Text(text = stringResource(R.string.option_re_init_alarm))
                    }
                }
            }
        }
    )
}