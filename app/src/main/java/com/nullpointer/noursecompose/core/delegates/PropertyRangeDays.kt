package com.nullpointer.noursecompose.core.delegates

import androidx.lifecycle.SavedStateHandle

class PropertyRangeDays(
    state: SavedStateHandle,
    val errorRange: Int = DEFAULT_RESOURCE,
) {
    companion object {
        const val DEFAULT_RESOURCE = -1
    }

    private var currentRange by SavableComposeState(state, "", Pair(0L, 0L))
    val value get() = currentRange
    val hasError get() = errorRange != DEFAULT_RESOURCE

    fun changeCurrent(newRange: Pair<Long, Long>) {
        currentRange = newRange
    }

}