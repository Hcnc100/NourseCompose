package com.nullpointer.noursecompose.ui.navigation.types

import android.os.Parcelable
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nullpointer.noursecompose.models.AlarmDeserializer
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.ramcosta.composedestinations.navargs.DestinationsNavTypeSerializer
import com.ramcosta.composedestinations.navargs.NavTypeSerializer
import kotlinx.parcelize.Parcelize


@Parcelize
data class ArgsAlarms(
    val isLost: Boolean,
    val listIdAlarm: List<Alarm>,
) : Parcelable


@NavTypeSerializer
class ArgsAlarmsTypeSerializer : DestinationsNavTypeSerializer<ArgsAlarms> {

    private val gson= GsonBuilder().registerTypeAdapter(
        Alarm::class.java, AlarmDeserializer()
    ).create()

    override fun toRouteString(value: ArgsAlarms): String {
        val array = gson.toJson(value.listIdAlarm)
        return "${value.isLost};${array}"
    }

    override fun fromRouteString(routeStr: String): ArgsAlarms {
        val things = routeStr.split(";")
        val typeAlarm = things[0].toBoolean()
        val listAlarm = gson.fromJson<List<Alarm>>(things[1], object : TypeToken<List<Alarm>>() {}.type)
        return ArgsAlarms(typeAlarm, listAlarm)
    }
}