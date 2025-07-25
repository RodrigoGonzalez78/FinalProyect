package com.example.finalproyect.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val authTokenKey = stringPreferencesKey(Constants.PREF_AUTH_TOKEN)
    private val currentUserEmailKey = stringPreferencesKey(Constants.PREF_CURRENT_USER_EMAIL)
    private val currentUserIDKey = stringPreferencesKey(Constants.PREF_CURRENT_USER_ID)

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[authTokenKey] = token
        }
    }

    fun getAuthToken(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[authTokenKey] ?: ""
        }
    }

    suspend fun clearAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(authTokenKey)
        }
    }

    suspend fun saveCurrentUserID(userID: String) {
        context.dataStore.edit { preferences ->
            preferences[currentUserIDKey] = userID
        }
    }

    fun getCurrentUserID():Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[currentUserIDKey] ?: ""
        }
    }

    suspend fun clearCurrentUserID() {
        context.dataStore.edit { preferences ->
            preferences.remove(currentUserIDKey)
        }
    }

    suspend fun saveCurrentUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[currentUserEmailKey] = email
        }
    }


    fun getCurrentUserEmail(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[currentUserEmailKey] ?: ""
        }
    }

    suspend fun clearCurrentUserEmail() {
        context.dataStore.edit { preferences ->
            preferences.remove(currentUserEmailKey)
        }
    }
}