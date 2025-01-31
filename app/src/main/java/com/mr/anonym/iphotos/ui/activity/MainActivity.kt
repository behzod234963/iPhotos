package com.mr.anonym.iphotos.ui.activity

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mr.anonym.data.local.dataStore.DataStoreInstance
import com.mr.anonym.iphotos.presentation.navigation.NavGraph
import com.mr.anonym.iphotos.presentation.utils.CheckConnection
import com.mr.anonym.iphotos.ui.theme.IPhotosTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var dataStore:DataStoreInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IPhotosTheme {
                NavGraph()
            }
        }
    }
}