package com.mr.anonym.data.local.implementation.local

import com.mr.anonym.data.local.room.PhotosDao
import com.mr.anonym.domain.local.repository.LocalPhotosRepository
import com.mr.anonym.domain.model.PhotosEntity
import kotlinx.coroutines.flow.Flow

class LocalPhotosRepositoryImpl(private val dao:PhotosDao):LocalPhotosRepository {
    override suspend fun insertPhoto(photo: PhotosEntity) {
        dao.insertPhoto(photo)
    }

    override suspend fun updateIsFavorite(id: Int, isFavorite: Boolean) {
        dao.updateIsFavorite(id, isFavorite)
    }

    override fun getPhotos(): Flow<List<PhotosEntity>> = dao.getPhotos()

    override fun getPhoto(id: Int): Flow<PhotosEntity> = dao.getPhoto(id)

    override suspend fun deletePhoto(photo: PhotosEntity) {
        dao.deletePhoto(photo)
    }
}