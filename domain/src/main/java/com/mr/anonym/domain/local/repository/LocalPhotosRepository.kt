package com.mr.anonym.domain.local.repository

import com.mr.anonym.domain.model.PhotosEntity
import kotlinx.coroutines.flow.Flow

interface LocalPhotosRepository {

    suspend fun insertAll(photos:List<PhotosEntity>)
    suspend fun insertPhoto(photo:PhotosEntity)
    fun getPhotos():Flow<List<PhotosEntity>>
    fun getPhoto(id:Int):Flow<PhotosEntity>
    suspend fun updateIsFavorite(id:Int,isFavorite:Boolean)
    suspend fun deletePhoto(photo: PhotosEntity)
    suspend fun clearPhotos()
}