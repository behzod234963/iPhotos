package com.mr.anonym.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("photos")
data class PhotosEntity (
    @PrimaryKey(autoGenerate = true)
    val id :Int = -1,
    val isFavorite:Boolean = false,
    val imageUrl:String = ""
)