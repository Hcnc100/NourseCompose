package com.nullpointer.noursecompose.ui.screen.addAlarm.descriptionScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.ui.screen.addAlarm.ContentPage
import com.nullpointer.noursecompose.ui.screen.addAlarm.descriptionScreen.viewModel.DescriptionViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DescriptionScreen(
    descriptionViewModel: DescriptionViewModel,
) {
    ContentPage(title = stringResource(id = R.string.title_description_alarm)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                OutlinedTextField(value = descriptionViewModel.description,
                    isError = descriptionViewModel.hasErrorDescription,
                    onValueChange = descriptionViewModel::changeDescription,
                    label = { Text(text = stringResource(R.string.hint_description_alarm)) },
                    placeholder = { Text(text = stringResource(R.string.label_description_alarm)) },
                    modifier = Modifier
                        .height(150.dp))
                Text(style = MaterialTheme.typography.caption,
                    modifier = Modifier.align(Alignment.End),
                    color = if (descriptionViewModel.hasErrorDescription) MaterialTheme.colors.error else Color.Unspecified,
                    text = if (descriptionViewModel.hasErrorDescription)
                        stringResource(id = descriptionViewModel.errorDescription) else descriptionViewModel.counterDescription )
            }
        }
    }
}