package org.kaorun.kadai.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val details: String? = null,
    val createdAtTimestamp: Long = System.currentTimeMillis(),
    val modifiedAtTimestamp: Long? = null,
    val dueTimestamp: Long? = null,
    val isCompleted: Boolean = false
)