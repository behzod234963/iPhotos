package com.mr.anonym.iphotos.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mr.anonym.iphotos.R
import com.mr.anonym.iphotos.presentation.utils.NavigationArguments
import com.mr.anonym.iphotos.presentation.viewModel.FullScreenViewModel
import com.mr.anonym.iphotos.presentation.viewModel.PhotoViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FullScreen(
    navController: NavController,
    arguments: NavigationArguments,
    viewModel: FullScreenViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val defaultBackground = if (isSystemInDarkTheme()) Color.Black else Color.White
    val fontColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    val model = viewModel.photoModel.collectAsState()

    val loadingAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_photos_loading)
    )

    val isSaved = viewModel.isSaved

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
                            contentDescription = "button back",
                            tint = fontColor
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
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
                                    viewModel.saveImageWithDefault(context,model.value[0].largeImageURL)
                                }else{
                                    Toast.makeText(context, "Operation not permitted", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                viewModel.saveImageWithMediaStore(context,model.value[0].largeImageURL)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_download),
                            contentDescription = "button download",
                            tint = fontColor
                        )
                    }
                }
            )
        }
    ) {
        if (isSaved.value){
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                LottieAnimation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    composition = loadingAnimation.value
                )
            }
            LaunchedEffect(Unit) {
                delay(2000)
                viewModel.changeSaveStatus()
            }
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { navController.popBackStack() }
            ) {
                model.value.forEach {item->
                    Log.d("UtilsLogging", "FullScreen: $item")
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(item.largeImageURL)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Fit,
                        contentDescription = "Fullscreen image",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}