package com.mr.anonym.data.remote

import android.graphics.Bitmap
import com.mr.anonym.data.BuildConfig
import com.mr.anonym.domain.model.PhotosModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotosApi {

    @GET("/api/")
    suspend fun getPhotos(
        @Query("key") key:String = BuildConfig.api_key,
        @Query("q") q:String,
        @Query("image_type") imageType:String = "photo",
        @Query("order") order:String = "popular",
        @Query("page") page:Int,
        @Query("per_page") perPage:Int
    ):Response<PhotosModel>

    @GET("/api/")
    suspend fun getPhotoById(
        @Query("key") key:String = BuildConfig.api_key,
        @Query("id") id:Int,
        @Query("image_type") imageType:String = "photo"
    ):Response<PhotosModel>
} 