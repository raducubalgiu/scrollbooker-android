package com.example.scrollbooker.screens.search
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.scrollbooker.R
import com.example.scrollbooker.screens.search.SearchViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import timber.log.Timber

@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    var search by remember { mutableStateOf("") }
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()
    val mapStyleOptionsKey = if(isDarkTheme) "dark" else "light"

    Timber.d("IS SYSTEM IN DARK MODE!!! $isDarkTheme")

    val mapStyleOptions = remember(mapStyleOptionsKey) {
        MapStyleOptions.loadRawResourceStyle(
            context,
            if(isDarkTheme) R.raw.map_style_dark else R.raw.map_style_light
        )
    }

    Column(Modifier.fillMaxSize()) {
        val bucharest = LatLng(44.444662, 26.009785)
        val singaporeMarkerState = rememberMarkerState(position = bucharest)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(bucharest, 12f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.map_style_dark
                )),
                mapColorScheme = ComposeMapColorScheme.DARK
        ) {
            Marker(
                state = singaporeMarkerState,
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
    }

//    Layout {
//        SearchBar(
//            value = search,
//            onValueChange = { search = it },
//            placeholder = "Ce serviciu cauti?",
//            onSearch = {}
//        )
//
//        TitleLarge(text = "Servicii Populare")
//    }
}