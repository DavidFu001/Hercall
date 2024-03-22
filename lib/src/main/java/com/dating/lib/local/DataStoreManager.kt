package com.dating.lib.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dating.lib.App.Companion.appContext
import com.dating.lib.extension.superLaunch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "DatingAppStore")

object DataStoreManager {

    private val dataStore: DataStore<Preferences> by lazy { appContext.dataStore }

    fun getInt(key: String, default: Int = 0): Int = runBlocking {
        return@runBlocking dataStore.data.catch {
            emit(emptyPreferences())
        }.map {
            it[intPreferencesKey(key)] ?: default
        }.first()
    }

    fun getIntFlow(key: String, default: Int = 0): Flow<Int> = dataStore.data.catch {
        it.printStackTrace()
        emit(emptyPreferences())
    }.map {
        it[intPreferencesKey(key)] ?: default
    }

    fun getString(key: String, default: String = ""): String = runBlocking {
        return@runBlocking dataStore.data.map {
            it[stringPreferencesKey(key)] ?: default
        }.first()
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean = runBlocking {
        return@runBlocking dataStore.data.map {
            it[booleanPreferencesKey(key)] ?: default
        }.first()
    }

    fun getBooleanFlow(key: String, default: Boolean = false): Flow<Boolean> =
        dataStore.data.catch {
            it.printStackTrace()
            emit(emptyPreferences())
        }.map {
            it[booleanPreferencesKey(key)] ?: default
        }

    fun getStringFlow(key: String, default: String = ""): Flow<String> = dataStore.data.catch {
        it.printStackTrace()
        emit(emptyPreferences())
    }.map {
        it[stringPreferencesKey(key)] ?: default
    }

    fun getLong(key: String, default: Long = 0): Long = runBlocking {
        return@runBlocking dataStore.data.map {
            it[longPreferencesKey(key)] ?: default
        }.first()
    }

    fun getFloat(key: String, default: Float = 0f): Float = runBlocking {
        return@runBlocking dataStore.data.map {
            it[floatPreferencesKey(key)] ?: default
        }.first()
    }

    fun getDouble(key: String, default: Double = 0.0): Double = runBlocking {
        return@runBlocking dataStore.data.map {
            it[doublePreferencesKey(key)] ?: default
        }.first()
    }

    fun getLongFlow(key: String, default: Long = 0L): Flow<Long> = dataStore.data.catch {
        it.printStackTrace()
        emit(emptyPreferences())
    }.map {
        it[longPreferencesKey(key)] ?: default
    }

    fun <U> put(key: String, value: U) {
        appContext.superLaunch {
            when (value) {
                is Long -> saveLongData(key, value)
                is String -> saveStringData(key, value)
                is Int -> saveIntData(key, value)
                is Boolean -> saveBooleanData(key, value)
                is Float -> saveFloatData(key, value)
            }
        }
    }

    suspend fun saveBooleanData(key: String, value: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun saveIntData(key: String, value: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[intPreferencesKey(key)] = value
        }
    }

    suspend fun saveStringData(key: String, value: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun saveFloatData(key: String, value: Float) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[floatPreferencesKey(key)] = value
        }
    }

    suspend fun saveLongData(key: String, value: Long) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[longPreferencesKey(key)] = value
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun removeStringData(key: String) {
        dataStore.edit {
            runCatching {
                it.remove(stringPreferencesKey(key))
            }
        }
    }

    fun clearSync() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
        }
    }
}