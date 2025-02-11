package com.mr.anonym.iphotos.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.mr.anonym.domain.model.HitsItem

@Composable
fun PhotosGridItem(
    onPhotoClick: () -> Unit,
    photosModel: HitsItem
) {

    val context = LocalContext.current
    val isFavorite = remember { mutableStateOf( false ) }

    Box(
        modifier = Modifier
            .width(250.dp)
            .wrapContentHeight()
            .padding(7.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = context)
                        .data(photosModel.largeImageURL)
                        .crossfade(true)
                        .scale(Scale.FIT)
                        .build(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPhotoClick() },
                    contentDescription = "photos",
                    contentScale = ContentScale.Fit
                )
            }
        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//                .padding(horizontal = 10.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//        ) {
////                Icon button favorite
//            IconButton(
//                modifier = Modifier
//                    .padding(top = 5.dp),
//                onClick = {
//                    isFavorite.value = !isFavorite.value
//                    onFavoriteClick(isFavorite.value)
//                }
//            ) {
//                favoriteContent()
//            }
////
//            IconButton(
//                modifier = Modifier
//                    .padding(top = 5.dp),
//                onClick = { onShareClick() }
//            ) {
//                Icon(
//                    modifier = Modifier
//                        .size(30.dp),
//                    imageVector = Icons.Default.Share,
//                    contentDescription = "button share",
//                    tint = Color.Red
//                )
//            }
//        }
    }
}