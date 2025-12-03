package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain

@Composable
fun SearchBusinessDomainList(
    businessDomains: FeatureState<List<BusinessDomain>>,
    selectedBusinessDomain: Int?,
    onClick: (Int?) -> Unit
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
                        name = stringResource(R.string.all)
                    )
                }
                items(bDomains.data) { bd ->
                    SearchBusinessDomainLabel(
                        onClick = { onClick(bd.id) },
                        isSelected = selectedBusinessDomain == bd.id,
                        name = bd.shortName
                    )
                }
            }
        }
    }
}