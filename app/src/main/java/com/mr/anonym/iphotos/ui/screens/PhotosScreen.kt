package com.mr.anonym.iphotos.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mr.anonym.domain.model.PhotosEntity
import com.mr.anonym.iphotos.R
import com.mr.anonym.iphotos.presentation.events.LocalDataEvent
import com.mr.anonym.iphotos.presentation.navigation.Screens
import com.mr.anonym.iphotos.presentation.utils.CheckConnection
import com.mr.anonym.iphotos.presentation.viewModel.PhotosViewModel
import com.mr.anonym.iphotos.ui.items.PhotosGridItem
import kotlinx.coroutines.delay

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosScreen(
    navController: NavController,
    viewModel: PhotosViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val checkConnection = CheckConnection(context)

    val isExpanded = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }
    val isSearchClicked = remember { mutableStateOf(false) }
    val isFavorite = remember { mutableStateOf(false) }
    val isRefresh = viewModel.isRefresh.collectAsState()
    val isConnected = checkConnection.networkStatus.collectAsState(false)

    val textPopular = stringResource(R.string.popular)
    val textLatest = stringResource(R.string.latest)
    val order = remember { mutableStateOf("popular") }
    val searchText = remember { mutableStateOf("") }

    val defaultColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    val fontColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    val loadingAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_photos_loading)
    )
    val noConnectionAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_internet_off)
    )

    val keyboardController = LocalSoftwareKeyboardController.current
    val keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefresh.value)

    val entity = viewModel.entity

    val photos = viewModel.getPhotos(
        order.value,
        if (
            searchText.value.isEmpty()
            || searchText.value.isBlank()
        )
            "cars"
        else
            searchText.value
    ).collectAsLazyPagingItems()

    val localPhotos = viewModel.localPhotos

    if (isConnected.value) {
        LaunchedEffect(Unit) {
            delay(2000)
            isLoading.value = false
        }
    } else {
        isLoading.value = false
    }
    LaunchedEffect(Unit) {
        delay(1000)
        if (localPhotos.value.isNotEmpty()) viewModel.deleteLocalPhoto()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(defaultColor)
    ) {
        MediumTopAppBar(
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 5.dp, top = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isSearchClicked.value) {
                        Text(
                            text = stringResource(R.string.app_name)
                        )
                        IconButton(
                            onClick = {
                                isSearchClicked.value = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "buttons search",
                                tint = fontColor
                            )
                        }
                    } else {
                        Spacer(Modifier.height(5.dp))
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = defaultColor,
                                unfocusedContainerColor = defaultColor,
                                focusedBorderColor = Color.LightGray,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            textStyle = TextStyle(
                                color = fontColor,
                                fontSize = 16.sp,
                            ),
                            value = searchText.value,
                            onValueChange = { searchText.value = it },
                            keyboardOptions = keyboardOptions,
                            keyboardActions = KeyboardActions {
                                val text = searchText.value.replace(oldChar = ' ', newChar = '+')
                                viewModel.getPhotos(order.value, text)
                                keyboardController?.hide()
                                isSearchClicked.value = false
                            },
                            leadingIcon = {
                                IconButton(
                                    onClick = {
                                        val text =
                                            searchText.value.replace(oldChar = ' ', newChar = '+')
                                        viewModel.getPhotos(order.value, text)
                                        keyboardController?.hide()
                                        isSearchClicked.value = false
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "icon search",
                                        tint = fontColor
                                    )
                                }
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (searchText.value.isBlank() || searchText.value.isBlank()) {
                                            isSearchClicked.value = false
                                        } else {
                                            searchText.value = ""
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "close search field",
                                        tint = fontColor
                                    )
                                }
                            }
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(
                    modifier = Modifier
                        .padding(start = 10.dp),
                    onClick = { TODO() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_download),
                        contentDescription = "button download",
                        tint = fontColor
                    )
                }
            },
            actions = {
                TopAppBarAction(
                    isExpanded.value,
                    popularText = textPopular,
                    latestText = textLatest,
                    fontColor = fontColor,
                    onPopularClick = {
                        isExpanded.value = false
                        order.value = "popular"
                        viewModel.isLoading(
                            order.value,
                            if (searchText.value.isBlank() || searchText.value.isEmpty()) "cars" else searchText.value
                        )
                    },
                    onLatestClick = {
                        isExpanded.value = false
                        order.value = "latest"
                        viewModel.isLoading(order.value, searchText.value)
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
            onRefresh = { viewModel.isLoading(order.value, searchText.value) }
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
                    } else {
                        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
                            items(
                                count = photos.itemCount,
                                key = photos.itemKey { it.toString() }
                            ) { index ->
                                val model = photos[index]
                                if (model != null) {
                                    PhotosGridItem(
                                        onPhotoClick = {
                                            viewModel.onLocalDataEvent(LocalDataEvent.InsertPhoto(
                                                photo = PhotosEntity(
                                                    id = model.id?:-1,
                                                    isFavorite = false,
                                                    imageUrl = model.largeImageURL
                                                )
                                            ))
                                            navController.navigate(Screens.PhotoScreen.route + "/${model.id}")
                                        },
                                        photosModel = model,
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