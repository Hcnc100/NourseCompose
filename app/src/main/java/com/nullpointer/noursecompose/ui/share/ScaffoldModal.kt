package com.nullpointer.noursecompose.ui.share

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScaffoldModal(
    isVisibleModal: Boolean,
    actionHideModal: () -> Unit,
    modifier: Modifier = Modifier,
    callBackSelection: (Uri) -> Unit,
    sheetState: ModalBottomSheetState,
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topBar,
        modifier = modifier
    ) { padding ->
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                SelectImgButtonSheet(
                    isVisible = isVisibleModal,
                    actionHidden = actionHideModal
                ) { uri ->
                    actionHideModal()
                    uri?.let { callBackSelection(it) }
                }
            },
        ) {
            content(padding)
        }
    }
}