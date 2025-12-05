package com.example.scrollbooker.ui.search.components
import androidx.annotation.OptIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.search.SearchPlayerViewModel
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium

@OptIn(UnstableApi::class)
@Composable
fun SearchBusinessCard(
    viewModel: SearchPlayerViewModel,
    id: Int,
    url: String,
    coverUrl: String,
    name: String,
    rating: Double,
    reviews: Int,
    location: String,
    services: List<ServiceUiModel>,
    modifier: Modifier = Modifier
) {
    val playerState by viewModel.playerState.collectAsState()

    val shouldShowVideoSurface =
            playerState.isPlaying &&
            playerState.currentId == id &&
            playerState.isReady &&
            !playerState.isBuffering

    val currentOnReleasePlayer by rememberUpdatedState(viewModel::releasePlayer)

    val videoAlpha by animateFloatAsState(
        targetValue = if(shouldShowVideoSurface && playerState.isFirstFrameRendered) 1f else 0f,
        label = "videoAlpha"
    )

    LifecycleStartEffect(true) {
        onStopOrDispose {
            currentOnReleasePlayer(id)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .padding(horizontal = BasePadding)
    ) {

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = coverUrl,
                contentDescription = name,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )

//            if(shouldShowVideoSurface) {
//                AndroidView(
//                    modifier = Modifier
//                        .matchParentSize()
//                        .graphicsLayer { alpha = videoAlpha },
//                    factory = { context ->
//                        PlayerView(context).apply {
//                            player = viewModel.player
//                            useController = false
//                            controllerAutoShow = false
//                            controllerShowTimeoutMs = 0
//
//                            setShowBuffering(SHOW_BUFFERING_NEVER)
//
//                            layoutParams = ViewGroup.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                            )
//                        }
//                    },
//                    update = { playerView ->
//                        playerView.player = viewModel.player
//                        playerView.resizeMode =
//                            AspectRatioFrameLayout.RESIZE_MODE_FILL
//                    }
//                )
//            }

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
        }

        Spacer(Modifier.height(8.dp))

        // Detalii business
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = name,
                style = titleMedium.copy(fontWeight = FontWeight.SemiBold),
                fontSize = 18.sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                RatingsStars(
                    rating = 4.5f,
                    maxRating = 5,
                    starSize = 20.dp
                )

                Text(
                    text = " $rating ",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "($reviews)",
                    color = Color.Gray
                )
            }

            Text(
                text = location,
                color = Color.Gray
            )

            Spacer(Modifier.height(8.dp))

            // Servicii
            services.take(3).forEach { service ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(service.name, style = bodyMedium)
                        Text(
                            service.duration,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Text(
                        "${service.price} RON",
                        style = bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(6.dp))
            }

            Text(
                text = "See more",
                style = bodySmall.copy(color = Color(0xFF5E35B1)),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

data class ServiceUiModel(
    val name: String,
    val duration: String,
    val price: Int
)

//@Preview(
//    name = "Light",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Preview(
//    name = "Dark",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun BusinessCardPreview() {
//    ScrollBookerTheme(
//        themePreferenceEnum = ThemePreferenceEnum.DARK
//    ) {
//        SearchBusinessCard(
//            imageUrl = "https://picsum.photos/600/300",
//            name = "Ida Spa Dorobanti",
//            rating = 5.0,
//            reviews = 4327,
//            location = "Sector 1, București",
//            services = listOf(
//                ServiceUiModel("NEW Intensive Muscle Release Massage", "1 hr - 1 hr 30 mins", 280),
//                ServiceUiModel("King Balinese Massage", "1 hr - 1 hr 30 mins", 280),
//                ServiceUiModel("Neuro Sedative Relaxing Massage", "1 hr - 1 hr 30 mins", 290),
//            )
//        )
//    }
//}