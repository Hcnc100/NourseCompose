package com.nullpointer.noursecompose.ui.share.mpGraph

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nullpointer.noursecompose.R


@Composable
fun ToolbarBack(title: String, actionBack: (() -> Unit)? = null) {
    TopAppBar(title = { Text(title) },
        navigationIcon = {
            actionBack?.let { action ->
                IconButton(onClick = { action() }) {
                    Icon(painterResource(id = R.drawable.ic_arrow_back),
                        stringResource(id = R.string.description_arrow_back))
                }
            }
        })
}

@Composable
fun SimpleToolbar(title: String) {
    TopAppBar(title = { Text(title) })
}

@Composable
fun SelectionMenuToolbar(
    titleDefault: String,
    titleSelection: String,
    numberSelection: Int,
    actionClear: () -> Unit,
    goToRegistry: () -> Unit,
    goToConfig:()->Unit
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
                        Text(text = "Ver los registros")
                    }
                    DropdownMenuItem(onClick = goToConfig) {
                        Text(text = "Ir a la configuracion")
                    }
                }
            }
        }
    )
}