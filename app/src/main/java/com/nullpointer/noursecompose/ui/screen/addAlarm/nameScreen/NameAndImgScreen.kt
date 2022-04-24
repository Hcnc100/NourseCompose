package com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.ui.screen.addAlarm.ContentPage
import com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.viewModel.NameAndImgViewModel
import com.nullpointer.noursecompose.ui.share.ButtonSheetContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class)
@Composable
fun NameAndImgScreen(
    nameAndImgViewModel: NameAndImgViewModel,
    modalState: ModalBottomSheetState,
    focusManager: FocusManager
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val bringName = remember { BringIntoViewRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

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
            Column(modifier = Modifier
                .fillMaxSize()) {
                Box(modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)) {
                    ImageAlarm(
                        modifier = Modifier.fillMaxWidth(),
                        contentDescription = stringResource(R.string.description_img_alarm),
                        fileImg = nameAndImgViewModel.fileImg
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


                Box(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .bringIntoViewRequester(bringName)
                    .padding(vertical = 80.dp)){
                    Column{
                        OutlinedTextField(
                            modifier = Modifier.onFocusEvent {
                                if ( it.isFocused) {
                                    scope.launch {
                                        delay(500)
                                        bringName.bringIntoView()
                                    }
                                }
                            },
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
    }

}

@Composable
fun ImageAlarm(
    modifier: Modifier = Modifier,
    fileImg: File? = null,
    bitmap: Bitmap? = null,
    urlImg:String?=null,
    showProgress: Boolean = false,
    contentDescription: String,
    contentScale:ContentScale = ContentScale.FillBounds
) {
    val painter = rememberImagePainter(
        // * if pass file img so ,load first,
        // * else load urlImg if this is not empty
        // * else load default
        data = when {
            fileImg != null -> fileImg
            bitmap != null -> bitmap
            urlImg!=null ->urlImg
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



