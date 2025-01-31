package com.mr.anonym.data.local.dataStore

import android.content.Context
import android.preference.PreferenceDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("photosDataStore")
class DataStoreInstance(private val context: Context) {

    suspend fun connectionStatus(status:Boolean){
        val key = booleanPreferencesKey("netStatus")
        context.dataStore.edit {
            it[key] = status
        }
    }
    fun getConnectionStatus():Flow<Boolean>{
        val key = booleanPreferencesKey("netStatus")
        return context.dataStore.data.map {
            it[key]?:false
        }
    }
}