package com.nullpointer.noursecompose.ui.screen.addAlarm.descriptionScreen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.delegates.PropertySavableString
import com.nullpointer.noursecompose.ui.screen.addAlarm.TitleAddAlarm
import com.nullpointer.noursecompose.ui.share.EditableTextSavable

@Composable
fun DescriptionScreen(
    modifier: Modifier = Modifier,
    descriptionProperty: PropertySavableString
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        TitleAddAlarm(title = stringResource(id = R.string.title_description_alarm))
        EditableTextSavable(
            valueProperty = descriptionProperty,
            modifier = Modifier.padding(horizontal = 20.dp),
            modifierText = Modifier.height(200.dp)
        )
    }

}