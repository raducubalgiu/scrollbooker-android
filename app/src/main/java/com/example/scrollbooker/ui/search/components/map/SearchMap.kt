package com.example.scrollbooker.ui.search.components.map

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.core.enums.toDomainColor
import com.example.scrollbooker.ui.search.CameraPositionState
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.DisposableMapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessBoundingBox
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.ui.GeoPoint
import com.example.scrollbooker.ui.search.MarkersUiState
import com.example.scrollbooker.ui.search.components.SearchMapActions
import com.example.scrollbooker.ui.search.components.SearchMapLoading
import com.example.scrollbooker.ui.search.components.map.markers.SearchMarkerPrimary
import com.example.scrollbooker.ui.search.components.map.markers.SearchMarkerSecondary
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.ContextMode
import com.mapbox.maps.MapOptions
import com.mapbox.maps.applyDefaultParams
import com.mapbox.maps.extension.compose.ComposeMapInitOptions
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.toCameraOptions
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SearchMap(
    viewModel: SearchViewModel,
    markersUiState: MarkersUiState,
    userLocation: GeoPoint?,
    isMapLoading: Boolean,
    onSheetExpand: () -> Unit,
    onNavigateToBusinessProfile: (Int) -> Unit,
    paddingBottom: Dp
) {
    val density = LocalDensity.current
    val markers = markersUiState.data?.results.orEmpty()
    val scope = rememberCoroutineScope()

    val selectedMarker by viewModel.selectedMarker.collectAsStateWithLifecycle()

    var mapStyle by rememberSaveable {
        mutableStateOf("mapbox://styles/radubalgiu/cmip1r7g000pm01sca0vz7dxp")
    }

    val cameraPosition by viewModel.cameraPosition.collectAsState()

    val viewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(cameraPosition.longitude, cameraPosition.latitude))
            zoom(cameraPosition.zoom)
            bearing(cameraPosition.bearing)
            pitch(cameraPosition.pitch)
        }
    }

    val mapInitOptions = remember {
        ComposeMapInitOptions(
            mapOptions = MapOptions.Builder()
                .applyDefaultParams(density.density)
                .contextMode(ContextMode.SHARED)
                .build(),
            textureView = true,
            antialiasingSampleCount = 4,
            mapName = "search_map"
        )
    }

    fun flyToUserLocation() {
        val loc = GeoPoint(
            lat = 44.443697,
            lng = 25.978861
        )
        scope.launch {
            val newZoom = 13.5
            val cameraOptions = CameraOptions.Builder()
                .center(Point.fromLngLat(loc.lng, loc.lat))
                .zoom(newZoom)
                .bearing(cameraPosition.bearing)
                .pitch(cameraPosition.pitch)
                .build()
            viewportState.flyTo(cameraOptions)
            viewModel.updateCamera(
                cameraPosition.copy(
                    latitude = loc.lat,
                    longitude = loc.lng,
                    zoom = newZoom
                )
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SearchMapActions(
            paddingBottom = paddingBottom,
            onFlyToUserLocation = { flyToUserLocation() },
            onSheetExpand = onSheetExpand
        )

        BusinessPreviewCard(
            selectedMarker = selectedMarker,
            onCloseClick = { viewModel.setSelectedMarker(null) },
            onNavigateToBusinessProfile = onNavigateToBusinessProfile,
            isVisible = selectedMarker != null,
            paddingBottom = paddingBottom
        )

        if(isMapLoading) {
            Box(modifier = Modifier
                .matchParentSize()
                .background(SurfaceBG)
                .zIndex(12f),
                contentAlignment = Alignment.Center
            ) { SearchMapLoading() }
        }

        MapboxMap(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { this.alpha = alpha },
            mapViewportState = viewportState,
            composeMapInitOptions = mapInitOptions,
            style = { MapStyle(style = mapStyle) },
            scaleBar = {},
        ) {
            val secondaryMarkers = markers.filter { !it.isPrimary }
            val primaryMarkers = markers.filter { it.isPrimary }

            secondaryMarkers.forEach { m ->
                SearchMarkerSecondary(
                    color = m.businessShortDomain.toDomainColor(),
                    coordinates = m.coordinates,
                    onMarkerClick = { viewModel.setSelectedMarker(m) }
                )
            }

            primaryMarkers.forEach { m ->
                SearchMarkerPrimary(
                    imageUrl = m.owner.avatar,
                    domainColor = m.businessShortDomain.toDomainColor(),
                    ratingsAverage = m.owner.ratingsAverage,
                    coordinates = m.coordinates,
                    onMarkerClick = { viewModel.setSelectedMarker(m) },
                )
            }

//            ViewAnnotation(
//                options = viewAnnotationOptions {
//                    geometry(Point.fromLngLat(
//                        25.978861.toDouble(),
//                        44.443697.toDouble()
//                    ))
//                    allowOverlap(true)
//                }
//            ) { SearchMarkerUserLocation() }

            DisposableMapEffect(Unit) { mapView ->
                viewModel.setMapReady(false)
                val mapboxMap = mapView.mapboxMap

                var lastRequestedCamera: CameraPositionState? = null

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
                    val center = cameraState.center

                    val currentCamera = CameraPositionState(
                        latitude = center.latitude(),
                        longitude = center.longitude(),
                        zoom = cameraState.zoom,
                        bearing = cameraState.bearing,
                        pitch = cameraState.pitch,
                    )

                    if(shouldRequestNewData(lastRequestedCamera, currentCamera)) {
                        lastRequestedCamera = currentCamera

                        val bounds = mapboxMap.coordinateBoundsForCamera(cameraState.toCameraOptions())
                        val bBox = BusinessBoundingBox(
                            minLng = bounds.west().toFloat(),
                            maxLng = bounds.east().toFloat(),
                            minLat = bounds.south().toFloat(),
                            maxLat = bounds.north().toFloat()
                        )
                        viewModel.onMapIdle(bBox)
                    }
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

private const val MIN_ZOOM_DELTA = 0.3f
private const val MIN_MOVE_METERS = 1000f

private fun shouldRequestNewData(
    last: CameraPositionState?,
    current: CameraPositionState
): Boolean {
    if (last == null) return true

    if (abs(current.zoom - last.zoom) >= MIN_ZOOM_DELTA) return true

    val distance = distanceInMeters(
        last.latitude,
        last.longitude,
        current.latitude,
        current.longitude
    )

    return distance >= MIN_MOVE_METERS
}

private fun distanceInMeters(
    lat1: Double,
    lng1: Double,
    lat2: Double,
    lng2: Double
): Float {
    val result = FloatArray(1)
    Location.distanceBetween(lat1, lng1, lat2, lng2, result)
    return result[0]
}