package com.nullpointer.noursecompose.models.registry

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registry_table")
data class Registry(
    val type: TypeRegistry,
    val idAlarm: Long,
    val timestamp: Long = System.currentTimeMillis(),
){
    @PrimaryKey(autoGenerate = true)
     var id: Long = 0
}
