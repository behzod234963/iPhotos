package com.mr.anonym.iphotos.presentation.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr.anonym.domain.local.useCase.LocalUseCases
import com.mr.anonym.domain.model.HitsItem
import com.mr.anonym.domain.model.PhotosEntity
import com.mr.anonym.domain.remote.useCase.RemoteUseCases
import com.mr.anonym.iphotos.presentation.events.LocalDataEvent
import com.mr.anonym.iphotos.presentation.state.LocalDataState
import com.mr.anonym.iphotos.presentation.utils.downloadWithCoil
import com.mr.anonym.iphotos.presentation.utils.saveImageToMemoryWithDefault
import com.mr.anonym.iphotos.presentation.utils.saveImageToMemoryWithMediaStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val remoteUseCases: RemoteUseCases,
    private val localUseCases: LocalUseCases
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val id = mutableIntStateOf(-1)

    private val _photoModel = MutableStateFlow(listOf(HitsItem()))
    val photoModel: StateFlow<List<HitsItem>> = _photoModel.asStateFlow()
    private val _photoEntity = mutableStateOf(PhotosEntity())
    val photoEntity: State<PhotosEntity> = _photoEntity

    private val _localPhotos = mutableStateOf(LocalDataState().photos)
    val localPhotos: State<List<PhotosEntity>> = _localPhotos

    private val _isSaved = mutableStateOf(false)
    val isSaved:State<Boolean> = _isSaved

    init {
        savedStateHandle.get<Int>("id")?.let { entry ->
            if (entry != -1) {
                id.intValue = entry
            }
        }
        onLocalDataEvent(LocalDataEvent.GetPhotos)
    }

    fun getById() = viewModelScope.launch {
        remoteUseCases.getRemotePhotoByID(id.intValue).collect {
            _photoModel.value = it
        }
    }

    fun isRefresh() = viewModelScope.launch {
        _isRefreshing.value = true
        delay(3000L)
        _isRefreshing.value = false
        getById()
    }

    fun onLocalDataEvent(event: LocalDataEvent) = viewModelScope.launch {
        when (event) {
            is LocalDataEvent.GetPhoto -> {
                localUseCases.getPhoto(event.id).collect {
                    _photoEntity.value = it
                }
            }

            is LocalDataEvent.GetPhotos -> {
                localUseCases.getPhotos().collect {
                    _localPhotos.value = it
                }
            }

            is LocalDataEvent.InsertPhoto -> {
                localUseCases.insertPhoto(event.photo)
            }

            is LocalDataEvent.UpdateIsFavorite -> {
                localUseCases.updateIsFavorite(event.id, event.isFavorite)
            }
        }
    }

    fun downloadAndSaveImageEvent(context: Context, imageUrl:String) = viewModelScope.launch {
        val imageData = downloadWithCoil(context, imageUrl)
        Log.d("IOlogging", "downloadAndSaveImageEvent: $imageData")
        if (imageData != null){
            saveImageToMemoryWithMediaStore(context,imageData)
            _isSaved.value = true
            Log.d("IOlogging", "downloadAndSaveImageEvent: $imageData")
        }else{
            _isSaved.value = false
        }
    }
    fun saveImageWithMediaStore(context: Context, imageUrl:String) = viewModelScope.launch {
        val imageData = downloadWithCoil(context, imageUrl)
        if (imageData != null) {
            saveImageToMemoryWithMediaStore(context,imageData)
            _isSaved.value = true
        }else{
            _isSaved.value = false
        }
    }
    fun saveImageWithDefault(context: Context, imageUrl:String) = viewModelScope.launch {
        val imageData = downloadWithCoil(context, imageUrl)
        if (imageData != null) {
            saveImageToMemoryWithDefault(context,imageData)
            _isSaved.value = true
        }else{
            _isSaved.value = false
        }
    }
}