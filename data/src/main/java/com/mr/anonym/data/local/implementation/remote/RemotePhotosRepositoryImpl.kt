package com.mr.anonym.data.local.implementation.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mr.anonym.data.remote.PhotosApi
import com.mr.anonym.data.remote.PhotosPagingSource
import com.mr.anonym.data.remote.PhotosRemoteMediator
import com.mr.anonym.domain.model.HitsItem
import com.mr.anonym.domain.model.PhotosModel
import com.mr.anonym.domain.remote.repository.RemotePhotosRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RemotePhotosRepositoryImpl(private val api: PhotosApi):RemotePhotosRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPhotos(
        key: String,
        q: String,
        imageType: String,
        order: String
    ): Flow<PagingData<HitsItem>> = Pager(
        config = PagingConfig(10),
        pagingSourceFactory = { PhotosPagingSource(api = api, order = order, q = q) }
    ).flow

    override suspend fun getPhotoById(key: String, id: Int, imageType: String): Response<PhotosModel> = api.getPhotoById(id = id)
}