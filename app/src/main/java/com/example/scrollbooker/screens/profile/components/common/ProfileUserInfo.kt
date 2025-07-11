package com.example.scrollbooker.screens.profile.components.common
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.screens.profile.components.common.userInformation.ProfileBio
import com.example.scrollbooker.screens.profile.components.common.userInformation.ProfileBusinessEmployee
import com.example.scrollbooker.screens.profile.components.common.userInformation.ProfileIntentActionsList
import com.example.scrollbooker.screens.profile.components.common.userInformation.ProfileLocationDistance
import com.example.scrollbooker.screens.profile.components.common.userInformation.ProfileOpeningHours
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ProfileUserInfo(
    user: UserProfile,
    actions: @Composable () -> Unit,
    onOpenScheduleSheet: () -> Unit,
    onNavigateToBusinessOwner: (Int?) -> Unit
) {
    val isBusinessOrEmployee = user.isBusinessOrEmployee
    val isOpenNow = user.openingHours.openNow

    Column(modifier = Modifier
        .padding(horizontal = SpacingXL)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Avatar(
                    url = user.avatar ?: "",
                    size = 90.dp
                )
                if(isBusinessOrEmployee) {
                    Box(modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(if(isOpenNow) Color.Green else Color(0xFFCCCCCC))
                        .border(3.dp, Color.White, CircleShape)
                    )
                }
            }

            Spacer(Modifier.width(BasePadding))

            Column {
                Text(
                    text = user.fullName,
                    style = titleMedium,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(SpacingXS))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.profession,
                        style = titleMedium,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if(isBusinessOrEmployee) {
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            painter = painterResource(R.drawable.ic_star_solid),
                            contentDescription = null,
                            tint = Primary
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = user.counters.ratingsAverage.toString(),
                            style = titleMedium,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnBackground
                        )
                    }
                }
                if(isBusinessOrEmployee) {
                    ProfileOpeningHours(
                        onOpenScheduleSheet = onOpenScheduleSheet,
                        openingHours = user.openingHours
                    )
                }
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(
            top = BasePadding,
            start = BasePadding,
            end = BasePadding
        ),
    ) { actions() }

    if(isBusinessOrEmployee) {
        ProfileBusinessEmployee(
            businessOwnerAvatar = user.businessOwner?.avatar,
            businessOwnerFullName = user.businessOwner?.fullName,
            onNavigateToBusinessOwner = {
                if(user.businessOwner?.id != null) {
                    onNavigateToBusinessOwner(user.businessOwner.id)
                }
            }
        )

        Spacer(Modifier.height(SpacingM))

        ProfileIntentActionsList()

        user.distanceKm?.let {
            ProfileLocationDistance(distance = user.distanceKm)
        }
    }

    if(!user.bio.isNullOrBlank()) {
        ProfileBio(user.bio)
    }

    Spacer(Modifier.height(BasePadding))
}