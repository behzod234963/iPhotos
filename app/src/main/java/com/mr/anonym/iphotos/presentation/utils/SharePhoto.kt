package com.mr.anonym.iphotos.presentation.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.mr.anonym.iphotos.R

class SharePhoto {

    fun execute(
        imageUri:String,
        title:String,
        context: Context,
    ){
        Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_TEXT, title)
        }.also {
            val chooser = Intent.createChooser(
                it, context.getString(R.string.share_via)
            )
            startActivity(context,chooser,null)
        }
    }
}