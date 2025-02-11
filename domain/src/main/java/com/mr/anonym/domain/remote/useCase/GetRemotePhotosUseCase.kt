package com.mr.anonym.domain.remote.useCase

import com.mr.anonym.domain.remote.repository.RemotePhotosRepository

class GetRemotePhotosUseCase(private val repository: RemotePhotosRepository) {

    operator fun invoke(order:String,q:String) = repository.getPhotos(order,q)
}