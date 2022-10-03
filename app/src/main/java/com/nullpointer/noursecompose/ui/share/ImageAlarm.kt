package com.nullpointer.noursecompose.ui.share

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.getGrayColor
import com.nullpointer.noursecompose.core.utils.isSuccess

@Composable
fun ImageAlarm(
    data: Any?,
    modifier: Modifier = Modifier,
    @DrawableRes
    imageDefault: Int = R.drawable.ic_image,
    context: Context = LocalContext.current
) {
    val painter = rememberAsyncImagePainter(
        placeholder = painterResource(id = imageDefault),
        error = painterResource(id = R.drawable.ic_broken_image),
        model = ImageRequest.Builder(context).data(data).scale(Scale.FILL).crossfade(true).build()
    )
    Image(
        modifier = modifier.fillMaxSize(),
        painter = if (data == null) painterResource(id = imageDefault) else painter,
        contentScale = if (painter.isSuccess) ContentScale.Crop else ContentScale.Fit,
        contentDescription = stringResource(id = R.string.description_img_alarm),
        colorFilter = if (painter.isSuccess) null else ColorFilter.tint(getGrayColor()),
    )
}

