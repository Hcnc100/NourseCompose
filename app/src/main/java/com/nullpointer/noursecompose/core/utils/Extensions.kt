package com.nullpointer.noursecompose.core.utils

import android.app.Activity
import android.app.KeyguardManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.net.Uri
import android.os.Build
import android.text.format.DateFormat
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.PluralsRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImagePainter
import com.google.android.material.timepicker.TimeFormat
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


fun Context.getPlural(@PluralsRes stringQuality: Int,quality:Int): String {
    return resources.getQuantityString(stringQuality,quality,quality)
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




fun Context.getTimeString(@PluralsRes resource: Int, value: Number): String {
    return this.resources.getQuantityString(resource, value.toInt(), value.toInt())
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

@Composable
inline fun <reified VM : ViewModel> shareViewModel():VM {
    val activity = LocalContext.current as ComponentActivity
    return hiltViewModel(activity)
}

fun Context.showToast(@StringRes resString: Int) {
    Toast.makeText(this, getString(resString), Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message:String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.getNotificationManager(): NotificationManager {
    return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
}

val Context.correctFlag: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }


val AsyncImagePainter.isSuccess get() = state is AsyncImagePainter.State.Success

@Composable
fun getGrayColor(): Color {
    return (if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray)
}

fun Modifier.myShimmer(
    shimmer: Shimmer,
): Modifier = composed {
    shimmer(shimmer).background(getGrayColor())
}

fun Context.getFormatTime(): Int {
    return if (DateFormat.is24HourFormat(this)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
}

fun Context.getUriRaw(
    @RawRes sound: Int
): Uri {
    return Uri.parse("android.resource://$packageName/$sound")
}

