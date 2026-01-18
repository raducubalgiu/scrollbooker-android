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
import com.example.scrollbooker.core.enums.BusinessPlanEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileOwner
import com.example.scrollbooker.entity.social.post.domain.model.BusinessPlan
import com.example.scrollbooker.entity.user.userProfile.domain.model.OpeningHours

@Composable
fun BusinessSummarySection(
    owner: BusinessProfileOwner,
    distance: Float?,
    businessPlan: BusinessPlan,
    address: String,
    openingHours: OpeningHours,
    isFollow: Boolean?,
    isFollowEnabled: Boolean,
    onFollow: () -> Unit,
    onNavigateToOwnerProfile: (Int) -> Unit,
    onFlyToReviewsSection: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasPhone = businessPlan.name == BusinessPlanEnum.STANDARD

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
                onClick = { onNavigateToOwnerProfile(owner.id) }
            )

            Spacer(Modifier.width(BasePadding))

            BusinessSummaryActions(
                counters = owner.counters,
                hasPhone = hasPhone,
                isFollow = isFollow,
                isFollowEnabled = isFollowEnabled,
                onFollow = onFollow,
                onFlyToReviewsSection = onFlyToReviewsSection
            )
        }

        BusinessSummaryDetails(
            distance = distance,
            fullName = owner.fullName,
            address = address,
            openingHours = openingHours
        )
    }
}