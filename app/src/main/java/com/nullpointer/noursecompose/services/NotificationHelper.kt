package com.nullpointer.noursecompose.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.toFormat
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.services.SoundServices.Companion.KEY_ALARM_PASS_ACTIVITY
import com.nullpointer.noursecompose.services.SoundServices.Companion.KEY_STOP_SOUND
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
        addActions: Boolean = true,
    ): Notification {
        // * create intent and put alarm as argument
        val intent = Intent(applicationContext, AlarmScreen::class.java).apply {
            putExtra(KEY_ALARM_PASS_ACTIVITY, alarm)
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_NO_USER_ACTION)
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
            if (addActions) {
                val stopIntent = Intent(applicationContext, SoundServices::class.java).apply {
                    action = KEY_STOP_SOUND
                }
                val stopPendingIntent = PendingIntent.getService(
                    this@NotificationHelper,
                    REQUEST_CODE_NOTIFICATION_ALARM,
                    stopIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                addAction(R.drawable.ic_stop, "Descartar", stopPendingIntent)
            }
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


    fun showNotificationLost(alarm: Alarm) {
        val intent = Intent(this, MainActivity::class.java)
        val randomRequestCode = "${alarm.id}${(1..999).random()}".toInt()
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                REQUEST_CODE_NOTIFICATION_LOST,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        val notificationBuilder = getNotificationBuilderLost(
            titleAlarm = "Recordatorio Perdido ${alarm.nextAlarm?.toFormat(this)}",
            message = alarm.title
        ).apply {
            setContentIntent(pendingIntent)
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannelLost()
        notificationManager.notify(
            randomRequestCode,
            notificationBuilder.build()
        )
    }

    fun showNotificationLost(listAlarms: List<Alarm>) {
        val rangeRandom = (123..9999)
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            REQUEST_CODE_NOTIFICATION_LOST,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        val fistNotify = getNotificationBuilderLost("Hay alarmas perdidas",
            titleAlarm = "${listAlarms.size} alarmas perdidas",
            true)
        notificationManager.notify(
            rangeRandom.random(),
            fistNotify.build()
        )
        listAlarms.forEach { alarm ->
            getNotificationBuilderLost(
                "Recordatorio Perdido ${alarm.nextAlarm?.toFormat(this)}",
                alarm.title
            ).apply {
                setContentIntent(pendingIntent)
            }.let { builder ->
                createNotificationChannelLost()
                notificationManager.notify(
                    rangeRandom.random(),
                    builder.build()
                )
            }
        }
    }

    private fun getNotificationBuilderLost(
        message: String,
        titleAlarm: String,
        isFirst: Boolean = false,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID_ALARM)
            .setSmallIcon(R.drawable.ic_salud)
            .setContentTitle(titleAlarm)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setGroup(ID_GROUP_LOST)
            .setGroupSummary(isFirst)
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
