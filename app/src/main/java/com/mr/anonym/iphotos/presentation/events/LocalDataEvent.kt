package com.mr.anonym.iphotos.presentation.events

import com.mr.anonym.domain.model.PhotosEntity

sealed class LocalDataEvent {
    data class InsertAll(val photos: List<PhotosEntity>):LocalDataEvent()
    data class InsertPhoto(val photo:PhotosEntity):LocalDataEvent()
    data object GetPhotos : LocalDataEvent()
    data class GetPhoto(val id:Int):LocalDataEvent()
    data class UpdateIsFavorite(val id:Int,val isFavorite:Boolean):LocalDataEvent()
    data object ClearPhotos : LocalDataEvent()
    data class DeletePhoto(val photo:PhotosEntity):LocalDataEvent()
}