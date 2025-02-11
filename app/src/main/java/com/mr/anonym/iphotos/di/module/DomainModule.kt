package com.mr.anonym.iphotos.di.module

import com.mr.anonym.data.implementation.local.LocalPhotosRepositoryImpl
import com.mr.anonym.data.implementation.remote.RemotePhotosRepositoryImpl
import com.mr.anonym.data.local.room.PhotosDao
import com.mr.anonym.data.remote.PhotosApi
import com.mr.anonym.domain.local.repository.LocalPhotosRepository
import com.mr.anonym.domain.local.useCase.ClearLocalPhotosUseCase
import com.mr.anonym.domain.local.useCase.DeleteLocalPhotoUseCase
import com.mr.anonym.domain.local.useCase.GetLocalPhotoUseCase
import com.mr.anonym.domain.local.useCase.GetLocalPhotosUseCase
import com.mr.anonym.domain.local.useCase.InsertAllLocalPhotosUseCase
import com.mr.anonym.domain.local.useCase.InsertLocalPhotoUseCase
import com.mr.anonym.domain.local.useCase.LocalUseCases
import com.mr.anonym.domain.local.useCase.UpdateIsFavoriteUseCase
import com.mr.anonym.domain.remote.repository.RemotePhotosRepository
import com.mr.anonym.domain.remote.useCase.GetRemotePhotoByID
import com.mr.anonym.domain.remote.useCase.GetRemotePhotosUseCase
import com.mr.anonym.domain.remote.useCase.RemoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideRemoteRepository(api:PhotosApi):RemotePhotosRepository = RemotePhotosRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideRemoteUseCase(repository: RemotePhotosRepository) = RemoteUseCases(
        getRemotePhotosUseCase = GetRemotePhotosUseCase(repository),
        getRemotePhotoByID = GetRemotePhotoByID(repository)
    )

    @Provides
    @Singleton
    fun provideLocalRepository(dao: PhotosDao):LocalPhotosRepository = LocalPhotosRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideLocalUseCases(repository: LocalPhotosRepository) = LocalUseCases(
        insertAllLocalPhotosUseCase = InsertAllLocalPhotosUseCase(repository),
        insertPhoto = InsertLocalPhotoUseCase(repository),
        getPhotos = GetLocalPhotosUseCase(repository),
        getPhoto = GetLocalPhotoUseCase(repository),
        updateIsFavorite = UpdateIsFavoriteUseCase(repository),
        deletePhoto = DeleteLocalPhotoUseCase(repository),
        clearPhotos = ClearLocalPhotosUseCase(repository)
    )
}