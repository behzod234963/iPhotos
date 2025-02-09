package com.mr.anonym.domain.local.useCase

import com.mr.anonym.domain.local.repository.LocalPhotosRepository

class UpdateIsFavoriteUseCase(private val repository: LocalPhotosRepository) {
    suspend operator fun invoke(id:Int,isFavorite:Boolean) = repository.updateIsFavorite(id, isFavorite)
}