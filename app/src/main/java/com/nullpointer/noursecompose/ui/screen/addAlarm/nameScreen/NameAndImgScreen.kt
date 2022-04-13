package com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.ui.screen.addAlarm.ContentPage
import com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.viewModel.NameAndImgViewModel
import com.nullpointer.noursecompose.ui.share.ButtonSheetContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NameAndImgScreen(
    nameAndImgViewModel: NameAndImgViewModel,
    modalState: ModalBottomSheetState,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

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
                ImageAlarmEdit(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.CenterHorizontally),
                    actionClick = {
                        focusManager.clearFocus()
                        scope.launch {
                            modalState.show()
                        }
                    })
                Spacer(modifier = Modifier.height(50.dp))
                Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    OutlinedTextField(
                        value = nameAndImgViewModel.nameAlarm,
                        onValueChange = nameAndImgViewModel::changeName,
                        label = { Text(text = stringResource(R.string.hint_name_alarm)) },
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

@Composable
fun ImageAlarmEdit(
    modifier: Modifier = Modifier,
    actionClick: () -> Unit,
) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = Color.LightGray,
            shape = RoundedCornerShape(10.dp),
        ) {
            Image(
                painter = rememberImagePainter(R.drawable.ic_image),
                contentDescription = stringResource(R.string.description_img_alarm),
            )
        }
        FloatingActionButton(
            onClick = actionClick,
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