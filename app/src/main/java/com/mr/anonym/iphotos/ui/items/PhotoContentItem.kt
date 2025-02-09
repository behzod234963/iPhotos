package com.mr.anonym.iphotos.ui.items

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mr.anonym.domain.model.HitsItem
import com.mr.anonym.domain.model.PhotosEntity
import com.mr.anonym.iphotos.R

@Composable
fun PhotoContentComponent(
    context: Context,
    fontColor: Color,
    model:HitsItem,
    entity: PhotosEntity,
    onContentPhotoClick:()->Unit,
    onFavoriteClick:()->Unit,
    onDownloadClick: ()->Unit,
    onShareClick:()->Unit
) {

    val scrollState = rememberScrollState()
    val isDownloadClick = remember { mutableStateOf( false ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .clickable { onContentPhotoClick() },
                model = ImageRequest.Builder(context)
                    .data(model.largeImageURL)
                    .size(width = model.imageWidth?:5, height = model.imageHeight?:5)
                    .build(),
                contentScale = ContentScale.Fit,
                contentDescription = "content photo",
            )
        }
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                onClick = { onFavoriteClick() }
            ) {
                if(entity.isFavorite){
                    Icon(
                        modifier = Modifier
                            .size(40.dp),
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "button favorite",
                        tint = Color.Red
                    )
                }else{
                    Icon(
                        modifier = Modifier
                            .size(40.dp),
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "button favorite",
                        tint = Color.Red
                    )
                }
            }
            IconButton(
                onClick = { onDownloadClick() }
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp),
                    painter = painterResource(R.drawable.ic_download),
                    contentDescription = "button download",
                    tint = Color.Red
                )
            }
            IconButton(
                onClick = { onShareClick() }
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp),
                    imageVector = Icons.Default.Share,
                    contentDescription = "button share",
                    tint = Color.Red
                )
            }
        }
        HorizontalDivider()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row {
                Row (
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(40.dp)
                        .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "likes",
                        tint = Color.Red
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = model.likes.toString(),
                        color = fontColor,
                        fontSize = 16.sp
                    )
                }
                Spacer(Modifier.width(10.dp))
                Row (
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(40.dp)
                        .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = painterResource(R.drawable.ic_download),
                        contentDescription = "downloads",
                        tint = Color.Red
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = model.downloads.toString(),
                        color = fontColor,
                        fontSize = 16.sp
                    )
                }
                Row (
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(40.dp)
                        .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "comments",
                        tint = Color.Red
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = model.comments.toString(),
                        color = fontColor,
                        fontSize = 16.sp
                    )
                }
                Spacer(Modifier.width(10.dp))
                Row (
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(40.dp)
                        .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = painterResource(R.drawable.ic_views),
                        contentDescription = "views",
                        tint = Color.Red
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = model.views.toString(),
                        color = fontColor,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Spacer(Modifier.width(10.dp))
                Row (
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = painterResource(R.drawable.ic_tag),
                        contentDescription = "tags",
                        tint = Color.Red
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = model.tags.toString(),
                        color = fontColor,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}