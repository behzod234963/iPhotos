package com.mr.anonym.data.local.room
import com.mr.anonym.domain.model.PhotosEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PhotosEntity::class], version = 1)
abstract class RoomInstance :RoomDatabase(){

    abstract val photosDao : PhotosDao
}