package com.mr.anonym.iphotos.presentation.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr.anonym.domain.local.useCase.LocalUseCases
import com.mr.anonym.domain.model.PhotosEntity
import com.mr.anonym.iphotos.presentation.state.LocalDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val localUseCases: LocalUseCases
) : ViewModel() {

    private val _photo = mutableStateOf(PhotosEntity())
    val photo = _photo

    private val _photos = mutableStateOf(LocalDataState().photos)
    val photos = _photos

    init {
        getAllPhoto()
    }

    private fun getAllPhoto() = viewModelScope.launch {
        localUseCases.getPhotos().collect {
            _photos.value = it
        }
    }

    fun deletePhoto() = viewModelScope.launch {
        if (_photos.value.isNotEmpty()) {
            _photos.value.forEach { photo ->
                if (!photo.isFavorite) {
                    localUseCases.deletePhoto(photo)
                }
            }
        }
    }
}