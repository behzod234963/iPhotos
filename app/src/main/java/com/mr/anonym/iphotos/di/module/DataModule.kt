package com.mr.anonym.iphotos.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mr.anonym.data.local.dataStore.DataStoreInstance
import com.mr.anonym.data.local.room.PhotosDao
import com.mr.anonym.data.local.room.RoomInstance
import com.mr.anonym.data.remote.PhotosApi
import com.mr.anonym.iphotos.presentation.utils.BASE_URL
import com.mr.anonym.iphotos.presentation.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHTTP():OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit2():Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHTTP())
            .build()

    @Provides
    @Singleton
    fun providePhotosApi(retrofit: Retrofit):PhotosApi = retrofit.create(PhotosApi::class.java)

    @Provides
    @Singleton
    fun provideRoomInstance(application:Application): RoomInstance =
        Room.databaseBuilder(
            application,
            RoomInstance::class.java,
            DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun providePhotosDao(roomInstance: RoomInstance): PhotosDao = roomInstance.photosDao

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context:Context):DataStoreInstance = DataStoreInstance(context)
}