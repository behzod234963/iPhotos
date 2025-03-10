package com.mr.anonym.domain.local.useCase

import com.mr.anonym.domain.local.repository.LocalPhotosRepository

class GetLocalPhotoUseCase(private val repository: LocalPhotosRepository) {
    operator fun invoke(id:Int) = repository.getPhoto(id)
}