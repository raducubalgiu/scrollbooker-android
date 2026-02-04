package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun SearchBusinessDomainList(
    businessDomains: FeatureState<List<BusinessDomain>>,
    selectedBusinessDomain: Int?,
    onClick: (Int?) -> Unit,
    shadowElevation: Dp = 6.dp,
    inactiveContainerColor: Color = Background,
    inactiveContentColor: Color = OnBackground,
    paddingValues: PaddingValues = PaddingValues(
        horizontal = 22.dp,
        vertical = 10.dp
    ),
    shape: Shape = ShapeDefaults.ExtraLarge
) {
    when(val bDomains = businessDomains) {
        is FeatureState.Error -> Unit
        is FeatureState.Loading -> Unit
        is FeatureState.Success -> {
            LazyRow(
                modifier = Modifier.padding(bottom = SpacingS),
                contentPadding = PaddingValues(horizontal = BasePadding)
            ) {
                item {
                    SearchBusinessDomainLabel(
                        onClick = { onClick(null) },
                        isSelected = selectedBusinessDomain == null,
                        name = stringResource(R.string.all),
                        shadowElevation = shadowElevation,
                        inactiveContainerColor = inactiveContainerColor,
                        inactiveContentColor = inactiveContentColor,
                        paddingValues = paddingValues,
                        shape = shape
                    )
                }
                items(bDomains.data) { bd ->
                    SearchBusinessDomainLabel(
                        onClick = { onClick(bd.id) },
                        isSelected = selectedBusinessDomain == bd.id,
                        name = bd.shortName,
                        shadowElevation = shadowElevation,
                        inactiveContainerColor = inactiveContainerColor,
                        inactiveContentColor = inactiveContentColor,
                        paddingValues = paddingValues,
                        shape = shape
                    )
                }
            }
        }
    }
}