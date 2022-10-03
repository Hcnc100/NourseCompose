package com.nullpointer.noursecompose.ui.screen.oxygen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.actions.ActionMeasureScreen
import com.nullpointer.noursecompose.actions.ActionMeasureScreen.*
import com.nullpointer.noursecompose.core.states.Resource
import com.nullpointer.noursecompose.core.utils.shareViewModel
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.presentation.MeasureViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.dialogs.DialogAddMeasure
import com.nullpointer.noursecompose.ui.navigation.HomeNavGraph
import com.nullpointer.noursecompose.ui.screen.measure.LoadingItemMeasure
import com.nullpointer.noursecompose.ui.screen.oxygen.components.ListEmptyOxygen
import com.nullpointer.noursecompose.ui.screen.oxygen.components.ListSuccessOxygen
import com.nullpointer.noursecompose.ui.share.ButtonToggleAddRemove
import com.nullpointer.noursecompose.ui.states.SelectedScreenState
import com.nullpointer.noursecompose.ui.states.rememberSelectedScreenState
import com.ramcosta.composedestinations.annotation.Destination

@HomeNavGraph
@Destination
@Composable
fun OxygenScreen(
    measureViewModel: MeasureViewModel = shareViewModel(),
    selectionViewModel: SelectionViewModel = shareViewModel(),
    measureScreenState: SelectedScreenState = rememberSelectedScreenState()
) {
    val listOxygenState by measureViewModel.listOxygen.collectAsState()
    var isShowDialog by rememberSaveable { mutableStateOf(false) }

    BackHandler(selectionViewModel.isSelectedEnable, selectionViewModel::clearSelection)

    OxygenScreen(
        listOxygen = listOxygenState,
        lazyGridState = measureScreenState.lazyGridState,
        isShowDialog = isShowDialog,
        isSelectedEnable = selectionViewModel.isSelectedEnable,
        isScrollInProgress = measureScreenState.isScrollInProgress,
        scaffoldState = measureScreenState.scaffoldState,
        actionOxygen = { action, value, measure ->
            when (action) {
                SHOW_ADD_DIALOG -> isShowDialog = true
                HIDE_ADD_DIALOG -> isShowDialog = false
                ADD_NEW_MEASURE -> {
                    value?.let {
                        measureViewModel.addNewMeasure(SimpleMeasure(value, MeasureType.OXYGEN))
                        measureScreenState.scrollToFirst()
                    }
                }
                DELETER_LIST_MEASURE -> {
                    measureViewModel.deleterListMeasure(
                        selectionViewModel.getListSelectionAndClear()
                    )
                }
                CHANGE_MEASURE_SELECT -> {
                    measure?.let { selectionViewModel.changeItemSelected(it) }
                }
            }
        }
    )
}

@Composable
private fun OxygenScreen(
    isShowDialog: Boolean,
    isSelectedEnable: Boolean,
    isScrollInProgress: Boolean,
    lazyGridState: LazyGridState,
    scaffoldState: ScaffoldState,
    listOxygen: Resource<List<SimpleMeasure>>,
    actionOxygen: (ActionMeasureScreen, Float?, SimpleMeasure?) -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            ButtonToggleAddRemove(
                isVisible = !isScrollInProgress,
                isSelectedEnable = isSelectedEnable,
                descriptionButtonAdd = stringResource(id = R.string.description_add_oxygen),
                actionAdd = { actionOxygen(SHOW_ADD_DIALOG, null, null) },
                descriptionButtonRemove = stringResource(id = R.string.description_remove_oxygen),
                actionRemove = { actionOxygen(DELETER_LIST_MEASURE, null, null) }
            )
        }
    ) { paddingValues ->
        ListTempState(
            listOxygen = listOxygen,
            lazyGridState = lazyGridState,
            isSelectedEnable = isSelectedEnable,
            modifier = Modifier.padding(paddingValues),
            changeSelect = { actionOxygen(CHANGE_MEASURE_SELECT, null, it as SimpleMeasure) }
        )
    }
    if (isShowDialog) {
        DialogAddMeasure(
            nameMeasure = stringResource(id = R.string.name_oxygen),
            measureFullSuffix = stringResource(id = R.string.suffix_oxygen),
            actionHiddenDialog = { actionOxygen(HIDE_ADD_DIALOG, null, null) },
            actionAdd = { actionOxygen(ADD_NEW_MEASURE, it, null) })
    }
}


@Composable
private fun ListTempState(
    isSelectedEnable: Boolean,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    changeSelect: (ItemSelected) -> Unit,
    listOxygen: Resource<List<SimpleMeasure>>
) {
    when (listOxygen) {
        is Resource.Success -> {
            if (listOxygen.data.isEmpty()) {
                ListEmptyOxygen(modifier = modifier)
            } else {
                ListSuccessOxygen(
                    modifier = modifier,
                    listState = lazyGridState,
                    listOxygen = listOxygen.data,
                    changeSelectState = changeSelect,
                    isSelectedEnable = isSelectedEnable
                )
            }
        }
        else -> LoadingItemMeasure(modifier = modifier)
    }
}