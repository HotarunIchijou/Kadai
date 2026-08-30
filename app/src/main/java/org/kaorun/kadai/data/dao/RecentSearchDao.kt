package org.kaorun.kadai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.kaorun.kadai.data.entity.RecentSearch

@Dao
interface RecentSearchDao {
    @Query(
        """
        SELECT `query` FROM recent_searches
        WHERE (:filter = '' OR `query` LIKE '%' || :filter || '%' ESCAPE '\')
        ORDER BY timestamp DESC
        LIMIT :limit
        """
    )
    fun getAll(filter: String = "", limit: Int = 10): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentSearch: RecentSearch)

    @Query("DELETE FROM recent_searches WHERE `query` = :query")
    suspend fun delete(query: String)

    @Query("DELETE FROM recent_searches")
    suspend fun clearAll()
}