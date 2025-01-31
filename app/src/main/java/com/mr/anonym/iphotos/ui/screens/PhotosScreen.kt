package com.mr.anonym.iphotos.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mr.anonym.data.local.dataStore.DataStoreInstance
import com.mr.anonym.iphotos.R
import com.mr.anonym.iphotos.presentation.navigation.Screens
import com.mr.anonym.iphotos.presentation.utils.CheckConnection
import com.mr.anonym.iphotos.presentation.viewModel.PhotosViewModel
import com.mr.anonym.iphotos.ui.items.PhotosGridItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosScreen(
    navController: NavController,
    viewModel: PhotosViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val dataStore = DataStoreInstance(context)
    val checkConnection = CheckConnection(context)

    val isExpanded = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }
    val isRefresh = viewModel.isRefresh.collectAsState()
    val isConnected = checkConnection.networkStatus.collectAsState(false)

    val textPopular = stringResource(R.string.popular)
    val textLatest = stringResource(R.string.latest)

    val defaultColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    val fontColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    val loadingAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_photos_loading)
    )
    val noConnectionAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_internet_off)
    )

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefresh.value)

    var photos = viewModel.getPhotos("popular").collectAsLazyPagingItems()

    if (isConnected.value) {
        LaunchedEffect(Unit) {
            delay(2000)
            isLoading.value = false
        }
    } else {
        isLoading.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(defaultColor)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.app_name)
                )
            },
            navigationIcon = { TODO() },
            actions = {
                TopAppBarAction(
                    isExpanded.value,
                    popularText = textPopular,
                    latestText = textLatest,
                    fontColor = fontColor,
                    onPopularClick = {
                        isExpanded.value = false
                        viewModel.isLoading("popular")
                    },
                    onLatestClick = {
                        isExpanded.value = false
                        viewModel.isLoading("latest")
                    },
                    onDismissRequest = { isExpanded.value = false },
                    onExpandedChange = { isExpanded.value = it }
                )
            }
        )
        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize(),
            state = swipeRefreshState,
            onRefresh = { viewModel.isLoading("popular") }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (!isConnected.value) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        item {
                            LottieAnimation(
                                modifier = Modifier
                                    .fillMaxSize(),
                                composition = noConnectionAnimation.value,
                            )
                        }
                    }
                } else {
                    if (isLoading.value) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LottieAnimation(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                composition = loadingAnimation.value,
                                restartOnPlay = true,
                                iterations = LottieConstants.IterateForever
                            )
                        }
                    }else{
                        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Adaptive(200.dp)) {
                            items(
                                count = photos.itemCount,
                                key = photos.itemKey { it.toString() }
                            ) { index ->
                                val model = photos[index]
                                if (model != null) {
                                    PhotosGridItem(
                                        isFavorite = false,
                                        onFavoriteClick = { TODO() },
                                        onShareClick = { TODO() },
                                        onPhotoClick = { navController.navigate(Screens.PhotoScreen.route + "/${model.id}") },
                                        photosModel = model,
                                    )
                                }
                            }
                            Log.d("NetworkLogging", "load: ${photos.itemCount}")
                            if (photos.loadState.append is LoadState.Loading) {
                                item {
                                    LottieAnimation(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        composition = loadingAnimation.value,
                                        restartOnPlay = true,
                                        iterations = LottieConstants.IterateForever
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarAction(
    isExpanded: Boolean,
    popularText: String,
    latestText: String,
    fontColor: Color,
    onPopularClick: () -> Unit,
    onLatestClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onExpandedChange: (Boolean) -> Unit
) {

    val isPopularClick = remember { mutableStateOf(true) }
    ExposedDropdownMenuBox(
        modifier = Modifier
            .width(125.dp),
        expanded = isExpanded,
        onExpandedChange = {
            onExpandedChange(it)
        }
    ) {
        OutlinedTextField(
            value = if (isPopularClick.value) popularText else latestText,
            onValueChange = {},
            modifier = Modifier
                .menuAnchor(),
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onDismissRequest() }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = popularText,
                        color = fontColor,
                        fontSize = 16.sp
                    )
                },
                onClick = {
                    onPopularClick()
                    isPopularClick.value = true
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = latestText,
                        color = fontColor,
                        fontSize = 16.sp
                    )
                },
                onClick = {
                    onLatestClick()
                    isPopularClick.value = false
                }
            )
        }
    }
}