package com.nullpointer.noursecompose.models.registry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.nullpointer.noursecompose.models.ItemSelected

@Entity(tableName = "logs_table")
data class Log(
    val type: TypeRegistry=TypeRegistry.CREATE,
    val idAlarm: Long=0,
    val timestamp: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    override val id: Long=0,
):ItemSelected{
    @delegate:Ignore
    override var isSelected by  mutableStateOf(false)
}
