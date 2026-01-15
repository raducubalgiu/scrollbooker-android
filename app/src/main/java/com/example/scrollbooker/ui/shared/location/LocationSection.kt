package com.example.scrollbooker.ui.shared.location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.shared.posts.sheets.location.LocationSheetShimmer
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import timber.log.Timber

@Composable
fun LocationSection(
    modifier: Modifier = Modifier,
    showAddress: Boolean = true
) {
    val viewModel: LocationViewModel = hiltViewModel()
    val businessLocation by viewModel.businessLocation.collectAsState()

    Column(modifier) {
        Spacer(Modifier.height(BasePadding))

        when(val business = businessLocation) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> LocationSheetShimmer()
            is FeatureState.Success -> {
                val location = business.data

                Column {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(
//                            painter = painterResource(R.drawable.ic_location_outline),
//                            contentDescription = null,
//                            tint = Primary
//                        )
//
//                        Spacer(Modifier.width(SpacingS))
//
//                        Text(
//                            text = location.distance?.let { stringResource(R.string.distanceText, 10) }
//                                ?: stringResource(R.string.activateLocationForSeeTheDistance),
//                            style = titleMedium,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    }
//
//                    Spacer(Modifier.height(SpacingM))

                    if(showAddress) {
                        Text(
                            text = "${stringResource(R.string.address)}: ${location.address}",
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(SpacingM))
                    }

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(shape = ShapeDefaults.ExtraLarge)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(location.mapUrl)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Business Location Map",
                            contentScale = ContentScale.Crop,
                            onError = { Timber.tag("Business Map Error").e("ERROR: ${it.result.throwable.message}") },
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.4f)
                                    )
                                )
                            )
                        )
                    }

                    Spacer(Modifier.height(SpacingM))

                    MainButton(
                        title = stringResource(R.string.navigationDirections),
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceBG,
                            contentColor = OnSurfaceBG
                        )
                    )

                    Spacer(Modifier.height(BasePadding))
                }
            }
        }
    }
}