package com.mr.anonym.iphotos.presentation.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr.anonym.domain.local.useCase.LocalUseCases
import com.mr.anonym.domain.model.HitsItem
import com.mr.anonym.domain.model.PhotosEntity
import com.mr.anonym.domain.remote.useCase.RemoteUseCases
import com.mr.anonym.iphotos.presentation.events.LocalDataEvent
import com.mr.anonym.iphotos.presentation.state.LocalDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val remoteUseCases: RemoteUseCases,
    private val localUseCases: LocalUseCases,
) : ViewModel() {

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh = _isRefresh.asStateFlow()

    private val _model = mutableStateOf(HitsItem())
    val model: State<HitsItem> = _model
    private val _entity = mutableStateOf(PhotosEntity())
    val entity: State<PhotosEntity> = _entity

    private val _localPhotos = mutableStateOf(LocalDataState().photos)
    val localPhotos: State<List<PhotosEntity>> = _localPhotos

    init {
        onLocalDataEvent(LocalDataEvent.GetPhotos)
        Log.d("LocalLogging", "PhotosViewModel: ${localPhotos.value.size}")
    }

    fun getPhotos(order: String,q: String) = remoteUseCases.getRemotePhotosUseCase(order, q)

    fun isLoading(order: String, q: String) {
        viewModelScope.launch {
            _isRefresh.value = true
            delay(3000L)
            _isRefresh.value = false
            getPhotos(order, q)
        }
    }

    suspend fun deleteLocalPhoto() = viewModelScope.launch {
        if (_localPhotos.value.isNotEmpty()){
            _localPhotos.value.forEach { photo->
                if (!photo.isFavorite){
                    localUseCases.deletePhoto(photo)
                }
            }
        }
    }

    fun onLocalDataEvent(event: LocalDataEvent) = viewModelScope.launch {
        when (event) {
            is LocalDataEvent.ClearPhotos -> {
                localUseCases.clearPhotos()
            }
            is LocalDataEvent.GetPhoto -> {
                localUseCases.getPhoto(event.id).collect {
                    _entity.value = it
                }
            }

            LocalDataEvent.GetPhotos -> {
                localUseCases.getPhotos().collect {
                    _localPhotos.value = it
                }
            }

            is LocalDataEvent.InsertPhoto -> {
                localUseCases.insertPhoto(event.photo)
            }

            is LocalDataEvent.UpdateIsFavorite -> {
                localUseCases.updateIsFavorite(id = event.id, isFavorite = event.isFavorite)
            }

            is LocalDataEvent.DeletePhoto -> {

            }

            is LocalDataEvent.InsertAll -> {
                localUseCases.insertAllLocalPhotosUseCase(event.photos)
            }
        }
    }
}