package com.nullpointer.noursecompose.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nullpointer.noursecompose.core.delegates.SavableComposeState
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectionViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
) : ViewModel() {
    companion object {
        private const val KEY_LIST_SELECTION = "KEY_LIST_SELECTION"
    }

    var listMeasureSelected: List<ItemSelected> by SavableComposeState(
        stateHandle,
        KEY_LIST_SELECTION,
        emptyList())

    val isSelectedEnable get() = listMeasureSelected.isNotEmpty()
    val numberSelection get() = listMeasureSelected.size

    fun changeItemSelected(item: ItemSelected) {
        listMeasureSelected = if (listMeasureSelected.contains(item)) {
            item.isSelected = false
            listMeasureSelected - item
        } else {
            item.isSelected = true
            listMeasureSelected + item
        }
    }

    fun getListSelectionAndClear():List<Long>{
        val listIdMeasure=listMeasureSelected.map { it.id!! }
        clearSelection()
        return listIdMeasure
    }

    fun clearSelection() {
        listMeasureSelected.forEach { it.isSelected = false }
        listMeasureSelected = emptyList()
    }
}