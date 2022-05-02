package com.nullpointer.noursecompose.ui.navigation.types

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.ramcosta.composedestinations.navargs.NavTypeSerializer
import com.ramcosta.composedestinations.navargs.parcelable.ParcelableNavTypeSerializer
import kotlinx.parcelize.Parcelize


@Parcelize
data class ArgsAlarms(
    val isLost: Boolean,
    val listIdAlarm: List<Long>,
) : Parcelable
@NavTypeSerializer
class ArgsAlarmsTypeSerializer : ParcelableNavTypeSerializer<ArgsAlarms> {

    companion object{
        fun generateRoute(isLost: Boolean,listAlarms:List<Alarm>): String {
            val listIds=listAlarms.map { it.id!! }
            val args=ArgsAlarms(isLost,listIds)
            return ArgsAlarmsTypeSerializer().toRouteString(args)
        }
    }

    override fun toRouteString(value: ArgsAlarms): String {
        val gson=Gson()
        val array=gson.toJson(value.listIdAlarm)
        return "${value.isLost};${array}"
    }


    override fun fromRouteString(routeStr: String, jClass: Class<out ArgsAlarms>): ArgsAlarms {
        val things = routeStr.split(";")
        val gson = GsonBuilder().create()
        val listAlarm = gson.fromJson<List<Long>>(things[1], object :TypeToken<List<Long>>(){}.type)
        val typeAlarm=things[0].toBoolean()
        return ArgsAlarms(typeAlarm,listAlarm)
    }
}