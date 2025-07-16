package com.example.scrollbooker.screens.search
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.example.scrollbooker.BuildConfig

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToBusinessProfile: () -> Unit
) {

    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(2.0)
                center(Point.fromLngLat(44.437,26.104))
                pitch(0.0)
                bearing(0.0)
            }
        }
    )

//    Layout(modifier = Modifier.statusBarsPadding()) {
//        MainButton(
//            title = "Business PRofile",
//            onClick = onNavigateToBusinessProfile
//        )
//    }
}