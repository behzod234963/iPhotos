package com.mr.anonym.iphotos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mr.anonym.iphotos.R
import com.mr.anonym.iphotos.presentation.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
) {

    val context = LocalContext.current

    val defaultColor = if(isSystemInDarkTheme()) Color.Black else Color.White

    val photoAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_photo)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(defaultColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxSize(),
            composition = photoAnimation.value,
            alignment = Alignment.Center,
            restartOnPlay = false,
//            iterations = LottieConstants.IterateForever
        )
        LaunchedEffect(Unit) {
            delay(2500)
            navController.navigate(Screens.PhotosScreen.route)
        }
    }
}