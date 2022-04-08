package com.nullpointer.noursecompose.models.alarm

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.lang.Exception

@Entity(tableName = "alarms_table")
data class Alarm(
    val title: String = "",
    val typeAlarm: AlarmTypes = AlarmTypes.ONE_SHOT,
    val nextAlarm: Long? = null,
    val message: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val nameFile: String? = null,
    val repeaterEvery: Long? = null,
    val rangeInitAlarm: Long? = null,
    val rangeFinishAlarm: Long? = null,
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = -1
    @Ignore
    var isSelect: Boolean = false

    fun updateTime(currentTime: Long): Alarm {
        return try {
            var newNextAlarm = nextAlarm!!
            while (newNextAlarm <= currentTime) {
                newNextAlarm += repeaterEvery!!
            }
            copy(nextAlarm = newNextAlarm)
        }catch (e: Exception){
            copy(isActive = false, nextAlarm = null)
        }
    }
}