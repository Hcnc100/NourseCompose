package com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.ui.screen.addAlarm.ContentPage
import com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.viewModel.NameAndImgViewModel
import com.nullpointer.noursecompose.ui.share.ButtonSheetContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class, ExperimentalPagerApi::class)
@Composable
fun NameAndImgScreen(
    nameAndImgViewModel: NameAndImgViewModel,
    modalState: ModalBottomSheetState,
    focusManager: FocusManager,
    fileName:String?
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val bringName = remember { BringIntoViewRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val boxContainer: @Composable () -> Unit = remember {
        {
            Box(contentAlignment = Alignment.Center) {
                ImageAlarm(
                    modifier = Modifier.size(150.dp),
                    contentDescription = stringResource(R.string.description_img_alarm),
                    fileImg = nameAndImgViewModel.fileImg,
                    bitmap = fileName?.let { ImageUtils.loadImageFromStorage(it, LocalContext.current) }
                )
                FloatingActionButton(
                    onClick = {
                        focusManager.clearFocus()
                        scope.launch {
                            modalState.show()
                        }
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(painterResource(id = R.drawable.ic_edit),
                        stringResource(R.string.description_change_img_alarm))
                }
            }
        }
    }

    val textContainer: @Composable (modifier: Modifier) -> Unit = remember {
        { modifier ->
            Box(modifier) {
                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .onFocusEvent {
                                if (it.isFocused) {
                                    scope.launch {
                                        delay(500)
                                        bringName.bringIntoView()
                                    }
                                }
                            }
                            .width(250.dp),
                        value = nameAndImgViewModel.nameAlarm,
                        onValueChange = nameAndImgViewModel::changeName,
                        isError = nameAndImgViewModel.hasErrorName,
                        label = { Text(text = stringResource(R.string.hint_name_alarm)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
                    )
                    Text(style = MaterialTheme.typography.caption,
                        modifier = Modifier.align(Alignment.End),
                        color = if (nameAndImgViewModel.hasErrorName) MaterialTheme.colors.error else Color.Unspecified,
                        text = if (nameAndImgViewModel.hasErrorName)
                            stringResource(id = nameAndImgViewModel.errorName) else nameAndImgViewModel.counterName)
                }
            }
        }
    }

    ModalBottomSheetLayout(sheetState = modalState,
        sheetContent = {
            if (modalState.isVisible) {
                // * bottom sheet really
                ButtonSheetContent(
                    scope = scope,
                    sheetState = modalState,
                    actionBeforeSelect = { uri ->
                        scope.launch { modalState.hide() }
                        uri?.let { nameAndImgViewModel.changeFileImg(it, context) }
                    }
                )
            } else {
                // * fake bottom sheet
                //  ! is important for consistency animations
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp))
            }
        }) {
        ContentPage(title = stringResource(R.string.title_change_name)) {

            when (LocalConfiguration.current.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround) {
                        boxContainer()
                        Spacer(modifier = Modifier.height(40.dp))
                        textContainer(modifier = Modifier
                            .bringIntoViewRequester(bringName)
                            .padding(bottom = 80.dp))
                    }
                }
                else -> {
                    Row(modifier = Modifier
                        .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        boxContainer()
                        textContainer(modifier = Modifier.bringIntoViewRequester(bringName))
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImageAlarm(
    modifier: Modifier = Modifier,
    fileImg: File? = null,
    bitmap: Bitmap? = null,
    urlImg: String? = null,
    showProgress: Boolean = false,
    contentDescription: String,
    contentScale: ContentScale = ContentScale.FillBounds,
) {
    val painter = rememberImagePainter(
        // * if pass file img so ,load first,
        // * else load urlImg if this is not empty
        // * else load default
        data = when {
            bitmap != null -> bitmap
            fileImg != null -> fileImg
            urlImg != null -> urlImg
            else -> R.drawable.ic_image
        }
    ) {
        placeholder(R.drawable.ic_image)
        crossfade(true)
    }
    val state = painter.state
    Box(modifier = modifier
        .clip(RoundedCornerShape(10.dp))
        .background(
            when {
                fileImg != null || bitmap != null -> Color.Transparent
                isSystemInDarkTheme() -> Color.DarkGray
                else -> Color.LightGray
            }
        ), contentAlignment = Alignment.Center) {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = when (state) {
                is ImagePainter.State.Error -> painterResource(id = R.drawable.ic_broken_image)
                else -> painter
            },
            contentDescription = contentDescription,
            contentScale = contentScale)
    }

    if (state is ImagePainter.State.Loading && showProgress) CircularProgressIndicator()
}



