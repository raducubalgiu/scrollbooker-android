package com.example.scrollbooker.ui.search.businessProfile.sections.summary
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileOwner
import com.example.scrollbooker.entity.user.userProfile.domain.model.OpeningHours

@Composable
fun BusinessSummarySection(
    owner: BusinessProfileOwner,
    distance: Float?,
    address: String,
    formattedAddress: String,
    openingHours: OpeningHours,
    isFollow: Boolean?,
    isFollowEnabled: Boolean,
    onFollow: () -> Unit,
    onNavigateToOwnerProfile: (String) -> Unit,
    onFlyToReviewsSection: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = BasePadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SpacingXL),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AvatarWithRating(
                url = owner.avatar ?: "",
                rating = owner.counters.ratingsAverage,
                elevation = 2.dp,
                onClick = { onNavigateToOwnerProfile(owner.username) }
            )

            Spacer(Modifier.width(BasePadding))

            BusinessSummaryActions(
                counters = owner.counters,
                isFollow = isFollow,
                isFollowEnabled = isFollowEnabled,
                onFollow = onFollow,
                onFlyToReviewsSection = onFlyToReviewsSection
            )
        }

        BusinessSummaryDetails(
            distance = distance,
            fullName = owner.fullName,
            address = formattedAddress,
            openingHours = openingHours
        )
    }
}