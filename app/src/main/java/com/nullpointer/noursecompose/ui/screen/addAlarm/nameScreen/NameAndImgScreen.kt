package com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.delegates.PropertySavableImg
import com.nullpointer.noursecompose.core.delegates.PropertySavableString
import com.nullpointer.noursecompose.core.utils.isSuccess
import com.nullpointer.noursecompose.ui.screen.addAlarm.TitleAddAlarm
import com.nullpointer.noursecompose.ui.share.EditableTextSavable

@Composable
fun NameAndImgScreen(
    actionNext: () -> Unit,
    showModalSelectImg: () -> Unit,
    modifier: Modifier = Modifier,
    imageAlarmProperty: PropertySavableImg,
    nameAlarmProperty: PropertySavableString,
    config: Configuration = LocalConfiguration.current,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TitleAddAlarm(title = stringResource(R.string.title_img_and_name_alarm))
        when (config.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ImageAlarm(
                        imageProperty = imageAlarmProperty,
                        actionEdit = showModalSelectImg,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(150.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    EditableTextSavable(
                        singleLine = true,
                        valueProperty = nameAlarmProperty,
                        keyboardActions = KeyboardActions(onDone = { actionNext() }),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImageAlarm(
                        imageProperty = imageAlarmProperty,
                        actionEdit = showModalSelectImg,
                        modifier = Modifier.size(200.dp)
                    )
                    EditableTextSavable(
                        singleLine = true,
                        modifier = Modifier,
                        valueProperty = nameAlarmProperty,
                        keyboardActions = KeyboardActions(onDone = { actionNext() }),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                    )
                }
            }
        }
    }

}

@Composable
private fun ImageAlarm(
    imageProperty: PropertySavableImg,
    modifier: Modifier = Modifier,
    actionEdit: () -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageProperty.value)
            .crossfade(true)
            .build(),
        placeholder = painterResource(id = R.drawable.ic_image),
        error = painterResource(id = R.drawable.ic_broken_image)
    )
    Card(modifier = modifier, shape = RoundedCornerShape(10.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = if (imageProperty.isNotEmpty) painter else painterResource(id = R.drawable.ic_image),
                contentDescription = stringResource(id = R.string.description_img_alarm),
                contentScale = if (painter.isSuccess) ContentScale.Crop else ContentScale.Fit
            )
            FloatingActionButton(
                onClick = actionEdit, modifier = Modifier
                    .padding(10.dp)
                    .size(35.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = stringResource(id = R.string.description_img_gallery)
                )
            }
        }
    }


}