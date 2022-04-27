package com.nullpointer.noursecompose.core.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.format.DateFormat
import android.view.WindowManager
import androidx.annotation.PluralsRes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.app.KeyguardManager
import kotlinx.coroutines.Job


// for mutable list

//fun <T> MutableStateFlow<MutableList<T>>.plusAssign(item: T) {
//    val value = this.value
//    value.add(item)
//    this.value = value
//}
//fun <T> MutableStateFlow<MutableList<T>>.minusAssign(item: T) {
//    val value = this.value
//    value.remove(item)
//    this.value = value
//}

// for immutable list

operator fun <T> MutableStateFlow<List<T>>.plusAssign(item: T) {
    this.value += item
}

operator fun <T> MutableStateFlow<List<T>>.plusAssign(listItems: List<T>) {
    this.value += listItems
}

operator fun <T> MutableStateFlow<List<T>>.minusAssign(item: T) {
    this.value -= item
}

operator fun <T> MutableStateFlow<List<T>>.minusAssign(listItems: List<T>) {
    this.value -= listItems
}

fun Long.toFormatOnlyTime(context: Context): String {
    val newPattern = if (DateFormat.is24HourFormat(context)) {
        "hh:mm"
    } else {
        "hh:mm a"
    }
    val sdf = SimpleDateFormat(newPattern, Locale.getDefault())
    return sdf.format(this)
}

fun Long.toFormat(context: Context,includeSeconds:Boolean=false): String {
    val base=if(includeSeconds)  "EEEE dd/MM/yyyy hh:mm:ss" else  "EEEE dd/MM/yyyy hh:mm"
    val newPattern = if (DateFormat.is24HourFormat(context)) base else "$base a"
    val sdf = SimpleDateFormat(newPattern, Locale.getDefault())
    return sdf.format(this)
}

fun getTimeNow(): Long {
    return Calendar.getInstance().timeInMillis
}

fun getTimeNowToClock(): Long {
    return Calendar.getInstance().apply {
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

fun setHourAndMinutesToday(hour: Int, minute: Int, timeInMillis: Long? = null): Long {
    return Calendar.getInstance().apply {
        timeInMillis?.let {
            this.timeInMillis = it
        }
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

fun getHourAndMinutesToday(timeInMillis: Long? = null): Pair<Int, Int> {
    Calendar.getInstance().apply {
        timeInMillis?.let {
            this.timeInMillis = it
        }
        return Pair(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE))
    }
}

fun getFirstTimeDay(timeDayInMillis: Long? = null): Long {
    // * get calendar with time now in time zone default
    return Calendar.getInstance(TimeZone.getDefault()).also { calendarNow ->
        // * if the time pass is no null
        // * get first time of the day passes as parameter
        timeDayInMillis?.let {
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                timeInMillis = it
            }.also { calendarUTC: Calendar ->
                calendarNow.set(Calendar.DAY_OF_YEAR, calendarUTC.get(Calendar.DAY_OF_YEAR))
                calendarNow.set(Calendar.MONTH, calendarUTC.get(Calendar.MONTH))
                calendarNow.set(Calendar.YEAR, calendarUTC.get(Calendar.YEAR))
            }
        }

        // * if no passed args, only get the first time of today
        calendarNow.set(Calendar.HOUR_OF_DAY, 0)
        calendarNow.set(Calendar.MINUTE, 0)
        calendarNow.set(Calendar.SECOND, 0)
        calendarNow.set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}


fun File.toBitmap(): Bitmap {
    val filePath: String = this.path
    return BitmapFactory.decodeFile(filePath)
}

fun Context.getTimeString(@PluralsRes resource: Int, value: Number): String {
    return this.resources.getQuantityString(resource, value.toInt(), value.toInt())
}

fun Uri.toBitmap(context: Context): Bitmap? {
    if (this == Uri.EMPTY) return null
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(
            context.contentResolver,
            this
        )
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    }
}

fun BroadcastReceiver.myGoAsync(
    coroutineScope: CoroutineScope,
    dispatcher: CoroutineDispatcher,
    block: suspend () -> Unit,
) {
    val pendingResult = goAsync()
    coroutineScope.launch(dispatcher) {
        block()
        pendingResult.finish()
    }
}

// * if contains this element so return new list without this element,
// * else return new list with this element
fun <E> List<E>.toggleItem(item: E): List<E> {
    return if (contains(item)) this - item else this + item
}

fun Activity.turnScreenOnAndKeyguardOff() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
    } else {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )
    }

    with(getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestDismissKeyguard(this@turnScreenOnAndKeyguardOff, null)
        }
    }
}

fun Activity.turnScreenOffAndKeyguardOn() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(false)
        setTurnScreenOn(false)
    } else {
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )
    }
}
