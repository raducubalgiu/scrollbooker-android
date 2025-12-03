package com.example.scrollbooker.ui.search.sheets.services.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.getIcon

@Composable
fun SearchBusinessDomainsSheetList(
    businessDomains: FeatureState<List<BusinessDomain>>,
    selectedBusinessDomainId: Int?,
    onClick: (Int) -> Unit
) {
    when(val bd = businessDomains) {
        is FeatureState.Error -> Text(text = stringResource(R.string.somethingWentWrong))
        is FeatureState.Loading -> Unit
        is FeatureState.Success -> {
            LazyRow(contentPadding = PaddingValues(horizontal = BasePadding)) {
                itemsIndexed(bd.data) { index, domain ->
                    SearchBusinessDomainCard(
                        name = domain.shortName,
                        icon = domain.getIcon(),
                        isSelected = selectedBusinessDomainId == domain.id,
                        onClick = { onClick(domain.id) }
                    )

                    if(index <= bd.data.size) {
                        Spacer(Modifier.width(BasePadding))
                    }
                }
            }
        }
    }
}