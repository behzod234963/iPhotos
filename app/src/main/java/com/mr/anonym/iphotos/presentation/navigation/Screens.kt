package com.mr.anonym.iphotos.presentation.navigation

sealed class Screens(val route:String) {
    data object SplashScreen: Screens(route = "SplashScreen")
    data object PhotosScreen:Screens(route = "PhotosScreen")
    data object PhotoScreen:Screens(route = "PhotoScreen")
}