package org.kaorun.kadai.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val details: String? = null,
    val timestamp: Long? = null,
    val isDone: Boolean = false
)
