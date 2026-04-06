package com.mbp.app.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val TOKEN = stringPreferencesKey("auth_token")
        val ROLE = stringPreferencesKey("user_role")
        val CUSTOMER_ID = intPreferencesKey("customer_id")
    }

    val tokenFlow: Flow<String?> = dataStore.data.map { it[Keys.TOKEN] }

    suspend fun saveToken(token: String) {
        dataStore.edit { it[Keys.TOKEN] = token }
    }

    suspend fun getToken(): String? = dataStore.data.map { it[Keys.TOKEN] }.first()

    suspend fun clearToken() {
        dataStore.edit {
            it.remove(Keys.TOKEN)
            it.remove(Keys.ROLE)
            it.remove(Keys.CUSTOMER_ID)
        }
    }

    suspend fun saveUserRole(role: String) {
        dataStore.edit { it[Keys.ROLE] = role }
    }

    suspend fun getUserRole(): String? = dataStore.data.map { it[Keys.ROLE] }.first()

    suspend fun saveCustomerId(id: Int) {
        dataStore.edit { it[Keys.CUSTOMER_ID] = id }
    }

    suspend fun getCustomerId(): Int? = dataStore.data.map { it[Keys.CUSTOMER_ID] }.first()
}
