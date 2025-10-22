package com.example.scrollbooker.ui.shared.posts.sheets.location

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.Primary
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle

@Composable
fun LocationSheet(
    onClose: () -> Unit,
) {
    val viewModel: LocationViewModel = hiltViewModel()
    val mapStyle by viewModel.mapStyle.collectAsState()
    val businessState by viewModel.businessState.collectAsState()

    val isSystemInDarkMode = isSystemInDarkTheme()

    LaunchedEffect(isSystemInDarkMode) {
        if(isSystemInDarkMode) Style.DARK
        else Style.STANDARD
    }

    SheetHeader(
        title = stringResource(R.string.location),
        onClose = onClose
    )

    Spacer(Modifier.height(BasePadding))

    when(val business = businessState) {
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Loading -> LocationSheetShimmer()
        is FeatureState.Success -> {
            val latitude = business.data.coordinates.lat.toDouble()
            val longitude = business.data.coordinates.lng.toDouble()

            val mapViewportState = rememberMapViewportState {
                setCameraOptions {
                    center(Point.fromLngLat(longitude, latitude))
                    zoom(15.0)
                    pitch(60.0)
                }
            }

            Row(
                modifier = Modifier.padding(horizontal = SpacingXL),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_location_outline),
                    contentDescription = null,
                    tint = Primary
                )
                Spacer(Modifier.width(SpacingS))

                Text(
                    text = "la 5 km de tine",
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(SpacingM))

            Text(
                modifier = Modifier.padding(horizontal = SpacingXL),
                text = "Adresa: ${business.data.address}",
                color = Color.Gray
            )

            MapboxMap(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .padding(SpacingXL)
                    .clip(shape = ShapeDefaults.ExtraLarge)
                    .background(SurfaceBG),
                mapViewportState = mapViewportState,
                style = { MapStyle(style = mapStyle) },
                scaleBar = {},
            ) {
                ViewAnnotation(
                    options = viewAnnotationOptions {
                        geometry(Point.fromLngLat(longitude, latitude))
                        allowOverlap(true)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(R.drawable.ic_location_solid),
                        contentDescription = null,
                        tint = Primary
                    )
                }
            }
        }
    }
}