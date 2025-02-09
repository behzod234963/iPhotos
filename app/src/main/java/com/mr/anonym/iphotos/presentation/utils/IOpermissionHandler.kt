package com.mr.anonym.iphotos.presentation.utils

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun IOpermissionHandler(
    context: Context,
    onPermissionGranted:()->Unit
) {

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {permissions->
        if(permissions.all{it.value}){
            onPermissionGranted()
        }
    }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            permissionLauncher.launch(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ))
        }else{
            onPermissionGranted()
        }
    }
}