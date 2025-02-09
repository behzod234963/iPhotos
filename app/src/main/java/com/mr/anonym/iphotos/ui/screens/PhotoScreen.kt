package com.mr.anonym.iphotos.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mr.anonym.domain.model.PhotosEntity
import com.mr.anonym.iphotos.presentation.events.LocalDataEvent
import com.mr.anonym.iphotos.presentation.navigation.Screens
import com.mr.anonym.iphotos.presentation.utils.IOpermissionHandler
import com.mr.anonym.iphotos.presentation.utils.NavigationArguments
import com.mr.anonym.iphotos.presentation.utils.SharePhoto
import com.mr.anonym.iphotos.presentation.viewModel.PhotoViewModel
import com.mr.anonym.iphotos.ui.items.PhotoContentComponent

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PhotoScreen(
    navController: NavController,
    arguments: NavigationArguments,
    viewModel: PhotoViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val isRefresh = viewModel.isRefreshing.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefresh.value)

    val defaultBackground = if (isSystemInDarkTheme()) Color.Black else Color.White
    val fontColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    val photoModel = viewModel.photoModel.collectAsState()
    val photoEntity = viewModel.photoEntity

    val localPhotos = viewModel.localPhotos

    viewModel.getById()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(defaultBackground),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "button back to photos"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(photoModel.value){model->
                PhotoContentComponent(
                    context = context,
                    fontColor = fontColor,
                    model = model,
                    entity = photoEntity.value,
                    onContentPhotoClick = { navController.navigate(Screens.FullScreen.route + "/${model.id}") },
                    onFavoriteClick = {
                        localPhotos.value.forEach { entity ->
                            if (model.id == entity.id) {
                                viewModel.onLocalDataEvent(
                                    LocalDataEvent.UpdateIsFavorite(
                                        id = model.id ?: -1,
                                        isFavorite = true
                                    )
                                )
                            } else {
                                viewModel.onLocalDataEvent(
                                    LocalDataEvent.InsertPhoto(
                                        PhotosEntity(
                                            id = model.id ?: -1,
                                            isFavorite = true,
                                            imageUrl = model.largeImageURL
                                        )
                                    )
                                )
                            }
                        }
                    },
                    onDownloadClick = {
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q){
                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED
                            ){
                                viewModel.saveImageWithDefault(context,model.largeImageURL)
                            }else{
                                Toast.makeText(context, "Operation not permitted", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            viewModel.saveImageWithMediaStore(context,model.largeImageURL)
                        }
                    },
                    onShareClick = {
                        SharePhoto().execute(
                            context = context,
                            title = "Share via",
                            imageUri = model.largeImageURL ?: ""
                        )
                    }
                )
            }
        }
    }
}