package com.nullpointer.noursecompose.ui.share

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.nullpointer.noursecompose.R

@Composable
fun ImageAlarm(
    path: String?,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(path).crossfade(true).build(),
        placeholder = painterResource(id = R.drawable.ic_image),
        error = painterResource(id = R.drawable.ic_broken_image)
    )
    Image(
        modifier = modifier,
        painter = if(path == null) painterResource(id = R.drawable.ic_image) else painter,
        contentScale = if (painter.state is AsyncImagePainter.State.Success) ContentScale.Crop else ContentScale.Fit,
        contentDescription = stringResource(id = R.string.description_img_alarm)
    )
}