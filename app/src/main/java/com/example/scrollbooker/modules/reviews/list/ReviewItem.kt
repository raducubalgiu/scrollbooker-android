package com.example.scrollbooker.modules.reviews.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.review.data.remote.ReviewLabel
import com.example.scrollbooker.entity.review.domain.model.Review
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun ReviewItem(
    review: Review
) {
    Column(modifier = Modifier
        .padding(
            top = SpacingS,
            start = SpacingS,
            end = SpacingS
        )
        .clip(ShapeDefaults.Small)
        .background(Background)

    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(SpacingM)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(url = review.customer.avatar ?: "")
                Spacer(Modifier.width(SpacingM))
                Column {
                    Text(
                        text = review.customer.fullName,
                        style = bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        color = Color.Gray,
                        text = "${
                            OffsetDateTime.parse(review.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                                .format(DateTimeFormatter.ofPattern("dd MM yyyy, HH:mm"))
                        }"
                    )
                }
            }

            Text(
                modifier = Modifier.padding(top = BasePadding),
                text = stringResource(id = ReviewLabel.fromValue(review.rating).labelRes),
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            RatingsStars(
                starSize = 20.dp,
                modifier = Modifier.padding(
                    vertical = SpacingS
                ),
                rating = review.rating.toFloat()
            )

            Text(
                text = review.review
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = SpacingXL),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${stringResource(R.string.addComment)}...",
                    color = Color.Gray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if(review.isLiked) Error else Color.Gray
                    )
                    if(review.likeCount > 0) {
                        Text(
                            modifier = Modifier.padding(start = SpacingXS),
                            text = "${review.likeCount}",
                            style = bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Error
                        )
                    }
                }
            }
        }
    }
}