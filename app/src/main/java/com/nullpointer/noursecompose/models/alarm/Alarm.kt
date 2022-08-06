package com.nullpointer.noursecompose.models.alarm

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.nullpointer.noursecompose.models.ItemSelected
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.lang.Exception

@Parcelize
@Entity(tableName = "alarms_table")
data class Alarm(
    val name: String = "",
    val description: String = "",
    val typeAlarm: AlarmTypes = AlarmTypes.ONE_SHOT,
    val nextAlarm: Long? = null,
    val pathFile: String? = null,
    val repeaterEvery: Long? = null,
    val rangeInitAlarm: Long? = null,
    val rangeFinishAlarm: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    @PrimaryKey(autoGenerate = true)
    override val id: Long=0
): ItemSelected, Parcelable {

    @delegate:Expose
    @IgnoredOnParcel
    @delegate:Ignore
    override var isSelected by mutableStateOf(false)

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

    companion object{
        fun createAlarmRandom(): Alarm {
         return Alarm(name = "Hola")
        }
    }
}