package com.mr.anonym.iphotos.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mr.anonym.iphotos.R
import com.mr.anonym.iphotos.presentation.utils.NavigationArguments
import com.mr.anonym.iphotos.presentation.viewModel.FullScreenViewModel
import com.mr.anonym.iphotos.presentation.viewModel.PhotoViewModel

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
                            viewModel.downloadAndSaveImageEvent(context,model.value[0].largeImageURL?:"")
                            if (isSaved.value){
                                Toast.makeText(context, "Download successfully", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context, "Download error!", Toast.LENGTH_SHORT).show()
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