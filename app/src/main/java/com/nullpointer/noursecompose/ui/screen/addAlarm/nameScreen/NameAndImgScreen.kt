package com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.ui.screen.addAlarm.viewModel.AddAlarmViewModel
import com.nullpointer.noursecompose.ui.share.EditableTextSavable
import com.nullpointer.noursecompose.ui.states.AddAlarmScreenState

@Composable
fun NameAndImgScreen(
    addAlarmViewModel: AddAlarmViewModel,
    addAlarmScreenState: AddAlarmScreenState,
    config: Configuration = LocalConfiguration.current,
) {
    when (config.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ImageAlarm(
                    imageFile = addAlarmViewModel.imageAlarm,
                    actionEdit = addAlarmScreenState::showModal,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(250.dp)
                )
                EditableTextSavable(valueProperty = addAlarmViewModel.nameAlarm, maxLines = 1)
            }
        }
        else -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(20.dp))
                ImageAlarm(
                    imageFile = addAlarmViewModel.imageAlarm,
                    actionEdit = addAlarmScreenState::showModal,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                        .size(200.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                EditableTextSavable(
                    valueProperty = addAlarmViewModel.nameAlarm,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    maxLines = 1
                )
                }
            }
        }


}

@Composable
private fun ImageAlarm(
    imageFile: String?,
    modifier: Modifier = Modifier,
    actionEdit: () -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(imageFile).crossfade(true).build(),
        placeholder = painterResource(id = R.drawable.ic_image),
        error = painterResource(id = R.drawable.ic_broken_image)
    )
    Card(modifier = modifier, shape = RoundedCornerShape(10.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = if (imageFile == null) painterResource(id = R.drawable.ic_image) else painter,
                modifier = Modifier.fillMaxSize(),
                contentDescription = stringResource(id = R.string.description_img_alarm),
                contentScale = if (painter.state is AsyncImagePainter.State.Success) ContentScale.Crop else ContentScale.Fit
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