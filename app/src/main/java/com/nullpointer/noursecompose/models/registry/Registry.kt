package com.nullpointer.noursecompose.models.registry

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.nullpointer.noursecompose.models.ItemSelected

@Entity(tableName = "registry_table")
data class Registry(
    val type: TypeRegistry=TypeRegistry.CREATE,
    val idAlarm: Long=0,
    val timestamp: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    override val id: Long? = null,
):ItemSelected{
    @Ignore
    override var isSelected: Boolean = false
}
