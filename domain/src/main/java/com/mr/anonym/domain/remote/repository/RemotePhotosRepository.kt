package com.mr.anonym.domain.remote.repository

import androidx.paging.PagingData
import com.mr.anonym.domain.BuildConfig
import com.mr.anonym.domain.model.HitsItem
import com.mr.anonym.domain.model.PhotosModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemotePhotosRepository {

    fun getPhotos(
        key:String = BuildConfig.api_key,
        q:String,
        imageType:String = "photo",
        order:String = "popular",
    ): Flow<PagingData<HitsItem>>

    suspend fun getPhotoById(
        key:String = BuildConfig.api_key,
        id:Int,
        imageType:String = "photo"
    ): Response<PhotosModel>
}