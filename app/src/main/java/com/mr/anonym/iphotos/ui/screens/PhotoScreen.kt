package com.mr.anonym.iphotos.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mr.anonym.domain.model.PhotosEntity
import com.mr.anonym.iphotos.R
import com.mr.anonym.iphotos.presentation.events.LocalDataEvent
import com.mr.anonym.iphotos.presentation.navigation.Screens
import com.mr.anonym.iphotos.presentation.utils.NavigationArguments
import com.mr.anonym.iphotos.presentation.utils.SharePhoto
import com.mr.anonym.iphotos.presentation.utils.downloadWithCoil
import com.mr.anonym.iphotos.presentation.viewModel.PhotoViewModel
import com.mr.anonym.iphotos.ui.items.PhotoContentComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PhotoScreen(
    navController: NavController,
    arguments: NavigationArguments,
    viewModel: PhotoViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val sharePhoto = SharePhoto()

    val isSaved = viewModel.isSaved

    val isFavorite = remember { mutableStateOf(false) }
    val isRefresh = viewModel.isRefreshing.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefresh.value)

    val defaultBackground = if (isSystemInDarkTheme()) Color.Black else Color.White
    val fontColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    val loadingAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_photos_loading)
    )

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
                        modifier = Modifier
                            .background(defaultBackground),
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
        if (isSaved.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    composition = loadingAnimation.value
                )
                LaunchedEffect(Unit) {
                    delay(2000)
                    viewModel.changeSaveStatus()
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(photoModel.value) { model ->
                    PhotoContentComponent(
                        context = context,
                        fontColor = fontColor,
                        model = model,
                        onContentPhotoClick = { navController.navigate(Screens.FullScreen.route + "/${model.id}") },
                        onFavoriteClick = {
                            isFavorite.value = it
                            if (model.id == photoEntity.value.id) {
                                viewModel.onLocalDataEvent(
                                    LocalDataEvent.UpdateIsFavorite(
                                        id = model.id ?: -1,
                                        isFavorite = it
                                    )
                                )
                            } else {
                                viewModel.onLocalDataEvent(
                                    LocalDataEvent.InsertPhoto(
                                        PhotosEntity(
                                            id = model.id ?: -1,
                                            isFavorite = it,
                                            imageUrl = model.largeImageURL
                                        )
                                    )
                                )
                            }
                        },
                        favoriteContent = {
                            model.id?.let { modelID ->
                                viewModel.onLocalDataEvent(LocalDataEvent.GetPhoto(modelID))
                            }

                            Icon(
                                modifier = Modifier
                                    .size(40.dp),
                                imageVector = if (photoEntity.value.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "button favorite",
                                tint = Color.Red
                            )

                        },
                        onDownloadClick = {
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                                if (ActivityCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ) == PackageManager.PERMISSION_GRANTED ||
                                    ActivityCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    viewModel.saveImageWithDefault(context, model.largeImageURL)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Operation not permitted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                viewModel.saveImageWithMediaStore(context, model.largeImageURL)
                            }
                        },
                        onShareClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val bitmap = downloadWithCoil(context,model.largeImageURL)
                                bitmap?.let { bitmapExecutor ->
                                    val uri = bitmap.let {  sharePhoto.saveBitmapToFile(context, bitmapExecutor) }
                                    uri?.let {uriExecutor->
                                        withContext(Dispatchers.Main){
                                            val intent = Intent(Intent.ACTION_SEND).apply {
                                                type = "image/*"
                                                putExtra(Intent.EXTRA_TEXT,"This photo shared on iPhotos")
                                                putExtra(Intent.EXTRA_STREAM, uriExecutor)
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                            context.startActivity(Intent.createChooser(intent, "Share via"))
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}