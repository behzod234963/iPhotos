package com.mr.anonym.iphotos.presentation.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class SharePhoto {

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri? {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "shared_image.png")
        return try {
            FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: Exception) {
            Log.e("UtilsLogging", "saveBitmapToFile: ${e.message}", )
            null
        }
    }
}