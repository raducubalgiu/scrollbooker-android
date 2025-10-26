package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scrollbooker.ui.search.SearchViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

//class SearchViewModel : ViewModel() {
//    var savedCenter: Point? by mutableStateOf(null)
//    var savedZoom: Double by mutableStateOf(10.0)
//// Add more like pitch, bearing if needed
//}

@Composable
fun Map(viewModel: SearchViewModel = viewModel()) {
    var isMapReady by remember { mutableStateOf(false) }

//    val lat by viewModel.latFlow.collectAsState()
//    val lon by viewModel.lonFlow.collectAsState()
//    val zoom by viewModel.zoomFlow.collectAsState()
//
//    val viewportState = rememberMapViewportState {
//        setCameraOptions {
//            center(Point.fromLngLat(lon.toDouble(), lat.toDouble()))
//            zoom(zoom.toDouble())
//        }
//    }
//
//    if (isMapReady) {
//        val mapViewportState = viewportState
//
//        MapboxMap(
//            modifier = Modifier.fillMaxSize(),
//            mapViewportState = mapViewportState
//            // Add other properties like styleUri = "mapbox://styles/mapbox/streets-v12",
//            // gestureSettings = ..., etc. as per your app
//        ) {
//            MapEffect(Unit) { mapView ->
//                val map = mapView.mapboxMap
//
//                map.addOnCameraChangeListener {
//                    val cam = map.cameraState
//                    viewModel.setCamera(
//                        lat = cam.center.latitude().toFloat(),
//                        lon = cam.center.longitude().toFloat(),
//                        zoom = cam.zoom.toFloat()
//                    )
//                // Save more if needed
//                }
//                map.addOnStyleLoadedListener {
//                    isMapReady = true
//                }
//            }
//            // Add your annotations, circles, etc. here if any
//        }
//    } else {
//        Box(Modifier.fillMaxSize()) {
//            CircularProgressIndicator(Modifier.align(Alignment.Center))
//        }
//    }
}