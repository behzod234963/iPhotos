package com.mr.anonym.domain.local.useCase

import com.mr.anonym.domain.local.repository.LocalPhotosRepository

class GetLocalPhotosUseCase(private val repository: LocalPhotosRepository) {
    operator fun invoke() = repository.getPhotos()
}