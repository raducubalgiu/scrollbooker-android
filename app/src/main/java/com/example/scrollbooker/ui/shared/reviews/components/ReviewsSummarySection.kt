package com.example.scrollbooker.ui.shared.reviews.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.extensions.formatRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.headlineMedium

@Composable
fun ReviewsSummarySection(
    summary: ReviewsSummary,
    onRatingClick: (Int) -> Unit,
    selectedRatings: Set<Int>,
    modifier: Modifier = Modifier
) {
    val maxCount = summary.breakdown.maxOfOrNull { it.count } ?: 1
    val isEnabled = summary.ratingsCount > 0

    Row(
        modifier = modifier
            .clip(ShapeDefaults.Small)
            .background(Background)
            .padding(
                start = BasePadding,
                 top = BasePadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = summary.ratingsAverage.formatRating(),
                style = headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(SpacingS))

            RatingsStars(
                starSize = 16.dp,
                rating = summary.ratingsAverage
            )
            Spacer(Modifier.height(SpacingS))

            Text(
                text = "${summary.ratingsCount} ${stringResource(R.string.reviews)}",
                style = bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(Modifier.width(SpacingXXL))

        Column(Modifier.weight(1f)) {
            summary.breakdown.sortedByDescending { it.rating }.forEach { item ->
                val progress = item.count / maxCount.toFloat()

                ReviewSummaryCheckbox(
                    rating = item.rating,
                    progress = progress,
                    count = item.count,
                    isEnabled = isEnabled,
                    isChecked = item.rating in selectedRatings,
                    onCheckChange = { onRatingClick(item.rating) }
                )
            }
        }
    }
}