package com.example.scrollbooker.ui.search.components.map.markers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.ui.theme.Background
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions

@Composable
fun SearchMarkerSecondary(
    markerSize: Dp = 18.dp,
    borderWidth: Dp = 3.dp,
    color: Color,
    backgroundColor: Color = Background,
    coordinates: BusinessCoordinates,
    onMarkerClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

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
            allowOverlap(true)
        }
    ) {
        Canvas(
            modifier = Modifier
                .size(markerSize)
                .shadow(elevation = 4.dp, shape = CircleShape, clip = false)
        ) {
            val radius = size.minDimension / 2f
            val borderPx = borderWidth.toPx()

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.08f),
                        color.copy(alpha = 0.6f),
                        color
                    ),
                    center = center,
                    radius = radius
                )
            )

            drawCircle(
                color = backgroundColor,
                radius = radius - borderPx / 2f,
                style = Stroke(width = borderPx)
            )

            drawCircle(
                color = color,
                radius = radius / 2.2f
            )
        }
    }
}