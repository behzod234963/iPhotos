package com.mr.anonym.data.implementation.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mr.anonym.data.remote.PhotosApi
import com.mr.anonym.data.remote.PhotosPagingSource
import com.mr.anonym.domain.model.HitsItem
import com.mr.anonym.domain.model.PhotosModel
import com.mr.anonym.domain.remote.repository.RemotePhotosRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RemotePhotosRepositoryImpl(private val api: PhotosApi):RemotePhotosRepository {
    override fun getPhotos(
        key: String,
        q: String,
        imageType: String,
        order: String
    ): Flow<PagingData<HitsItem>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = { PhotosPagingSource(api,q,order) }
        ).flow
    }

    override suspend fun getPhotoById(key: String, id: Int, imageType: String): Response<PhotosModel> = api.getPhotoById(id = id)
}