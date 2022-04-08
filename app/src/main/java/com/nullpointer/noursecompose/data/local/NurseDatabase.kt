package com.nullpointer.noursecompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nullpointer.noursecompose.data.local.daos.AlarmDAO
import com.nullpointer.noursecompose.data.local.daos.MeasureDAO
import com.nullpointer.noursecompose.data.local.daos.RegistryDAO
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.models.registry.Registry

@Database(
    entities = [Alarm::class, SimpleMeasure::class,Registry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converts::class)
abstract class NurseDatabase: RoomDatabase() {
    abstract fun getAlarmDAO(): AlarmDAO
    abstract fun getMeasureDAO(): MeasureDAO
    abstract fun getRegistryDao():RegistryDAO
}