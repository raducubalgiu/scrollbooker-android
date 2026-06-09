package com.example.scrollbooker.ui.search.businessProfile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.formatRating
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.business.domain.model.NearbyBusiness
import com.example.scrollbooker.ui.theme.Rating
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun NearbyBusinessItem(
    business: NearbyBusiness,
    onNavigateToBusinessProfile: (username: String) -> Unit
) {
    val owner = business.owner
    val location = business.location
    val imageUrl = business.mediaFiles.firstOrNull()?.thumbnailUrl

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SpacingS)
            .clickable(
                onClick = { onNavigateToBusinessProfile(business.owner.username) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        NearbyBusinessImage(
            url = imageUrl ?: "placeholder.jpg",
            username = owner.username
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = owner.fullName,
                style = titleMedium.copy(fontWeight = FontWeight.SemiBold),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_star_solid),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Rating,
                )
                Text(
                    text = owner.counters.ratingsAverage.formatRating(),
                    style = bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "(${owner.counters.ratingsCount})",
                    style = bodyMedium,
                    color = Color.Gray
                )
            }
        }

        Text(
            text = owner.profession,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = location.formattedAddress,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
