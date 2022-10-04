package com.nullpointer.noursecompose.ui.share

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScaffoldModal(
    isVisibleModal: Boolean,
    actionHideModal: () -> Unit,
    callBackSelection: (Uri) -> Unit,
    sheetState: ModalBottomSheetState,
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topBar,
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