package com.example.scrollbooker.components.customized.postGrid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.shimmer.ShimmerMode
import com.example.scrollbooker.components.core.shimmer.shimmerEffect
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.theme.SurfaceBG
import timber.log.Timber

@Composable
fun PostGrid(
    post: Post,
    onNavigateToPost: () -> Unit
) {
    val isProcessing = post.mediaFiles.firstOrNull()?.readyToStream == false
    val url = post.mediaFiles.firstOrNull()?.customCoverUrl ?: post.mediaFiles.firstOrNull()?.thumbnailUrl

    Box(modifier = Modifier
        .aspectRatio(9f / 12f)
        .background(SurfaceBG)
        .clickable(enabled = !isProcessing, onClick = onNavigateToPost)
    ) {
        if (isProcessing) {
            PostGridProcessingPlaceholder()
        } else {
            AsyncImage(
                model = url,
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

            post.review?.let { review ->
                PostGridRatingBadge(
                    rating = review.rating,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }

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
                        PostGridViews(
                            viewsCount = post.counters.viewsCount
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PostGridProcessingPlaceholder() {
    Box(modifier = Modifier
        .fillMaxSize()
        .shimmerEffect(mode = ShimmerMode.DARK)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = stringResource(R.string.postProcessing),
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.postProcessing),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PostGridRatingBadge(
    rating: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Color.Black.copy(alpha = 0.45f))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color(0xFFFFC107),
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = rating.toString(),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}