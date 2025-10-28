package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import com.example.scrollbooker.ui.search.CameraPositionState
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.DisposableMapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.ui.search.BusinessAnnotation
import com.example.scrollbooker.ui.search.components.markers.SearchMarkerDot

@Composable
fun SearchMap(
    viewModel: SearchViewModel,
    dummyAnnotations: List<BusinessAnnotation>
) {
    val cameraPosition by viewModel.cameraPosition.collectAsState()
    val isMapReady by viewModel.isMapReady.collectAsState()
    val isStyleLoaded by viewModel.isStyleLoaded.collectAsState()
    val brush = rememberShimmerBrush()

    val viewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(cameraPosition.longitude, cameraPosition.latitude))
            zoom(cameraPosition.zoom)
            bearing(cameraPosition.bearing)
            pitch(cameraPosition.pitch)
        }
    }

    LaunchedEffect(viewportState) {
        viewportState.easeTo(
            cameraOptions = CameraOptions.Builder()
                .center(Point.fromLngLat(cameraPosition.longitude, cameraPosition.latitude))
                .zoom(cameraPosition.zoom)
                .bearing(cameraPosition.bearing)
                .pitch(cameraPosition.pitch)
                .build()
        )
    }

    val isReady = isMapReady && isStyleLoaded

    Box(modifier = Modifier
        .fillMaxSize()
        .background(SurfaceBG)
    ) {
        if(!isReady) {
            Box(modifier = Modifier
                .matchParentSize()
                .background(SurfaceBG)
                .background(brush)
                .zIndex(12f)
            )
        }

        MapboxMap(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { this.alpha = alpha },
            mapViewportState = viewportState,
            scaleBar = {},
        ) {
            dummyAnnotations.map { a ->
                ViewAnnotation(
                    options = viewAnnotationOptions {
                        geometry(Point.fromLngLat(
                            a.longitude.toDouble(),
                            a.latitude.toDouble())
                        )
                        allowOverlap(true)
                    }
                ) {
                    SearchMarkerDot(category = a.businessCategory)
                }
            }

            DisposableMapEffect(Unit) { mapView ->
                viewModel.setMapReady(false)
                val mapboxMap = mapView.mapboxMap

                val cameraCancellable = mapboxMap.subscribeCameraChanged {
                    val cameraState = mapboxMap.cameraState
                    val center = cameraState.center

                    viewModel.updateCamera(
                        CameraPositionState(
                            latitude = center.latitude(),
                            longitude = center.longitude(),
                            zoom = cameraState.zoom,
                            bearing = cameraState.bearing,
                            pitch = cameraState.pitch
                        )
                    )
                }

                val mapLoadedCancellable = mapboxMap.subscribeMapLoaded {
                    viewModel.setMapReady(true)
                }

                val styleLoadedCancellable = mapboxMap.subscribeStyleLoaded {
                    viewModel.setStyleLoaded(true)
                }

                val mapIdleCancellable = mapboxMap.subscribeMapIdle {  }

                onDispose {
                    cameraCancellable.cancel()
                    mapLoadedCancellable.cancel()
                    styleLoadedCancellable.cancel()
                    mapIdleCancellable.cancel()
                }
            }

        }
    }
}