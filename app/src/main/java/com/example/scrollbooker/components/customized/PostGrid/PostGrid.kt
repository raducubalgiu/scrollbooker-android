package com.example.scrollbooker.components.customized.PostGrid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.theme.SurfaceBG
import timber.log.Timber

@Composable
fun PostGrid(
    post: Post,
    viewsCount: Float = 14.200f,
    onNavigateToPost: (Int) -> Unit
) {
    Box(modifier = Modifier
        .aspectRatio(9f / 12f)
        .background(SurfaceBG)
        .clickable(onClick = {
            onNavigateToPost(post.id)
        })
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(post.mediaFiles.first().thumbnailUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            contentDescription = "Post Grid",
            contentScale = ContentScale.Crop,
            onError = { Timber.tag("Post Grid Error").e("ERROR: ${it.result.throwable.message}") },
            modifier = Modifier.fillMaxSize()
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.2f),
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.4f)
                    )
                )
            )
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    //                PostGridLabel(
//                    lastMinute = post.lastMinute,
//                    product = post.product
//                )
                }
                Column {
                    //post.product?.let { PostGridCover(post.product) }
                    PostGridViews(viewsCount)
                }
            }
        }
    }
}