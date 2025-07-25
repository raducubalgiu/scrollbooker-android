package com.example.scrollbooker.ui.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.formatDistance
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun FeedSearchRecommendedBusiness(recommendedBusiness: RecommendedBusiness) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = SpacingM,
                horizontal = BasePadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Avatar(
                url = recommendedBusiness.user.avatar ?: "",
                size = 50.dp
            )
            Box(modifier = Modifier
                .size(15.dp)
                .offset(3.dp)
                .clip(CircleShape)
                .background(Color.Green)
                .border(3.dp, Color.White, CircleShape)
            )
        }
        Spacer(Modifier.width(SpacingM))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = recommendedBusiness.user.fullName,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(SpacingS))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star_solid),
                        contentDescription = null,
                        tint = Primary
                    )
                    Spacer(Modifier.width(SpacingXS))
                    Text(
                        text = "${recommendedBusiness.user.ratingsAverage}",
                        fontWeight = FontWeight.SemiBold,
                        style = bodyLarge
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = recommendedBusiness.user.profession ?: "",
                    color = Color.Gray,
                    style = bodyMedium
                )
                Spacer(Modifier.width(SpacingS))
                Box(Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                )
                Spacer(Modifier.width(SpacingS))
                Text(
                    text = formatDistance(recommendedBusiness.distance),
                    color = Color.Gray,
                    style = bodyMedium
                )
            }
        }
    }
}