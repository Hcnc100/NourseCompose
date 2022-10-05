package com.nullpointer.noursecompose.ui.states

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.core.util.toAndroidXPair
import androidx.core.util.toKotlinPair
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.getFormatTime
import com.nullpointer.noursecompose.core.utils.getHourAndMinutesToday
import com.nullpointer.noursecompose.core.utils.setHourAndMinutesToday
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.core.util.Pair as AndroidPair

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
class AddAlarmScreenState(
    context: Context,
    val pagerState: PagerState,
    scaffoldState: ScaffoldState,
    private val focusManager: FocusManager,
    private val coroutineScope: CoroutineScope,
    val modalBottomSheetState: ModalBottomSheetState,
    private val actionUpdatePickerTime: (Long) -> Unit,
    private val actionUpdateDateRange: (Pair<Long, Long>) -> Unit,
    private val timeInitAlarm: Long,
    private val rangeInitAlarm: Pair<Long, Long>,
) : SimpleScreenState(scaffoldState, context) {

    companion object {
        private const val KEY_TIME_PICKER = "KEY_TIME_PICKER"
        private const val KEY_DATE_RANGE_PICKER = "KEY_DATE_RANGE_PICKER"
    }

    val currentPage get() = pagerState.currentPage
    val isShowModal get() = modalBottomSheetState.isVisible
    private val timePicker = createTimePicker()
    private val dateRangePicker = createDateRangePicker()
    private val fragmentManager get() = (context as AppCompatActivity).supportFragmentManager


    private fun hiddenKeyBoard() = focusManager.clearFocus()

    fun changeVisibleModal(newValue: Boolean) = coroutineScope.launch {
        hiddenKeyBoard()
        if (newValue) modalBottomSheetState.show() else modalBottomSheetState.hide()
    }

    fun nextPage() {
        coroutineScope.launch {
            hiddenKeyBoard()
            pagerState.scrollToPage(pagerState.currentPage + 1)
        }
    }

    fun previousPage() {
        coroutineScope.launch {
            hiddenKeyBoard()
            pagerState.scrollToPage(pagerState.currentPage - 1)
        }
    }

    fun showTimePicker() {
        if (!timePicker.isAdded) timePicker.show(fragmentManager, KEY_TIME_PICKER)
    }

    fun showDateRangePicker() {
        if (!dateRangePicker.isAdded) dateRangePicker.show(fragmentManager, KEY_DATE_RANGE_PICKER)
    }

    private fun createDateRangePicker(): MaterialDatePicker<AndroidPair<Long, Long>> {
        val pickerSaved = fragmentManager.findFragmentByTag(
            KEY_DATE_RANGE_PICKER
        ) as? MaterialDatePicker<AndroidPair<Long, Long>>
        return (pickerSaved ?: createNewDateRangePicker()).also { safePickerTime ->
            with(safePickerTime) {
                addOnPositiveButtonClickListener {
                    actionUpdateDateRange(it.toKotlinPair())
                }
            }
        }
    }

    private fun createNewDateRangePicker(): MaterialDatePicker<AndroidPair<Long, Long>> {
        return MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText(context.getString(R.string.title_select_range_alarm)).apply {
                setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now()).build()
                )
                setSelection(rangeInitAlarm.toAndroidXPair())
            }.build()
    }


    private fun createTimePicker(): MaterialTimePicker {
        val pickerSaved = fragmentManager.findFragmentByTag(KEY_TIME_PICKER) as? MaterialTimePicker
        return (pickerSaved ?: createNewTimePicker()).also { safePickerTime ->
            with(safePickerTime) {
                addOnPositiveButtonClickListener {
                    actionUpdatePickerTime(setHourAndMinutesToday(hour, minute))
                }
            }
        }
    }

    private fun createNewTimePicker(): MaterialTimePicker {
        val (hour, minutes) = getHourAndMinutesToday(timeInitAlarm)
        return MaterialTimePicker.Builder()
            .setTitleText(context.getString(R.string.title_hour_of_alarm))
            .setTimeFormat(context.getFormatTime())
            .setHour(hour)
            .setMinute(minutes)
            .build()
    }

}


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun rememberAddAlarmScreenState(
    actionUpdatePickerTime: (Long) -> Unit,
    actionUpdateDateRange: (Pair<Long, Long>) -> Unit,
    context: Context = LocalContext.current,
    pagerState: PagerState = rememberPagerState(),
    focusManager: FocusManager = LocalFocusManager.current,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    modalBottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    ),
    timeInitAlarm: Long,
    rangeInitAlarm: Pair<Long, Long>,
)= remember(pagerState,scaffoldState,modalBottomSheetState,coroutineScope) {
    AddAlarmScreenState(
        context = context,
        pagerState = pagerState,
        focusManager = focusManager,
        coroutineScope = coroutineScope,
        scaffoldState = scaffoldState,
        actionUpdateDateRange = actionUpdateDateRange,
        modalBottomSheetState = modalBottomSheetState,
        actionUpdatePickerTime = actionUpdatePickerTime,
        timeInitAlarm = timeInitAlarm,
        rangeInitAlarm = rangeInitAlarm
    )
}