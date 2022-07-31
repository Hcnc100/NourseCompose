package com.nullpointer.noursecompose.ui.navigation.types


//
//@Parcelize
//data class ArgsAlarms(
//    val isLost: Boolean,
//    val listIdAlarm: List<Long>,
//) : Parcelable
//@NavTypeSerializer
//class ArgsAlarmsTypeSerializer : ParcelableNavTypeSerializer<ArgsAlarms> {
//
//    companion object{
//        fun generateRoute(isLost: Boolean,listAlarms:List<Alarm>): String {
//            val listIds=listAlarms.map { it.id!! }
//            val args=ArgsAlarms(isLost,listIds)
//            return ArgsAlarmsTypeSerializer().toRouteString(args)
//        }
//    }
//
//    override fun toRouteString(value: ArgsAlarms): String {
//        val gson=Gson()
//        val array=gson.toJson(value.listIdAlarm)
//        return "${value.isLost};${array}"
//    }
//
//
//    override fun fromRouteString(routeStr: String, jClass: Class<out ArgsAlarms>): ArgsAlarms {
//        val things = routeStr.split(";")
//        val gson = GsonBuilder().create()
//        val listAlarm = gson.fromJson<List<Long>>(things[1], object :TypeToken<List<Long>>(){}.type)
//        val typeAlarm=things[0].toBoolean()
//        return ArgsAlarms(typeAlarm,listAlarm)
//    }
//}