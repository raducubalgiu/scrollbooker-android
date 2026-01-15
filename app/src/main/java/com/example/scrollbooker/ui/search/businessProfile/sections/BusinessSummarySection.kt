package com.example.scrollbooker.ui.search.businessProfile.sections
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.divider.VerticalDivider
import com.example.scrollbooker.core.enums.BusinessPlanEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileOwner
import com.example.scrollbooker.entity.social.post.domain.model.BusinessPlan
import com.example.scrollbooker.entity.user.userProfile.domain.model.OpeningHours
import com.example.scrollbooker.ui.profile.components.userInfo.components.CounterItem
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BusinessSummarySection(
    owner: BusinessProfileOwner,
    distance: Float?,
    businessPlan: BusinessPlan,
    address: String,
    openingHours: OpeningHours,
    modifier: Modifier = Modifier
) {
    val hasPhone = businessPlan.name == BusinessPlanEnum.STANDARD
    val isFollow = owner.isFollow

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
                onClick = {}
            )

            Spacer(Modifier.width(BasePadding))

            Column(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CounterItem(
                            counter = owner.counters.ratingsCount,
                            label = stringResource(R.string.reviews),
                            onNavigate = {}
                        )

                        VerticalDivider()

                        CounterItem(
                            counter = owner.counters.followersCount,
                            label = stringResource(R.string.followers),
                            onNavigate = {}
                        )

                        VerticalDivider()

                        CounterItem(
                            counter = owner.counters.followingsCount,
                            label = stringResource(R.string.following),
                            onNavigate = {}
                        )
                    }
                }

                Spacer(Modifier.height(SpacingXL))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if(businessPlan.name == BusinessPlanEnum.STANDARD) {
                        MainButtonOutlined(
                            modifier = Modifier.weight(0.5f),
                            title = stringResource(R.string.call),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary,
                                contentColor = OnPrimary,
                            ),
                            border = BorderStroke(1.dp, Primary),
                            shape = ShapeDefaults.ExtraLarge,
                            onClick = {}
                        )

                        Spacer(Modifier.width(SpacingS))
                    }

                    MainButtonOutlined(
                        modifier = Modifier.weight(0.5f),
                        title = stringResource(R.string.follow),
                        shape = ShapeDefaults.ExtraLarge,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                hasPhone -> Color.Transparent
                                isFollow -> Color.Transparent
                                else -> Primary
                            },
                            contentColor = when {
                                hasPhone -> OnBackground
                                isFollow -> OnBackground
                                else -> OnPrimary
                            }
                        ),
                        border = BorderStroke(1.dp, when {
                            hasPhone -> Divider
                            isFollow -> Divider
                            else -> Primary
                        }),
                        onClick = {}
                    )
                }
            }
        }

        Text(
            text = owner.fullName,
            style = titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(Modifier.height(SpacingXS))

        Row(verticalAlignment = Alignment.CenterVertically) {
            distance?.let {
                Text(
                    text = "${it}km",
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = "\u2022",
                    color = Color.Gray
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                text = address,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(SpacingM))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if(openingHours.openNow) stringResource(R.string.open)
                       else stringResource(R.string.closed),
                color = OnBackground,
                style = titleMedium,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "\u2022",
                color = Color.Gray
            )

            Text(
                text = if(openingHours.openNow) "Inchide la ${openingHours.closingTime}"
                       else "Deschide ${openingHours.nextOpenDay} la ${openingHours.nextOpenTime}",
                color = Color.Gray,
                style = bodyLarge,
            )
        }
    }
}