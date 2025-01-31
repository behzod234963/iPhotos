package com.mr.anonym.iphotos.di.module

import com.mr.anonym.data.local.implementation.remote.RemotePhotosRepositoryImpl
import com.mr.anonym.data.remote.PhotosApi
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
        getRemotePhotos = GetRemotePhotosUseCase(repository),
        getRemotePhotoByID = GetRemotePhotoByID(repository)
    )
}