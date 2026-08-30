package org.kaorun.kadai.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.kaorun.kadai.data.model.TaskSortConfig
import org.kaorun.kadai.data.model.TaskSortDirection
import org.kaorun.kadai.data.model.TaskSortField
import org.kaorun.kadai.data.model.ThemeMode
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
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_THEME = booleanPreferencesKey("dynamic_theme")
    }

    val isOnboardingCompleted: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
        }
        .distinctUntilChanged()

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
        .distinctUntilChanged()

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
                TaskSortField.entries.find { it.name == name }
            } ?: TaskSortField.DATE_CREATED

            val direction = preferences[PreferencesKeys.SORT_DIRECTION]?.let { name ->
                TaskSortDirection.entries.find { it.name == name }
            } ?: TaskSortDirection.DESCENDING

            TaskSortConfig(field = sortBy, direction = direction)
        }
        .distinctUntilChanged()

    suspend fun setSortConfig(config: TaskSortConfig) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_BY] = config.field.name
            preferences[PreferencesKeys.SORT_DIRECTION] = config.direction.name
        }
    }

    val themeMode: Flow<ThemeMode> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.THEME_MODE]?.let { name ->
                ThemeMode.entries.find { it.name == name }
            } ?: ThemeMode.SYSTEM_DEFAULT
        }
        .distinctUntilChanged()

    suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    val dynamicTheme: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.DYNAMIC_THEME] ?: true
        }
        .distinctUntilChanged()

    suspend fun setDynamicTheme(isDynamicTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DYNAMIC_THEME] = isDynamicTheme
        }
    }
}