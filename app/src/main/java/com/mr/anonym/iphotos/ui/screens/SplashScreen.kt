package com.mr.anonym.iphotos.ui.screens

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mr.anonym.iphotos.R
import com.mr.anonym.iphotos.presentation.navigation.Screens
import com.mr.anonym.iphotos.presentation.viewModel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {

    val defaultColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    val splashAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_photo)
    )

    viewModel.deletePhoto()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(defaultColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(250.dp),
            composition = splashAnimation.value,
            alignment = Alignment.Center,
            restartOnPlay = false,
//            iterations = LottieConstants.IterateForever
        )
        LaunchedEffect(Unit) {
            delay(3000)
            navController.navigate(Screens.PhotosScreen.route){
                popUpTo(Screens.SplashScreen.route){ inclusive = true }
            }
        }
    }
}