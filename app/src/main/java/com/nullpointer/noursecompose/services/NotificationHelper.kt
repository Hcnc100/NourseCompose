package com.nullpointer.noursecompose.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.core.utils.toFormat
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.services.SoundServices.Companion.KEY_ALARM_PASS
import com.nullpointer.noursecompose.services.SoundServices.Companion.KEY_ALARM_PASS_ACTIVITY
import com.nullpointer.noursecompose.ui.activitys.AlarmScreen
import com.nullpointer.noursecompose.ui.activitys.MainActivity

class NotificationHelper(context: Context) : ContextWrapper(context) {
    companion object {
        const val CHANNEL_ID_ALARM = "Recordatorios"
        const val DESCRIPTION_CHANNEL_ALARM = "Canal donde se muestran los recordatorios"
        const val REQUEST_CODE_NOTIFICATION_ALARM = 1292

        const val CHANNEL_ID_LOST = "Recordatorios perdidos"
        const val DESCRIPTION_CHANNEL_LOST = "Canal donde se muestran los recordatorios perdidos"
        const val REQUEST_CODE_NOTIFICATION_LOST = 1293
        const val ID_GROUP_LOST = "ID_GROUP_LOST"
    }

    private val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


    @SuppressLint("InlinedApi")
    fun getNotificationAlarm(
        alarm: Alarm,
        useFullScreen: Boolean = true,
    ): Notification {
        // * create intent and put alarm as argument
        val intent = Intent(applicationContext, AlarmScreen::class.java).apply {
            putExtra(KEY_ALARM_PASS_ACTIVITY, alarm)
        }
        // * create pending intent
        val pendingIntent = PendingIntent.getActivity(
            this,
            REQUEST_CODE_NOTIFICATION_ALARM,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        // * create notification
        val notificationBuilder = getNotificationBuilderAlarm(alarm).apply {
            if (useFullScreen) setFullScreenIntent(pendingIntent, true)
        }
        // * create notification channel
        createNotificationChannelAlarm()

        return notificationBuilder.build()

    }


    private fun getPendingIntentCompose(idAlarm: String): PendingIntent {
        // * create deep link
        // * this go to post for notification
        val deepLinkIntent = Intent(Intent.ACTION_VIEW,
            "https://www.nourse-compose.com/alarm/$idAlarm".toUri(),
            this,
            MainActivity::class.java)
        // * create pending intent compose
        val deepLinkPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        return deepLinkPendingIntent
    }


    fun showNotificationLost(
        message: String,
        bitmap: Bitmap?,
        instruction: String,
        timeAlarmLost: Long,
    ) {
        val intent = Intent(this, AlarmScreen::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                REQUEST_CODE_NOTIFICATION_LOST,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        val notificationBuilder =
            getNotificationBuilderLost(message, pendingIntent, bitmap, instruction, timeAlarmLost)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannelLost()
        notificationManager.notify(
            REQUEST_CODE_NOTIFICATION_LOST,
            notificationBuilder.build()
        )
    }

    private fun getNotificationBuilderLost(
        nameMedicine: String,
        pendingIntent: PendingIntent,
        bitmap: Bitmap?,
        instruction: String,
        timeAlarmLost: Long,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID_ALARM)
            .setSmallIcon(R.drawable.ic_salud)
            .setContentTitle("Recordatorio Perdido ${timeAlarmLost.toFormat(this)}")
            .setContentText(nameMedicine)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setGroup(ID_GROUP_LOST)
            .apply {
                if (bitmap != null) {
                    setStyle(
                        NotificationCompat.BigPictureStyle(this)
                            .bigPicture(bitmap)
                            .setSummaryText("Recordatorio de medicamento")
                    )
                } else if (instruction.isNotEmpty()) {
                    setStyle(
                        NotificationCompat.BigTextStyle(this)
                            .bigText(instruction)
                            .setBigContentTitle("Es hora de tomar $nameMedicine")
                            .setSummaryText("Recordatorio de medicamento")
                    )
                }
            }
    }



    private fun getNotificationBuilderAlarm(
        alarm: Alarm,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID_ALARM)
            .setSmallIcon(R.drawable.ic_salud)
            .setContentTitle(getString(R.string.title_notify_alarm))
            .setContentText(alarm.title)
            .setAutoCancel(false)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
    }

    private fun createNotificationChannelLost() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_ID_LOST
            val descriptionText = DESCRIPTION_CHANNEL_LOST
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID_LOST, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannelAlarm() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_ID_ALARM
            val descriptionText = DESCRIPTION_CHANNEL_ALARM
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID_ALARM, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
