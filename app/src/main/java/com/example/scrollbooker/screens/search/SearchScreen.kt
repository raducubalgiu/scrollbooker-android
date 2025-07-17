package com.example.scrollbooker.screens.search
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.businessType.domain.model.BusinessType
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    businessTypes: FeatureState<List<BusinessType>>,
    onNavigateToBusinessProfile: () -> Unit
) {
    var selectedBusinessType by rememberSaveable { mutableStateOf<BusinessType?>(null) }

    Box(Modifier.fillMaxSize()) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = rememberMapViewportState {
                setCameraOptions {
                    zoom(15.0)
                    center(Point.fromLngLat(26.104, 44.437))
                    pitch(0.0)
                    bearing(0.0)
                }
            }
        )

        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BasePadding)
                    .shadow(
                        elevation = 2.dp,
                        shape = CircleShape,
                        clip = false
                    )
                    .clip(shape = CircleShape)
                    .background(Background)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box {
                        Icon(
                            modifier = Modifier
                                .padding(BasePadding)
                                .size(30.dp),
                            painter = painterResource(R.drawable.ic_search_solid),
                            contentDescription = null
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("")
                        Text("")
                    }
                    Box(
                        modifier = Modifier
                            .padding(
                                horizontal = SpacingM,
                                vertical = SpacingS
                            )
                            .border(
                                width = 1.dp,
                                color = Divider,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(SpacingM)
                                .size(25.dp),
                            painter = painterResource(R.drawable.ic_filter_outline),
                            contentDescription = null
                        )
                    }
                }
            }

            when(val businessTypesState = businessTypes) {
                is FeatureState.Success -> {
                    LazyRow {
                        item { Spacer(Modifier.width(BasePadding)) }

                        itemsIndexed(businessTypesState.data) { index, businessType ->
                            val isSelected = selectedBusinessType?.id == businessType.id

                            Box(
                                modifier = Modifier
                                    .padding(top = SpacingM)
                                    .shadow(
                                        elevation = 2.dp,
                                        shape = CircleShape,
                                        clip = false
                                    )
                                    .clip(shape = ShapeDefaults.ExtraLarge)
                                    .background(if(isSelected) Primary else Background)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            selectedBusinessType = businessType
                                        }
                                    )
                            ) {
                                Box(
                                    modifier = Modifier.padding(
                                        vertical = SpacingM,
                                        horizontal = BasePadding
                                    )
                                ) {
                                    Text(
                                        text = businessType.plural,
                                        style = titleMedium,
                                        fontWeight = if(isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if(isSelected) OnPrimary else OnBackground
                                    )
                                }
                            }

                            if(index < businessTypesState.data.size) {
                                Spacer(Modifier.width(SpacingS))
                            }
                        }

                        item { Spacer(Modifier.width(BasePadding)) }
                    }
                }
                else -> Unit
            }
        }

//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//                .height(400.dp)
//                .background(Background)
//        ) {
//            Text("Bottom Sheet")
//        }
    }
}