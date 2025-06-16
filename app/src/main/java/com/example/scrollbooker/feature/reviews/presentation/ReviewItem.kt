package com.example.scrollbooker.feature.reviews.presentation

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.feature.reviews.domain.model.Review
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun ReviewItem(
    review: Review
) {
    Column(Modifier.padding(BasePadding)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar(url = review.customer.avatar ?: "")
            Spacer(Modifier.width(BasePadding))
            Column {
                Text(
                    text = review.customer.fullName,
                    style = bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "@${review.customer.username}",
                    style = bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "${
                        OffsetDateTime.parse(review.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                            .format(DateTimeFormatter.ofPattern("dd MM yyyy, HH:mm"))
                    }"
                )
            }
        }

        Spacer(Modifier.height(BasePadding))

        Text(
            text = "Foarte multumit",
            style = titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(SpacingXXS))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Primary
            )
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Primary
            )
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Primary
            )
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Primary
            )
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Primary
            )
        }

        Spacer(Modifier.height(BasePadding))

        Text(
            text = review.review
        )

        Spacer(Modifier.height(BasePadding))

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Adauga un comentariu")
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = null,
                tint = Error
            )
        }
    }
}