package com.example.scrollbooker.ui.profile.components.userInfo
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
import androidx.compose.runtime.remember
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
import com.example.scrollbooker.core.extensions.toFixedDecimals
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXL
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.ui.profile.components.userInfo.components.INTENT_ACTION_SPECS
import com.example.scrollbooker.ui.profile.components.userInfo.components.IntentAction
import com.example.scrollbooker.ui.profile.components.userInfo.components.ProfileBio
import com.example.scrollbooker.ui.profile.components.userInfo.components.ProfileBusinessEmployee
import com.example.scrollbooker.ui.profile.components.userInfo.components.ProfileIntentActionsList
import com.example.scrollbooker.ui.profile.components.userInfo.components.ProfileLocationDistance
import com.example.scrollbooker.ui.profile.components.userInfo.components.ProfileOpeningHours
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun rememberIntentActions(user: UserProfile): List<IntentAction> {
    return remember(user) {
        INTENT_ACTION_SPECS.map { spec ->
            IntentAction(
                icon = spec.icon,
                title = spec.title,
                value = spec.valueOf(user),
                isBusinessOrEmployee = spec.onlyBusinessOrEmployee
            )
        }
    }
}

fun filterIntentActions(
    actions: List<IntentAction>,
    isBusinessOrEmployee: Boolean
): List<IntentAction> {
        return if (!isBusinessOrEmployee) {
        actions.filter { it.value.isNotEmpty() && !it.isBusinessOrEmployee }
    } else {
        actions.filter { it.value.isNotEmpty() }
    }
}

@Composable
fun ProfileUserInfo(
    user: UserProfile,
    actions: @Composable () -> Unit,
    onOpenScheduleSheet: () -> Unit,
    onNavigateToBusinessOwner: (Int?) -> Unit
) {
    val isBusinessOrEmployee = user.isBusinessOrEmployee
    val isOpenNow = user.openingHours.openNow

    val intentActions = rememberIntentActions(user)
    val filteredIntentList = remember(user, intentActions) {
        filterIntentActions(intentActions, user.isBusinessOrEmployee)
    }

    Column(modifier = Modifier
        .padding(horizontal = SpacingXL)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Avatar(
                    url = user.avatar ?: "",
                    size = AvatarSizeXL
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
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray
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
                            text = user.counters.ratingsAverage.toFixedDecimals(1),
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
        if(user.id != user.businessOwner?.id) {
            ProfileBusinessEmployee(
                businessOwnerAvatar = user.businessOwner?.avatar,
                businessOwnerFullName = user.businessOwner?.fullName,
                onNavigateToBusinessOwner = {
                    if (user.businessOwner?.id != null) {
                        onNavigateToBusinessOwner(user.businessOwner.id)
                    }
                }
            )
        }

        Spacer(Modifier.height(SpacingM))

        ProfileIntentActionsList(intentList = filteredIntentList)

        if(!user.isOwnProfile && user.distanceKm != null) {
            ProfileLocationDistance(distance = user.distanceKm)
        }
    }

    if(!user.bio.isNullOrBlank()) {
        ProfileBio(user.bio)
    }

    Spacer(Modifier.height(BasePadding))
}