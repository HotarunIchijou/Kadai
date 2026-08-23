package org.kaorun.kadai.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.kaorun.kadai.data.SortDirection
import org.kaorun.kadai.data.TaskSortBy
import org.kaorun.kadai.data.TaskSortConfig
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val PERMISSION_CARD_DISMISSED = booleanPreferencesKey("permission_card_dismissed")
        val SORT_BY = stringPreferencesKey("task_sort_by")
        val SORT_DIRECTION = stringPreferencesKey("task_sort_direction")
    }

    val isOnboardingCompleted: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
        }

    suspend fun setOnboardingCompleted(completed: Boolean = true) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    val isPermissionCardDismissed: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.PERMISSION_CARD_DISMISSED] ?: false
        }

    suspend fun setPermissionCardDismissed(dismissed: Boolean = true) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PERMISSION_CARD_DISMISSED] = dismissed
        }
    }

    val sortConfig: Flow<TaskSortConfig> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val sortBy = preferences[PreferencesKeys.SORT_BY]?.let { name ->
                TaskSortBy.entries.find { it.name == name }
            } ?: TaskSortBy.DATE_CREATED

            val direction = preferences[PreferencesKeys.SORT_DIRECTION]?.let { name ->
                SortDirection.entries.find { it.name == name }
            } ?: SortDirection.DESCENDING

            TaskSortConfig(sortBy = sortBy, direction = direction)
        }

    suspend fun updateSortConfig(config: TaskSortConfig) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_BY] = config.sortBy.name
            preferences[PreferencesKeys.SORT_DIRECTION] = config.direction.name
        }
    }
}