package com.nullpointer.noursecompose.data.local.room

import androidx.room.TypeConverter
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import com.nullpointer.noursecompose.models.measure.MeasureType
import com.nullpointer.noursecompose.models.registry.TypeRegistry

class Converts {

    @TypeConverter
    fun toAlarmType(type: String): AlarmTypes =
        AlarmTypes.valueOf(type)

    @TypeConverter
    fun fromAlarmType(alarmTypes: AlarmTypes): String = alarmTypes.name

    @TypeConverter
    fun toMeasureType(type: String): MeasureType =
        MeasureType.valueOf(type)

    @TypeConverter
    fun fromMeasureType(measureType: MeasureType): String = measureType.name

    @TypeConverter
    fun toRegistryType(type: String): TypeRegistry = TypeRegistry.myValueOf(type)

    @TypeConverter
    fun fromRegistryType(registry: TypeRegistry): String = registry.stringValue
}