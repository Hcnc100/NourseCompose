package com.nullpointer.noursecompose.ui.share

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.getGrayColor
import com.nullpointer.noursecompose.core.utils.isSuccess

@Composable
fun ImageAlarm(
    path: String?,
    modifier: Modifier = Modifier,
    @DrawableRes
    imageDefault: Int = R.drawable.ic_image,
    context: Context = LocalContext.current
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(path).crossfade(true).build(),
        placeholder = painterResource(id = imageDefault),
        error = painterResource(id = R.drawable.ic_broken_image)
    )
    Image(
        modifier = modifier,
        painter = if (path == null) painterResource(id = imageDefault) else painter,
        contentScale = if (painter.isSuccess) ContentScale.Crop else ContentScale.Fit,
        contentDescription = stringResource(id = R.string.description_img_alarm),
        colorFilter = if (painter.isSuccess) null else ColorFilter.tint(getGrayColor())
    )
}

@Composable
 fun AlarmCurrent(
    imgAlarm: String?,
    modifier: Modifier = Modifier
) {
    if (imgAlarm == null) {
        Icon(
            painter = painterResource(id = R.drawable.ic_alarm),
            contentDescription = stringResource(id = R.string.description_img_alarm),
            modifier = modifier.padding(5.dp)
        )
    } else {
        ImageAlarm(path = imgAlarm, modifier = modifier)
    }
}