package com.mr.anonym.domain.local.useCase

import com.mr.anonym.domain.local.repository.LocalPhotosRepository
import com.mr.anonym.domain.model.PhotosEntity

class InsertAllLocalPhotosUseCase(private val repository: LocalPhotosRepository) {

    suspend operator fun invoke(photos:List<PhotosEntity>){
        repository.insertAll(photos)
    }
}