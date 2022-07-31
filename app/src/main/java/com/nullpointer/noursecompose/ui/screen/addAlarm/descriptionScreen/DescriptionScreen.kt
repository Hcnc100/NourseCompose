package com.nullpointer.noursecompose.ui.screen.addAlarm.descriptionScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.ui.screen.addAlarm.TitleAddAlarm
import com.nullpointer.noursecompose.ui.screen.addAlarm.viewModel.AddAlarmViewModel
import com.nullpointer.noursecompose.ui.share.EditableTextSavable

@Composable
fun DescriptionScreen(
    addAlarmViewModel: AddAlarmViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TitleAddAlarm(title = stringResource(id = R.string.title_description_alarm))
        Spacer(modifier = Modifier.height(20.dp))
        EditableTextSavable(
            valueProperty = addAlarmViewModel.description,
            modifier = Modifier.padding(horizontal = 20.dp),
            modifierText = Modifier.height(200.dp))
    }

}