package com.mr.anonym.iphotos.presentation.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

fun saveImageToMemoryWithMediaStore(context: Context, bitmap: Bitmap): Boolean {

    val fileName = "IMG_${System.currentTimeMillis()}.jpg"
    val path = Environment.DIRECTORY_PICTURES

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, path)
    }
    val uri =
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: return false
    return try {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        true
    } catch (e: IOException) {
        Log.d("IOlogging", "SaveImageToMemory: ${e.message}")
        false
    }
}

@SuppressLint("SuspiciousIndentation")
fun saveImageToMemoryWithDefault(context: Context,bitmap: Bitmap) {

    val fileName = "IMG_${System.currentTimeMillis()}.jpg"
    val path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"bek folder")

    if (!path.exists()) path.mkdirs()

    val file = File(path,fileName)

    try {
        FileOutputStream(file).use {outputStream->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        Log.d("IOlogging", "saveImageToMemoryWithDefault = Файл сохранён: ${file.absolutePath}")
    } catch (io: Exception) {
        Log.e("IOlogging", "saveImageToMemoryWithDefault = : ${io.message}",io)
    }
}