package com.nullpointer.noursecompose.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nullpointer.noursecompose.data.local.room.daos.AlarmDAO
import com.nullpointer.noursecompose.data.local.room.daos.MeasureDAO
import com.nullpointer.noursecompose.data.local.room.daos.LogsDAO
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.measure.SimpleMeasure
import com.nullpointer.noursecompose.models.registry.Log

@Database(
    entities = [Alarm::class, SimpleMeasure::class,Log::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converts::class)
abstract class NurseDatabase: RoomDatabase() {
    abstract fun getAlarmDAO(): AlarmDAO
    abstract fun getMeasureDAO(): MeasureDAO
    abstract fun getLogDao(): LogsDAO
}