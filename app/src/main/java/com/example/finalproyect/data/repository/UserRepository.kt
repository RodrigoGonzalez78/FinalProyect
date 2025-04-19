package com.example.finalproyect.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.finalproyect.data.local.dao.UserDao
import com.example.finalproyect.data.remote.ApiService
import com.example.finalproyect.domain.model.User
import com.example.finalproyect.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: UserDao,
    private val dataStore: DataStore<Preferences>
) : UserRepository {

    override suspend fun getUsers(): List<User> {
        // 1. Obtiene remoto
        //val remote = api.fetchUsers()
        // 2. Guarda en local
        //dao.insertAll(remote.map { it.toEntity() })
        // 3. Devuelve dominio
        return  emptyList()//dao.getAll().map { it.toDomain() }
    }

    override suspend fun saveUser(user: User) {
        //dao.insert(user.toEntity())
    }

    // Ejemplo de DataStore
    override val prefsFlow: Flow<Int> =
        dataStore.data.map { prefs -> prefs[intPreferencesKey("counter")] ?: 0 }
}
