package com.example.scrollbooker.ui.search.components.map.markers
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.request.ImageRequest
import com.example.scrollbooker.R
import timber.log.Timber

@Composable
fun SearchMarkerPrimary(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    domainColor: Color,
    baseAvatarSize: Dp,
    animatedAvatarSize: Dp,
    animatedElevation: Float
) {
    val context = LocalContext.current
    val imageRequest = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .build()
    }

    val ringWidth = 3.dp
    val whiteRingWidth = 2.dp

    val innerSize = animatedAvatarSize - ringWidth - whiteRingWidth - 4.dp

    val pointerWidth = baseAvatarSize * 0.42f
    val pointerHeight = 8.dp
    val pointerOffsetY = (-2).dp

    Column(
        modifier = modifier.padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(animatedAvatarSize)
                .graphicsLayer {
                    shadowElevation = animatedElevation
                    shape = CircleShape
                    clip = false
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val outerRadius = size.minDimension / 2f
                val ringPx = ringWidth.toPx()
                val whiteRingPx = whiteRingWidth.toPx()

                drawCircle(
                    brush = Brush.sweepGradient(
                        listOf(
                            domainColor.copy(alpha = 0.9f),
                            domainColor.copy(alpha = 0.6f),
                            domainColor.copy(alpha = 0.9f),
                        )
                    ),
                    radius = outerRadius - ringPx / 2f,
                    center = center,
                    style = Stroke(width = ringPx)
                )

                drawCircle(
                    color = Color.White,
                    radius = outerRadius - ringPx - whiteRingPx / 2f,
                    center = center,
                    style = Stroke(width = whiteRingPx)
                )
            }

            Box(
                modifier = Modifier.size(innerSize),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = "Marker Primary",
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_user),
                    error = painterResource(R.drawable.ic_user),
                    onError = { Timber.tag("Marker Avatar Error").e("ERROR: ${it.result.throwable.message}") }
                )
            }
        }

        Canvas(
            modifier = Modifier
                .offset(y = pointerOffsetY)
                .size(
                    width = pointerWidth,
                    height = pointerHeight
                )
        ) {
            val w = size.width
            val h = size.height

            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(w, 0f)
                lineTo(w / 2f, h)
                close()
            }

            drawPath(
                path = path,
                color = domainColor
            )
        }
    }
}