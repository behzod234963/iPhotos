package com.mr.anonym.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("photosDataStore")
class DataStoreInstance(private val context: Context) {

    suspend fun isStart(status:Boolean){
        val key = booleanPreferencesKey("isStart")
        context.dataStore.edit {
            it[key] = status
        }
    }
    fun getIsStartStatus():Flow<Boolean>{
        val key = booleanPreferencesKey("isStart")
        return context.dataStore.data.map {
            it[key]?:false
        }
    }
}