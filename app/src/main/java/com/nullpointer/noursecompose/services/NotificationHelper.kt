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
import com.nullpointer.noursecompose.ui.navigation.types.ArgsAlarmsTypeSerializer

class NotificationHelper(context: Context) : ContextWrapper(context) {
    companion object {
        const val CHANNEL_ID_ALARM = R.string.name_channel_alarms
        const val DESCRIPTION_CHANNEL_ALARM = R.string.description_channel_alarm
        const val REQUEST_CODE_NOTIFICATION_ALARM = 1292
        const val ID_GROUP_ALARM = "ID_GROUP_ALARM"

        const val CHANNEL_ID_LOST = R.string.name_channel_lost_alarm
        const val DESCRIPTION_CHANNEL_LOST = R.string.description_channel_lost_alarm
        const val ID_GROUP_LOST = "ID_GROUP_LOST"
    }

    private val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    private val rangeRandom = (123..9999)

    init {
        // * create notification channel
        createNotificationChannelAlarm()
        createNotificationChannelLost()
    }

    @SuppressLint("InlinedApi")
    fun getNotifyAlarmFromServices(
        alarm: Alarm,
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
        val notificationBuilder = getBaseNotification(
            title = getString(R.string.title_notify_alarm),
            message = alarm.message,
            autoCancel = false,
            channelId = getString(CHANNEL_ID_ALARM),
            priority = NotificationCompat.PRIORITY_MAX
        ).apply {
            // * add full screen intent
            setFullScreenIntent(pendingIntent, true)
            // * add action stop
            val stopIntent = Intent(applicationContext, SoundServices::class.java).apply {
                action = KEY_STOP_SOUND
            }
            val stopPendingIntent = PendingIntent.getService(
                this@NotificationHelper,
                REQUEST_CODE_NOTIFICATION_ALARM,
                stopIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            addAction(R.drawable.ic_stop,
                getString(R.string.name_action_stop_alarm),
                stopPendingIntent)
        }
        // ? return notify for use in foreground services
        return notificationBuilder.build()
    }

    @SuppressLint("InlinedApi")
    fun showNotifyAlarm(
        listAlarms: List<Alarm>,
    ) {
        if (listAlarms.size > 1) {
            val pendingIntent = getPendingIntentCompose(listAlarms, false)
            getBaseNotification(
                title = getString(R.string.title_group_alarm_pending),
                message = getString(R.string.message_group_alarm_pending, listAlarms.size),
                autoCancel = true,
                channelId = getString(CHANNEL_ID_ALARM),
                group = ID_GROUP_ALARM,
                isFirst = true
            ).apply {
                setContentIntent(pendingIntent)
            }.let { notify ->
                notificationManager.notify(
                    rangeRandom.random(),
                    notify.build()
                )
            }
        }

        listAlarms.forEach {
            val pendingIntent = getPendingIntentCompose(listOf(it), false)
            // * create notification
            getBaseNotification(
                title = getString(R.string.title_notify_alarm),
                message = it.title,
                autoCancel = true,
                channelId = getString(CHANNEL_ID_ALARM),
                group = ID_GROUP_ALARM
            ).apply {
                setContentIntent(pendingIntent)
            }.let { builder ->
                notificationManager.notify(
                    it.id!!.toInt(),
                    builder.build()
                )
            }
        }
    }

    @SuppressLint("InlinedApi")
    fun showNotificationLost(listAlarms: List<Alarm>) {
        if (listAlarms.size > 1) {
            val pendingIntent = getPendingIntentCompose(listAlarms, true)
            getBaseNotification(
                title = getString(R.string.title_group_lost_alarm),
                message = getString(R.string.message_group_lost_alarm_pending, listAlarms.size),
                autoCancel = false,
                channelId = getString(CHANNEL_ID_LOST),
                group = ID_GROUP_LOST,
                isFirst = true
            ).apply {
                setContentIntent(pendingIntent)
            }.let { notify ->
                notificationManager.notify(
                    rangeRandom.random(),
                    notify.build()
                )
            }
        }
        listAlarms.forEach { alarm ->
            val pendingIntent = getPendingIntentCompose(listOf(alarm), true)
            getBaseNotification(
                title = getString(R.string.title_notify_lost_alarm),
                message = alarm.title + "\n${alarm.nextAlarm?.toFormat(this)}",
                autoCancel = true,
                channelId = getString(CHANNEL_ID_LOST),
                group = ID_GROUP_LOST,
            ).apply {
                setContentIntent(pendingIntent)
            }.let { builder ->
                createNotificationChannelLost()
                notificationManager.notify(
                    alarm.id!!.toInt(),
                    builder.build()
                )
            }
        }
    }


    private fun getPendingIntentCompose(listAlarms: List<Alarm>, isLost: Boolean): PendingIntent {
        // * create deep link
        // * this go to post for notification
        val routeString = ArgsAlarmsTypeSerializer.generateRoute(isLost, listAlarms)
        val deepLinkIntent = Intent(Intent.ACTION_VIEW,
            "https://www.nourse-compose.com/alarm/$routeString".toUri(),
            this,
            MainActivity::class.java)
        // * create pending intent compose
        val deepLinkPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        return deepLinkPendingIntent
    }


    private fun getBaseNotification(
        title: String,
        message: String,
        autoCancel: Boolean,
        channelId: String,
        group: String? = null,
        isFirst: Boolean = false,
        priority: Int = NotificationCompat.PRIORITY_HIGH,
    ) = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.ic_salud)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(autoCancel)
        .setPriority(priority)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .setGroupSummary(isFirst).apply {
            group?.let {
                setGroup(group)
            }
        }


    private fun createNotificationChannelLost() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(getString(CHANNEL_ID_LOST),
                getString(CHANNEL_ID_LOST),
                importance
            ).apply {
                description = getString(DESCRIPTION_CHANNEL_LOST)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannelAlarm() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                getString(CHANNEL_ID_ALARM),
                getString(CHANNEL_ID_ALARM),
                importance
            ).apply {
                description = getString(DESCRIPTION_CHANNEL_ALARM)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

}
