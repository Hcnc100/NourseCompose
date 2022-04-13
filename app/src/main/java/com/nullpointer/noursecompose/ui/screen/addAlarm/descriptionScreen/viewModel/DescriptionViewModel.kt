package com.nullpointer.noursecompose.ui.screen.addAlarm.descriptionScreen.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.delegates.SavableComposeState
import com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.viewModel.NameAndImgViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DescriptionViewModel @Inject constructor(
    state: SavedStateHandle,
) : ViewModel() {
    companion object {
        private const val KEY_DESCRIPTION = "KEY_DESCRIPTION"
        private const val KEY_ERROR_DESCRIPTION = "KEY_ERROR_DESCRIPTION"
        const val MAX_LENGTH = 350
    }

    var description: String by SavableComposeState(state, KEY_DESCRIPTION, "")
        private set

    var errorDescription: Int by SavableComposeState(state, KEY_ERROR_DESCRIPTION, -1)
        private set

    val hasErrorDescription get() = errorDescription!=-1
    val counterDescription get() =  "${description.length} / $MAX_LENGTH"

    fun changeDescription(newDescription: String) {
        description = newDescription
        validateDescription()
    }

    fun validateDescription() {
        errorDescription = when {
            description.length > MAX_LENGTH -> R.string.error_description_long
            else -> -1
        }
    }
}