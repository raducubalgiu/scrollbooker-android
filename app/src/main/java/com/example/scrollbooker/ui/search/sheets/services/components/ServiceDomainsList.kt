package com.example.scrollbooker.ui.search.sheets.services.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineSmall

@Composable
fun ServiceDomainsList(
    serviceDomains: List<ServiceDomain>?,
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

        LazyVerticalGrid(
            contentPadding = PaddingValues(BasePadding),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(BasePadding),
            horizontalArrangement = Arrangement.spacedBy(BasePadding),
        ) {
            items(serviceDomains.orEmpty()) {
                ServiceDomainCard(
                    name = it.name,
                    thumbnailUrl = it.thumbnailUrl,
                    onClick = { onSetServiceDomainId(it.id) }
                )
            }
        }
    }
}