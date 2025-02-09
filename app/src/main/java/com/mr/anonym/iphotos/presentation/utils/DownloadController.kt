package com.mr.anonym.iphotos.presentation.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import coil3.BitmapImage
import coil3.ImageLoader
import coil3.asDrawable
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware

suspend fun downloadWithCoil(context: Context,url:String):Bitmap?{
    return try {
        val imageLoader = ImageLoader.Builder(context).build()
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false)
            .build()

        val result = imageLoader.execute(request)

        if (result is SuccessResult){
            (result.image as? BitmapImage)?.bitmap
        }else{
            Log.e("IOlogging", "Ошибка загрузки изображения: результат не SuccessResult")
            null
        }
    }catch (e:Exception){
        Log.d("IOlogging", "downloadWithCoil: ${e.message}")
        null
    }
}