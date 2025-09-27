package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.search.SearchViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions

@Composable
fun RememberedMap(
    viewModel: SearchViewModel
) {
    val density = LocalDensity.current

    //val composeInit = remember(density) { ComposeMapInitOptions(density, textureView = true) }

    val lat by viewModel.latFlow.collectAsState()
    val lon by viewModel.lonFlow.collectAsState()
    val zoom by viewModel.zoomFlow.collectAsState()

    val viewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(lon.toDouble(), lat.toDouble()))
            zoom(zoom.toDouble())
        }
    }

    var first by remember { mutableStateOf(true) }

    LaunchedEffect(lat, lon, zoom) {
        val opts = CameraOptions.Builder()
            .center(Point.fromLngLat(lon.toDouble(), lat.toDouble()))
            .zoom(zoom.toDouble())
            .build()

        if(first) {
            first = false
            viewportState.setCameraOptions {
                center(opts.center)
                zoom(opts.zoom!!)
            }
        } else {
            viewportState.easeTo(
                cameraOptions = opts,
                animationOptions = MapAnimationOptions.mapAnimationOptions {
                    duration(250)
                }
            )
        }
    }

    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        //composeMapInitOptions = composeInit,
        mapViewportState = viewportState,
        style = { MapboxStandardStyle() },
    )

    LaunchedEffect(viewportState.cameraState) {
        val cam = viewportState.cameraState
        cam?.let {
            viewModel.setCamera(
                lat = cam.center.latitude().toFloat(),
                lon = cam.center.longitude().toFloat(),
                zoom = cam.zoom.toFloat()
            )
        }
    }
}