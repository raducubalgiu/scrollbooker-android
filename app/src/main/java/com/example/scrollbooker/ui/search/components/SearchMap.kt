package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Primary
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions

@Composable
fun SearchMap() {
    val isSystemInDarkMode = isSystemInDarkTheme()
    var viewAnnotationList1 by remember { mutableStateOf(
        listOf(
            Point.fromLngLat(26.104, 44.437),
            Point.fromLngLat(25.993856, 44.453898),
            Point.fromLngLat(26.009778, 44.451661),
            Point.fromLngLat(26.117450, 44.429056),
            Point.fromLngLat(26.094708, 44.428457),
            Point.fromLngLat(26.074734, 44.437146),
            Point.fromLngLat(26.064108, 44.432652),
            Point.fromLngLat(26.109086, 44.451537),
            Point.fromLngLat(26.146140, 44.409470),
            Point.fromLngLat(25.991507, 44.444861),
            Point.fromLngLat(25.989817, 44.363089),
        )
    ) }

    val mapState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(26.104, 44.437))
            zoom(10.0)
            pitch(60.0)
        }
    }

    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapState,
        style = { MapStyle(style = if(isSystemInDarkMode) Style.DARK else Style.STANDARD) },
        scaleBar = {},
    ) {
        viewAnnotationList1.forEachIndexed { index, point ->
            ViewAnnotation(
                options = viewAnnotationOptions {
                    geometry(point)
                    allowOverlap(true)
                }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(R.drawable.ic_location_solid),
                    contentDescription = null,
                    tint = Primary
                )
            }
        }
    }
}