package com.mr.anonym.iphotos.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.mr.anonym.domain.model.HitsItem
import com.mr.anonym.iphotos.R

@Composable
fun PhotosGridItem(
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit,
    onShareClick:()->Unit,
    onPhotoClick: () -> Unit,
    photosModel: HitsItem
) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .width(250.dp)
            .wrapContentHeight()
            .padding(7.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = context)
                        .data(photosModel.largeImageURL)
                        .crossfade(true)
                        .scale(Scale.FIT)
                        .build(),
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onPhotoClick() },
                    contentDescription = "photos",
                    contentScale = ContentScale.Fit
                )
            HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onFavoriteClick() }
                    ) {
                        if (isFavorite) {
                            Icon(
                                modifier = Modifier
                                    .size(45.dp),
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "",
                                tint = Color.Red
                            )
                        } else {
                            Icon(
                                modifier = Modifier
                                    .size(45.dp),
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "",
                                tint = Color.Red
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onShareClick() }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp),
                            imageVector = Icons.Default.Share,
                            contentDescription = "button share",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}