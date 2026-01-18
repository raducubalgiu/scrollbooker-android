package com.example.scrollbooker.ui.search.components.card
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessOwner
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun SearchCard(
    modifier: Modifier = Modifier,
    business: BusinessSheet,
    showMoreProductsBtn: Boolean = true,
    onNavigateToBusinessProfile: (Int) -> Unit,
    onOpenBookingsSheet: (BusinessOwner) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .padding(horizontal = BasePadding)
            .clickable(
                onClick = {
                    onNavigateToBusinessProfile(business.id)
                },
                interactionSource = interactionSource,
                indication = null
            )
    ) {
        SearchCardCarousel(
            mediaFiles = business.mediaFiles
        )

        Spacer(Modifier.height(12.dp))

        SearchCardBusinessInfo(
            fullName = business.owner.fullName,
            ratingsAverage = business.owner.ratingsAverage,
            ratingsCount = business.owner.ratingsCount,
            profession = business.owner.profession,
            address = business.address,
            distance = "4.5km"
        )

        Spacer(Modifier.height(BasePadding))

        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            business.products.forEachIndexed { index, prod ->
                SearchCardProductRow(product = prod)

                if(index < business.products.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = SpacingS),
                        color = Divider,
                        thickness = 0.55.dp
                    )
                }
            }

            if(showMoreProductsBtn) {
                MainButtonOutlined(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = BasePadding),
                    title = stringResource(R.string.seeAllServices),
                    onClick = { onOpenBookingsSheet(business.owner) }
                )
            }
        }
    }
}