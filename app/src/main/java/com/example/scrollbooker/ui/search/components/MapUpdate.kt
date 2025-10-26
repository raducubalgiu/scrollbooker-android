package com.example.scrollbooker.ui.search.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.search.SearchViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraChangedCallback
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.StyleLoadedCallback
import com.mapbox.maps.extension.compose.ComposeMapInitOptions
import com.mapbox.maps.extension.compose.DisposableMapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions

@Composable
fun MapUpdate(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
//    val lat by viewModel.latFlow.collectAsState()
//    val lon by viewModel.lonFlow.collectAsState()
//    val zoom by viewModel.zoomFlow.collectAsState()
//
//    // Init options/stil – stabile (nu le schimba între recompoziții)
//    //val composeInit: ComposeMapInitOptions = remember { ComposeMapInitOptions() }
//    val viewportState = rememberMapViewportState {
//        setCameraOptions {
//            center(Point.fromLngLat(lon.toDouble(), lat.toDouble()))
//            zoom(zoom.toDouble())
//        }
//    }
//
//    // VM -> Map: prima dată snap, apoi animație
//    var first by remember { mutableStateOf(true) }
//    LaunchedEffect(lat, lon, zoom) {
//        val opts = CameraOptions.Builder()
//            .center(Point.fromLngLat(lon.toDouble(), lat.toDouble()))
//            .zoom(zoom.toDouble())
//            .build()
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
//    // Harta
//    MapboxMap(
//        modifier = modifier,
//        //composeMapInitOptions = composeInit,
//        mapViewportState = viewportState,
//        style = { MapboxStandardStyle() }
//    )
//
//    // --- SUBSCRIBE pe evenimentele Mapbox v11+ ---
//
//    // 1) Style loaded -> poți marca harta ca "ready", poți face init de straturi/annotații
//    DisposableMapEffect(Unit) { mapView ->
//        val map = mapView.mapboxMap
//        val styleCb = StyleLoadedCallback {
//        // e.g., poți seta camera inițială fără animație sau să adaugi layer-e
//        // map.setCamera(CameraOptions.Builder().center(...).zoom(...).build())
//        // viewModel.markMapReady()
//        }
//        map.subscribeStyleLoaded(styleCb)
//        onDispose {
//            //map.unsubscribeStyleLoaded(styleCb)
//        }
//    }
//
//// 2) Camera changed -> sync Map -> ViewModel (salvezi lat/lon/zoom)
//    DisposableMapEffect(Unit) { mapView ->
//        val map = mapView.mapboxMap
//        val cameraCb = CameraChangedCallback {
//            val cam = map.cameraState
//            viewModel.setCamera(
//                lat = cam.center.latitude().toFloat(),
//                lon = cam.center.longitude().toFloat(),
//                zoom = cam.zoom.toFloat()
//            )
//        }
//        map.subscribeCameraChanged(cameraCb)
//        onDispose {
//            //map.unsubscribeCameraChanged(cameraCb)
//        }
//    }
}