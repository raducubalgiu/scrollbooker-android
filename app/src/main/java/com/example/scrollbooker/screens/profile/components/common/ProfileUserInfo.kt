package com.example.scrollbooker.screens.profile.components.common
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
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
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

    if(isBusinessOrEmployee) {
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
            Avatar(
                url = user.businessOwner?.avatar ?: "",
                size = 25.dp
            )
            Spacer(Modifier.width(SpacingS))
            Text(
                text = user.businessOwner?.fullName ?: "",
                color = OnBackground,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(Modifier.height(SpacingM))

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SpacingXXL)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_map_outline),
                        contentDescription = null,
                        tint = Primary
                    )
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        text = stringResource(R.string.address),
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(10.dp)
                        .background(Color.Gray, shape = RectangleShape)
                        .padding(vertical = BasePadding)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_email_outline),
                        contentDescription = null,
                        tint = Primary
                    )
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        text = stringResource(R.string.email),
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(10.dp)
                        .background(Color.Gray, shape = RectangleShape)
                        .padding(vertical = BasePadding)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_globe_outline),
                        contentDescription = null,
                        tint = Primary
                    )
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        text = stringResource(R.string.website),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(SpacingM))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_location_outline),
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(Modifier.width(4.dp))
                Text(text =
                    buildAnnotatedString {
                        append("${stringResource(R.string.at)} ")
                        withStyle(SpanStyle(
                            fontWeight = FontWeight.Bold
                        )) {
                            append("${user.distanceKm}km")
                        }
                        append(" ${stringResource(R.string.fromYou)}")
                    }
                )
            }
        }
    }

    if(user.bio?.isNotEmpty() == true) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = SpacingM,
                start = 50.dp,
                end = 50.dp,
                bottom = 0.dp
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = user.bio,
                textAlign = TextAlign.Center
            )
        }
    }

    Spacer(Modifier.height(BasePadding))
}