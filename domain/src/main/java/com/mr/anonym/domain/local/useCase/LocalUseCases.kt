package com.mr.anonym.domain.local.useCase

data class LocalUseCases(
    val insertAllLocalPhotosUseCase: InsertAllLocalPhotosUseCase,
    val insertPhoto:InsertLocalPhotoUseCase,
    val getPhotos:GetLocalPhotosUseCase,
    val getPhoto:GetLocalPhotoUseCase,
    val updateIsFavorite:UpdateIsFavoriteUseCase,
    val deletePhoto:DeleteLocalPhotoUseCase,
    val clearPhotos:ClearLocalPhotosUseCase
)
