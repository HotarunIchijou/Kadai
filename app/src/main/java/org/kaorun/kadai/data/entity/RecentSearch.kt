package org.kaorun.kadai.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_searches")
data class RecentSearch(
    @PrimaryKey val query: String,
    val timestamp: Long = System.currentTimeMillis()
)