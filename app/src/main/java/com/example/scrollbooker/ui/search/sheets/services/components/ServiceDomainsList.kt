package com.example.scrollbooker.ui.search.sheets.services.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.headlineSmall
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ServiceDomainsList(
    serviceDomains: FeatureState<List<ServiceDomain>>?,
    onSetServiceDomainId: (Int) -> Unit
) {
    Column {
        Spacer(Modifier.height(SpacingXL))

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            style = headlineSmall,
            color = OnBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.categories)
        )

        Spacer(Modifier.height(BasePadding))

        when(val domains = serviceDomains) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> {
                LazyVerticalGrid(
                    contentPadding = PaddingValues(BasePadding),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(BasePadding),
                    horizontalArrangement = Arrangement.spacedBy(BasePadding),
                ) {
                    items(6) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(shape = ShapeDefaults.Medium)
                                .background(rememberShimmerBrush())
                        )
                    }
                }
            }
            is FeatureState.Success -> {
                LazyVerticalGrid(
                    contentPadding = PaddingValues(BasePadding),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(BasePadding),
                    horizontalArrangement = Arrangement.spacedBy(BasePadding),
                ) {
                    items(domains.data) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clip(shape = ShapeDefaults.Medium)
                                    .clickable {
                                        onSetServiceDomainId(it.id)
                                    }
                            ) {
                                AsyncImage(
                                    modifier = Modifier.matchParentSize(),
                                    model = "",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                )

                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .background(SurfaceBG)
                                )
                            }

                            Spacer(Modifier.height(SpacingS))

                            Text(
                                text = it.name,
                                style = titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            null -> null
        }
    }
}