package com.example.scrollbooker.ui.search.components.map.markers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.example.scrollbooker.core.util.rememberScrollBookerImageLoader
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import timber.log.Timber

@Composable
fun SearchMarkerPrimary(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    domainColor: Color,
    ratingsAverage: Float,
    coordinates: BusinessCoordinates,
    avatarSize: Dp = 56.dp,
    showPointer: Boolean = true,
    onMarkerClick: () -> Unit
) {
    val ringWidth = 3.dp
    val whiteRingWidth = 2.dp
    val pointerHeight = if (showPointer) 8.dp else 0.dp
    val interactionSource = remember { MutableInteractionSource() }
    
    val context = LocalContext.current
    val imageLoader = rememberScrollBookerImageLoader()
    val imageRequest = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .size(112)
            .build()
    }

    ViewAnnotation(
        modifier = Modifier.clickable(
            onClick = onMarkerClick,
            interactionSource = interactionSource,
            indication = null
        ),
        options = viewAnnotationOptions {
            geometry(Point.fromLngLat(
                coordinates.lng.toDouble(),
                coordinates.lat.toDouble())
            )
            allowOverlap(false)
        }
    ) {
        Column(
            modifier = modifier.padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .graphicsLayer {
                        shadowElevation = 6f
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

                val innerSize = avatarSize -
                        ringWidth -
                        whiteRingWidth -
                        4.dp

                Box(
                    modifier = Modifier.size(innerSize),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageRequest,
                        imageLoader = imageLoader,
                        contentDescription = "Marker Primary",
                        modifier = Modifier
                            .matchParentSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_user),
                        error = painterResource(R.drawable.ic_user),
                        onError = { Timber.tag("Marker Avatar Error").e("ERROR: ${it.result.throwable.message}") }
                    )

//                Box(
//                    modifier = Modifier
//                        .align(Alignment.TopCenter)
//                        .offset(y = (-10).dp)
//                        .background(
//                            color = Color.White.copy(alpha = 0.85f),
//                            shape = CircleShape
//                        )
//                        .padding(horizontal = 6.dp, vertical = 4.dp)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            modifier = Modifier.size(10.dp),
//                            painter = painterResource(R.drawable.ic_star_solid),
//                            contentDescription = null,
//                            tint = Primary
//                        )
//                        Spacer(Modifier.width(2.dp))
//                        Text(
//                            text = ratingsAverage.toFixedDecimals(1),
//                            style = labelSmall,
//                            color = Color.Black,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    }
//                }
                }
            }

            if (showPointer) {
                Canvas(
                    modifier = Modifier
                        .offset(y = (-2).dp)
                        .size(width = avatarSize * 0.42f, height = pointerHeight)
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
    }
}