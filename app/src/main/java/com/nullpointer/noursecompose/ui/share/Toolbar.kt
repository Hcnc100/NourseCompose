package com.nullpointer.noursecompose.ui.share


import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.getPlural
import com.nullpointer.noursecompose.ui.screen.main.ToolbarActions

@Composable
fun ToolbarBack(title: String, actionBack: () -> Unit) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        title = { Text(title) },
        contentColor = Color.White,
        navigationIcon = {
            IconButton(onClick =actionBack) {
                Icon(painterResource(id = R.drawable.ic_arrow_back),
                    stringResource(id = R.string.description_arrow_back))
            }
        })
}

@Composable
fun SimpleToolbar(title: String) {
    TopAppBar(
        contentColor = Color.White,
        backgroundColor = MaterialTheme.colors.primary,
        title = { Text(title) })
}

@Composable
fun SelectToolbar(
    @StringRes
    titleDefault: Int,
    @PluralsRes
    titleSelection: Int,
    numberSelection: Int,
    actionToolbar: (ToolbarActions) -> Unit,
    context: Context = LocalContext.current
) {

    val title by remember(numberSelection) {
        derivedStateOf {
            if (numberSelection == 0)
                context.getString(titleDefault) else
                context.getPlural(titleSelection, numberSelection)
        }
    }

    var isMenuExpanded by remember {
        mutableStateOf(false)
    }


    TopAppBar(
        backgroundColor = if (numberSelection == 0) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
        title = { Text(title, color = Color.White) },
        contentColor = Color.White,
        actions = {
            if (numberSelection != 0) {
                IconButton(onClick = { actionToolbar(ToolbarActions.CLEAR) }) {
                    Icon(
                        painterResource(id = R.drawable.ic_clear),
                        contentDescription = stringResource(R.string.description_clear_selection)
                    )
                }
            } else {
                IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = stringResource(R.string.description_icon_options)
                    )
                }
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false }) {
                    DropdownMenuItem(
                        onClick = {
                            actionToolbar(ToolbarActions.LOGS)
                            isMenuExpanded=false
                        }
                    ){ Text(text = stringResource(id = R.string.option_logs)) }

                    DropdownMenuItem(
                        onClick = {
                            actionToolbar(ToolbarActions.SETTINGS)
                            isMenuExpanded=false
                        }
                    ){ Text(text = stringResource(id = R.string.option_config)) }

                    DropdownMenuItem(
                        onClick = {
                            actionToolbar(ToolbarActions.RE_INIT)
                            isMenuExpanded=false
                        }
                    ){ Text(text = stringResource(id = R.string.option_re_init_alarm)) }
                }
            }
        }
    )
}