package com.example.scrollbooker.components.customized.post.sheets.linkedProducts

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.customized.DisplayAddressWithDistance
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.extensions.formatRating
import com.example.scrollbooker.core.util.Dimens.AvatarSizeM
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.products.domain.model.LinkedProductsBusiness
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun LinkedProductsBusinessHeader(business: LinkedProductsBusiness) {
    Row(
        modifier = Modifier.padding(BasePadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(url = business.avatar ?: "", size = AvatarSizeM)

        Spacer(Modifier.width(SpacingM))

        Column {
            Text(
                text = business.fullname,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(SpacingXS))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = business.ratingsAverage.formatRating(),
                    fontWeight = FontWeight.Bold,
                    style = bodyMedium
                )

                Spacer(Modifier.width(4.dp))

                RatingsStars(rating = business.ratingsAverage, starSize = 14.dp)

                Spacer(Modifier.width(4.dp))

                Text(
                    text = "(${business.ratingsCount})",
                    color = Color.Gray,
                    style = bodyMedium
                )
            }

            Spacer(Modifier.height(SpacingS))

            DisplayAddressWithDistance(
                distanceKm = business.distanceKm,
                address = business.address ?: ""
            )
        }
    }
}
