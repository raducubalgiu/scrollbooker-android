package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.scrollbooker.ui.search.SearchViewModel
import com.google.maps.android.compose.rememberMarkerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.google.android.gms.maps.CameraUpdateFactory

@Composable
fun SearchGoogleMap(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel
) {
    val vmCamera by viewModel.camera.collectAsState()
    val isMapLoaded by viewModel.isMapLoaded.collectAsState()

    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(vmCamera.lat, vmCamera.lng),
            vmCamera.zoom
        ).let { pos ->
            CameraPosition(pos.target, pos.zoom, vmCamera.tilt, vmCamera.bearing)
        }
    }

    LaunchedEffect(vmCamera) {
        val target = LatLng(vmCamera.lat, vmCamera.lng)
        val desired = CameraPosition(target, vmCamera.zoom, vmCamera.tilt, vmCamera.bearing)
        if(cameraState.position != desired) {
            cameraState.animate(CameraUpdateFactory.newCameraPosition(desired))
        }
    }

    LaunchedEffect(cameraState) {
        snapshotFlow { cameraState.isMoving }
            .collect { moving ->
                viewModel.updateFromMap(cameraState.position)
            }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraState,
        onMapLoaded = {
            viewModel.onMapLoaded()
        }
    ) {
        Marker(
            state = rememberMarkerState(position=LatLng(44.450507, 25.993102)),
            title = "Singapore",
            snippet = "Marker in Singapore"
        )
    }

    if(!isMapLoaded) {
        Box(Modifier.fillMaxSize().background(SurfaceBG))
    }
}