package com.mr.anonym.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.mr.anonym.domain.model.PhotosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotosDao {

    @Insert
    suspend fun insertAll(photos:List<PhotosEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhoto(photo:PhotosEntity)

    @Query("SELECT * FROM photos")
    fun getPhotos():Flow<List<PhotosEntity>>

    @Query("SELECT * FROM photos WHERE id=:id")
    fun getPhoto(id:Int):Flow<PhotosEntity>

    @Query("UPDATE photos SET isFavorite=:isFavorite WHERE id=:id")
    suspend fun updateIsFavorite(id:Int,isFavorite:Boolean)

    @Delete
    suspend fun deletePhoto(photo: PhotosEntity)

    @Query("DELETE  FROM photos")
    suspend fun clearPhotos()
}