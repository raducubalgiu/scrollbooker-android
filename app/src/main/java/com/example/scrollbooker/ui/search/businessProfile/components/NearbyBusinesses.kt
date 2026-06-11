package com.example.scrollbooker.ui.search.businessProfile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.business.domain.model.NearbyBusiness
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun NearbyBusinesses(
    businesses: List<NearbyBusiness>?,
    onNavigateToBusinessProfile: (username: String) -> Unit
) {
    if (businesses.isNullOrEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        HorizontalDivider(color = Divider, thickness = 1.dp)
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.nearServices),
            style = titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = BasePadding),
            horizontalArrangement = Arrangement.spacedBy(BasePadding)
        ) {
            items(businesses) { business ->
                NearbyBusinessItem(
                    business = business,
                    onNavigateToBusinessProfile = onNavigateToBusinessProfile,
                )
            }
        }
    }
}


