package com.nullpointer.noursecompose.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nullpointer.noursecompose.core.delegates.SavableComposeState
import com.nullpointer.noursecompose.models.ItemSelected
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectionViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
) : ViewModel() {
    companion object {
        private const val KEY_LIST_SELECTION = "KEY_LIST_SELECTION"
    }

    private var listIdsItemsSelected: List<Long> by SavableComposeState(
        stateHandle,
        KEY_LIST_SELECTION,
        emptyList())

    private val listItemSelected = mutableListOf<ItemSelected>()

    val isSelectedEnable get() = listIdsItemsSelected.isNotEmpty()
    val numberSelection get() = listIdsItemsSelected.size

    fun changeItemSelected(item: ItemSelected) {
        listIdsItemsSelected = if (listIdsItemsSelected.contains(item.id)) {
            item.isSelected= false
            listItemSelected.remove(item)
            listIdsItemsSelected - item.id
        } else {
            item.isSelected= true
            listItemSelected.add(item)
            listIdsItemsSelected + item.id
        }
    }

    fun reselectedItemSelected(listItems: List<ItemSelected>) {
        listItems.filter { listIdsItemsSelected.contains(it.id) }
            .onEach { it.isSelected = true }.let {
                listItemSelected.addAll(it)
            }
    }

    fun getListSelectionAndClear(): List<Long> {
        val listIdMeasure = listOf(*listIdsItemsSelected.toTypedArray())
        clearSelection()
        return listIdMeasure
    }

    fun clearSelection() {
        listItemSelected.forEach { it.isSelected = false }
        listItemSelected.clear()
        listIdsItemsSelected = emptyList()
    }
}