package com.example.scrollbooker.screens.profile.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.shared.userProfile.domain.model.UserProfile
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ProfileUserInfo(
    user: UserProfile,
    actions: @Composable () -> Unit,
    onOpenScheduleSheet: () -> Unit,
    onNavigateToBusinessOwner: (Int?) -> Unit
) {
    val isBusinessOrEmployee = user.businessId != null
    val isOpenNow = user.openingHours.openNow
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier
        .padding(horizontal = SpacingXL)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, Divider, CircleShape)
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
                            imageVector = Icons.Filled.Star,
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
                    Spacer(Modifier.height(SpacingXS))
                    Row(modifier = Modifier
                        .clickable(
                            onClick = onOpenScheduleSheet,
                            interactionSource = interactionSource,
                            indication = null
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = OnSurfaceBG
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = formatOpeningHours(user.openingHours).toString(),
                            style = bodyMedium,
                            color = OnSurfaceBG,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = null,
                            tint = OnSurfaceBG
                        )
                    }
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
    ) {
        actions()
    }

    if(isBusinessOrEmployee && user.businessOwner?.id != user.id) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = BasePadding)
            .clickable(
                onClick = {
                    if(user.businessOwner?.id != null) {
                        onNavigateToBusinessOwner(user.businessOwner.id)
                    }
                },
                interactionSource = interactionSource,
                indication = null
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Repeat,
                contentDescription = null,
                tint = Primary
            )
            Spacer(Modifier.width(SpacingS))
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, Divider, CircleShape)
            )
            Spacer(Modifier.width(SpacingS))
            Text(
                text = user.businessOwner?.fullName ?: "",
                color = OnBackground,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(
            top = BasePadding,
            start = 50.dp,
            end = 50.dp,
            bottom = 0.dp
        ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = user.bio ?: "",
            textAlign = TextAlign.Center
        )
    }

    Spacer(Modifier.height(BasePadding))
}