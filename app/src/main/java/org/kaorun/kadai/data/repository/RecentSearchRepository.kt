package org.kaorun.kadai.data.repository

import kotlinx.coroutines.flow.Flow
import org.kaorun.kadai.data.dao.RecentSearchDao
import org.kaorun.kadai.data.entity.RecentSearch

class RecentSearchRepository(private val recentSearchDao: RecentSearchDao) {
    fun getAll(query: String = "", limit: Int = 10): Flow<List<String>> {
        val trimmed = query.trim()
        val escaped = trimmed.escapeLikeWildcards()
        return recentSearchDao.getAll(filter = escaped, limit = limit)
    }

    suspend fun save(query: String) {
        val trimmed = query.trim()
        if (trimmed.isNotBlank()) {
            recentSearchDao.insert(
                RecentSearch(
                    query = trimmed,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun delete(query: String) =
        recentSearchDao.delete(query)
}

private fun String.escapeLikeWildcards(): String =
    replace("\\", "\\\\")
        .replace("%", "\\%")
        .replace("_", "\\_")
