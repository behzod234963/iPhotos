package com.mr.anonym.domain.local.useCase

data class LocalUseCases(
    val insertPhoto:InsertPhotoUseCase,
    val getPhotos:GetPhotosUseCase,
    val getPhoto:GetPhotoUseCase,
    val updateIsFavorite:UpdateIsFavoriteUseCase,
    val deletePhoto:DeletePhotoUseCase
)
