package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessBoundingBox
import com.example.scrollbooker.entity.booking.business.domain.model.getMarkerColor
import com.example.scrollbooker.ui.GeoPoint
import com.example.scrollbooker.ui.search.MarkersUiState
import com.example.scrollbooker.ui.search.components.markers.SearchMarkerPrimary
import com.example.scrollbooker.ui.search.components.markers.SearchMarkerSecondary
import com.example.scrollbooker.ui.search.components.markers.SearchMarkerUserLocation
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.toCameraOptions
import kotlinx.coroutines.launch

@Composable
fun SearchMap(
    viewModel: SearchViewModel,
    cameraPosition: CameraPositionState,
    viewportState: MapViewportState,
    markersUiState: MarkersUiState,
    userLocation: GeoPoint?
) {
    val scope = rememberCoroutineScope()
    val isMapReady by viewModel.isMapReady.collectAsState()
    val isStyleLoaded by viewModel.isStyleLoaded.collectAsState()
    val markers = markersUiState.data?.results.orEmpty()

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

    val isMapLoading = !isMapReady || !isStyleLoaded

    Box(modifier = Modifier
        .fillMaxSize()
        .background(SurfaceBG)
    ) {
        if(isMapLoading) {
            Box(modifier = Modifier
                .matchParentSize()
                .background(SurfaceBG)
                .zIndex(12f),
                contentAlignment = Alignment.Center
            ) {
                SearchMapLoading()
            }
        }

        MapboxMap(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { this.alpha = alpha },
            mapViewportState = viewportState,
            scaleBar = {},
        ) {
            val secondaryMarkers = markers.filter { !it.isPrimary }
            val primaryMarkers = markers.filter { it.isPrimary }

            secondaryMarkers.forEach { m ->
                ViewAnnotation(
                    options = viewAnnotationOptions {
                        geometry(Point.fromLngLat(
                            m.coordinates.lng.toDouble(),
                            m.coordinates.lat.toDouble())
                        )
                        allowOverlap(true)
                    }
                ) {
                    SearchMarkerSecondary(color = m.getMarkerColor())
                }
            }

            primaryMarkers.forEach { m ->
                ViewAnnotation(
                    options = viewAnnotationOptions {
                        geometry(Point.fromLngLat(
                            m.coordinates.lng.toDouble(),
                            m.coordinates.lat.toDouble())
                        )
                        allowOverlap(false)
                    }
                ) {
                    SearchMarkerPrimary(
                        imageUrl = null,
                        domainColor = m.getMarkerColor(),
                        ratingsAverage = m.ratingsAverage
                    )
                }
            }

            ViewAnnotation(
                options = viewAnnotationOptions {
                    geometry(Point.fromLngLat(
                        25.978861.toDouble(),
                        44.443697.toDouble()
                    ))
                    allowOverlap(true)
                }
            ) {
                SearchMarkerUserLocation()
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
                            pitch = cameraState.pitch,
                        )
                    )
                }

                val mapIdleCancellable = mapboxMap.subscribeMapIdle {
                    val cameraState = mapboxMap.cameraState
                    val bounds = mapboxMap.coordinateBoundsForCamera(cameraState.toCameraOptions())
                    val bBox = BusinessBoundingBox(
                        minLng = bounds.west().toFloat(),
                        maxLng = bounds.east().toFloat(),
                        minLat = bounds.south().toFloat(),
                        maxLat = bounds.north().toFloat()
                    )
                    viewModel.setBBox(bBox)
                }

                val mapLoadedCancellable = mapboxMap.subscribeMapLoaded {
                    viewModel.setMapReady(true)
                }

                val styleLoadedCancellable = mapboxMap.subscribeStyleLoaded {
                    viewModel.setStyleLoaded(true)
                }

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