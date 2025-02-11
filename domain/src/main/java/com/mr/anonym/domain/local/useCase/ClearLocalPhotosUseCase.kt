package com.mr.anonym.domain.local.useCase

import com.mr.anonym.domain.local.repository.LocalPhotosRepository

class ClearLocalPhotosUseCase (private val repository: LocalPhotosRepository){

    suspend operator fun invoke() {
        repository.clearPhotos()
    }
}