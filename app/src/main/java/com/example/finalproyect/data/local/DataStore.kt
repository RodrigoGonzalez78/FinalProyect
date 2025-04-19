package com.example.finalproyect.data.local

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences
import javax.inject.Inject

class DataStore @Inject constructor(
    private val dataStore: androidx.datastore.core.DataStore<Preferences>
) {
    private val JWT_KEY = stringPreferencesKey("jwt_token")
    private val USER_ID= stringPreferencesKey("user_id")

    suspend fun saveUserId(userId:String ){

    }




}