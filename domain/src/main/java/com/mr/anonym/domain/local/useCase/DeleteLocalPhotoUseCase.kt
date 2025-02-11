package com.mr.anonym.domain.local.useCase

import com.mr.anonym.domain.local.repository.LocalPhotosRepository
import com.mr.anonym.domain.model.PhotosEntity

class DeleteLocalPhotoUseCase(private val repository: LocalPhotosRepository) {
    suspend operator fun invoke(photo:PhotosEntity) = repository.deletePhoto(photo)
}