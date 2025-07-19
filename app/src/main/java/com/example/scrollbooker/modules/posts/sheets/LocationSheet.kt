package com.example.scrollbooker.modules.posts.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.modules.posts.PostsPagerViewModel
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import androidx.compose.runtime.getValue
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState

@Composable
fun LocationSheet(
    pagerViewModel: PostsPagerViewModel,
    businessId: Int?,
    onClose: () -> Unit
) {
    val businessState by pagerViewModel.businessState.collectAsState()

    LaunchedEffect(businessId) {
        if(businessId != null) {
            pagerViewModel.setBusinessId(businessId)
        }
    }

    val isSystemInDarkMode = isSystemInDarkTheme()

    SheetHeader(
        title = stringResource(R.string.location),
        onClose = onClose
    )

    when(val business = businessState) {
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Loading -> LoadingScreen()
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

            Column(Modifier.fillMaxSize()) {
                Spacer(Modifier.height(BasePadding))

                Row(
                    modifier = Modifier.padding(horizontal = SpacingXL),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_location_outline),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        text = "la 5 km de tine",
                        color = Color.Gray
                    )
                }

                Spacer(Modifier.height(SpacingM))

                Text(
                    modifier = Modifier.padding(horizontal = SpacingXL),
                    text = "Adresa: ${business.data.address}",
                    color = Color.Gray
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    MapboxMap(
                        modifier = Modifier
                            .weight(1f)
                            .padding(SpacingXL)
                            .clip(shape = ShapeDefaults.ExtraLarge)
                            .background(SurfaceBG),
                        mapViewportState = mapViewportState,
                        style = { MapStyle(style = if(isSystemInDarkMode) Style.DARK else Style.STANDARD) },
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

                    MainButton(
                        modifier = Modifier.padding(horizontal = SpacingXL),
                        onClick = onClose,
                        title = stringResource(R.string.close)
                    )
                }
            }
        }
    }
}