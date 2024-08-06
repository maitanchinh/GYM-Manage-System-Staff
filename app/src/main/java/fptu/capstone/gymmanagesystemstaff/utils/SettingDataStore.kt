package fptu.capstone.gymmanagesystemstaff.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

    private val Context.dataStore by preferencesDataStore(name = "setting_prefs")
object SettingDataStore {

        private val DARK_MODE = booleanPreferencesKey("dark_mode")

    fun getDarkMode(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[DARK_MODE] ?: false
            }
    }

    suspend fun saveDarkMode(context: Context, isDarkMode: Boolean) {
        try {
            context.dataStore.edit { preferences ->
                preferences[DARK_MODE] = isDarkMode
            }
        } catch (e: Exception) {
            // Handle IOException appropriately
            e.printStackTrace()
        }
    }
}