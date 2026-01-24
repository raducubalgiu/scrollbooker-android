package com.example.scrollbooker.ui.search.components.map.markers
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.enums.toDomainColor
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions

@Composable
fun SearchMarker(
    isPrimary: Boolean,
    isSelected: Boolean,
    marker: BusinessMarker,
    baseAvatarSize: Dp = 58.dp,
    onClick: () -> Unit
) {
    val showPrimaryUi = isPrimary || isSelected

    val animatedAvatarSize by animateDpAsState(
        targetValue = if (isSelected) baseAvatarSize * 1.28f else baseAvatarSize,
        animationSpec = tween(
            durationMillis = 180,
            easing = FastOutSlowInEasing
        ),
        label = "avatarSize"
    )

    val animatedElevation by animateFloatAsState(
        targetValue = if (isSelected) 14f else 6f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "elevation"
    )

    ViewAnnotation(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        options = viewAnnotationOptions {
            geometry(Point.fromLngLat(marker.coordinates.lng.toDouble(), marker.coordinates.lat.toDouble()))
            allowOverlap(showPrimaryUi)
        }
    ) {
        Box(contentAlignment = Alignment.BottomCenter) {
            SearchMarkerSecondary(color = marker.businessShortDomain.toDomainColor())

            AnimatedVisibility(
                visible = showPrimaryUi,
                enter = fadeIn(tween(180)) + scaleIn(
                    initialScale = 0.9f,
                    animationSpec = tween(220, easing = FastOutSlowInEasing),
                    transformOrigin = TransformOrigin(0.5f, 1f)
                ),
                exit = fadeOut(tween(180)) + scaleOut(
                    animationSpec = tween(220, easing = FastOutSlowInEasing),
                    transformOrigin = TransformOrigin(0.5f, 1f)
                )
            ) {
                SearchMarkerPrimary(
                    imageUrl = marker.mediaPreview?.thumbnailUrl,
                    domainColor = marker.businessShortDomain.toDomainColor(),
                    baseAvatarSize = baseAvatarSize,
                    animatedAvatarSize = animatedAvatarSize,
                    animatedElevation = animatedElevation,
                )
            }
        }
    }
}


