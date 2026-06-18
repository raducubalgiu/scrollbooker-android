package com.example.scrollbooker.ui.booking.confirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.extensions.formatRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlowUser
import com.example.scrollbooker.ui.theme.titleMedium
import timber.log.Timber

@Composable
fun BookingSummaryOwner(
    owner: BookingFlowUser
) {
    Row(
        modifier = Modifier.padding(BasePadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(ShapeDefaults.Medium)
        ) {
            AsyncImage(
                model = owner.avatar,
                contentDescription = "User Avatar",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                onError = {
                    Timber.tag("Avatar Error").e("ERROR: ${it.result.throwable.message}")
                }
            )
        }

        Spacer(Modifier.width(SpacingM))

        Column {
            Text(
                text = owner.fullName,
                style = titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(SpacingXXS))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = owner.ratingsAverage.formatRating(),
                    fontWeight = FontWeight.Bold
                )

                RatingsStars(
                    rating = owner.ratingsAverage,
                    starSize = 20.dp
                )

                Text(
                    text = "(${owner.ratingsCount})",
                    color = Color.Gray
                )
            }
        }
    }
}