package com.example.scrollbooker.ui.search.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.search.SearchViewModel
import com.mapbox.common.Cancelable
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraChangedCallback
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.StyleLoadedCallback
import com.mapbox.maps.extension.compose.DisposableMapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.animation.MapAnimationOptions

@Composable
fun MapSearch(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
//    // --- Camera din VM ---
//    val lat by viewModel.latFlow.collectAsState()
//    val lon by viewModel.lonFlow.collectAsState()
//    val zoom by viewModel.zoomFlow.collectAsState()
//
//    // --- Viewport state + cameră inițială ---
//    val viewportState = rememberMapViewportState {
//        setCameraOptions {
//            center(Point.fromLngLat(lon.toDouble(), lat.toDouble()))
//            zoom(zoom.toDouble())
//        }
//    }
//
//    // Guard anti-buclă (VM -> Map -> VM)
//    var ignoreNext by remember { mutableStateOf(false) }
//
//    // VM -> Map: prima dată snap, apoi animație scurtă
//    var first by remember { mutableStateOf(true) }
//
//    LaunchedEffect(lat, lon, zoom) {
//        val opts = CameraOptions.Builder()
//            .center(Point.fromLngLat(lon.toDouble(), lat.toDouble()))
//            .zoom(zoom.toDouble())
//            .build()
//
//        ignoreNext = true // consumăm următorul event din subscribeCameraChanged
//
//        if (first) {
//            first = false
//            viewportState.setCameraOptions {
//                center(opts.center)
//                zoom(opts.zoom!!)
//            }
//        } else {
//            viewportState.easeTo(
//                cameraOptions = opts,
//                animationOptions = MapAnimationOptions.mapAnimationOptions { duration(250) }
//            )
//        }
//    }
//
//    // --- Harta propriu-zisă (stabilă) ---
//    MapboxMap(
//        modifier = modifier,
//        //composeMapInitOptions = composeInit,
//        mapViewportState = viewportState,
//    ) {
//        DisposableMapEffect(Unit) { mapView ->
//            val map = mapView.mapboxMap
//            var disposed = false
//
//            map.getStyle { _ ->
//                if(!disposed) viewModel.setMapReady(true)
//            }
//
//            val styleCancelable: Cancelable = map.subscribeStyleLoaded(
//                StyleLoadedCallback {
//                    if(!disposed) viewModel.setMapReady(true)
//                }
//            )
//
//            onDispose {
//                disposed = true
//                styleCancelable.cancel()
//            }
//        }
//
//        DisposableMapEffect(Unit) { mapView ->
//            val map = mapView.mapboxMap
//
//            var lastTs = 0L
//            val minIntervalMs = 80L
//
//            val cameraCancelable: Cancelable = map.subscribeCameraChanged(
//                CameraChangedCallback {
//                    // daca muti tu camera din code, seteaza un flag ignoreNext = true inainte
//                    if (ignoreNext) { ignoreNext = false; return@CameraChangedCallback }
//
//                    val now = System.currentTimeMillis()
//                    if (now - lastTs < minIntervalMs) return@CameraChangedCallback
//                    lastTs = now
//
//                    val cam = map.cameraState
//
//                    viewModel.setCamera(
//                        lat = cam.center.latitude().toFloat(),
//                        lon = cam.center.longitude().toFloat(),
//                        zoom = cam.zoom.toFloat()
//                    )
//                }
//            )
//
//            onDispose { cameraCancelable.cancel() }
//        }
//    }
}