package com.mr.anonym.iphotos.presentation.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr.anonym.domain.model.HitsItem
import com.mr.anonym.domain.remote.useCase.RemoteUseCases
import com.mr.anonym.iphotos.presentation.utils.downloadWithCoil
import com.mr.anonym.iphotos.presentation.utils.saveImageToMemoryWithMediaStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val remoteUseCases: RemoteUseCases
):ViewModel(){

    private val id = mutableIntStateOf( -1 )

    private val _isSaved = mutableStateOf(false)
    val isSaved: State<Boolean> = _isSaved

    private val _photoModel = MutableStateFlow(listOf(HitsItem()))
    val photoModel: StateFlow<List<HitsItem>> = _photoModel.asStateFlow()

    init {
        Log.d("UtilsLogging", "FUllViewModel: ${id.intValue}")
        savedStateHandle.get<Int>("screenID")?.let {fullID->
            if (fullID != -1){
                id.intValue = fullID
                Log.d("UtilsLogging", "FUllViewModel: ${id.intValue}")
            }
        }
        getById()
    }
    fun downloadAndSaveImageEvent(context: Context, imageUrl:String) = viewModelScope.launch {
        val imageData = downloadWithCoil(context, imageUrl)
        Log.d("IOlogging", "downloadAndSaveImageEvent: $imageData")
        if (imageData != null){
            saveImageToMemoryWithMediaStore(context,imageData)
            _isSaved.value = true
        }
    }
    private fun getById() = viewModelScope.launch {
        remoteUseCases.getRemotePhotoByID(id.intValue).collect {
            _photoModel.value = it
        }
    }
}