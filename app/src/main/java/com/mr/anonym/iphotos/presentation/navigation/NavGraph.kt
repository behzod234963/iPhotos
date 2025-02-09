package com.mr.anonym.iphotos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mr.anonym.iphotos.presentation.utils.NavigationArguments
import com.mr.anonym.iphotos.ui.screens.FullScreen
import com.mr.anonym.iphotos.ui.screens.PhotoScreen
import com.mr.anonym.iphotos.ui.screens.PhotosScreen
import com.mr.anonym.iphotos.ui.screens.SplashScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route
    ){
        composable(Screens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(Screens.PhotosScreen.route) {
            PhotosScreen(navController)
        }
        composable(
            Screens.PhotoScreen.route + "/{id}",
            arguments = listOf(navArgument(name = "id"){
                    type = NavType.IntType
                    defaultValue = -1
                })
        ) {entry->
            val id = entry.arguments?.getInt("id")?:-1
            PhotoScreen(
                navController = navController,
                arguments = NavigationArguments(id,-1)
            )
        }
        composable(
            Screens.FullScreen.route + "/{screenID}",
            arguments = listOf(navArgument("screenID"){
                    type = NavType.IntType
                    defaultValue = -1
                })
        ) {entry->
            val id = entry.arguments?.getInt("screenID")?:-1
            FullScreen(
                navController = navController,
                arguments = NavigationArguments(
                    id = -1,
                    fullID = id
                )
            )
        }
    }
}