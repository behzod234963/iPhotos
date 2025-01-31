package com.mr.anonym.iphotos.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr.anonym.domain.remote.useCase.RemoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val remoteUseCases: RemoteUseCases
) :ViewModel(){

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh = _isRefresh.asStateFlow()

    fun getPhotos(order:String) = remoteUseCases.getRemotePhotos.execute()

    fun isLoading(order: String) {
        viewModelScope.launch {
            _isRefresh.value = true
            delay(3000L)
            _isRefresh.value = false
            getPhotos(order)
        }
    }
}