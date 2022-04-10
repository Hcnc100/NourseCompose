package com.nullpointer.noursecompose.ui.share.mpGraph

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
fun SelectionToolbar(
    titleDefault: String,
    titleSelection: String,
    numberSelection: Int,
    actionClear: () -> Unit,
) {
    TopAppBar(
        backgroundColor = if (numberSelection == 0) MaterialTheme.colors.primarySurface else MaterialTheme.colors.primary,
        title = {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                Text(if (numberSelection == 0) titleDefault else titleSelection)
                if (numberSelection != 0)
                    IconButton(onClick = actionClear) {
                        Icon(painterResource(id = R.drawable.ic_clear),
                            contentDescription = stringResource(R.string.description_clear_selection))
                    }
            }
        })
}