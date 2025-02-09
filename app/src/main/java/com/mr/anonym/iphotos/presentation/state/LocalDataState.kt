package com.mr.anonym.iphotos.presentation.state

import com.mr.anonym.domain.model.PhotosEntity

data class LocalDataState(
    val photos:List<PhotosEntity> = emptyList()
)