package com.example.scrollbooker.ui.shared.reviews.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.headlineMedium

@SuppressLint("DefaultLocale")
@Composable
fun ReviewsSummarySection(
    summary: ReviewsSummary,
    onRatingClick: (Int) -> Unit,
    selectedRatings: Set<Int>,
    modifier: Modifier = Modifier
) {
    val maxCount = summary.breakdown.maxOfOrNull { it.count } ?: 1
    val isEnabled = summary.ratingsCount > 0

    Column(
        modifier = modifier
            .clip(ShapeDefaults.Small)
            .background(Background)
            .padding(bottom = SpacingM),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = BasePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = summary.ratingsAverage.toString(),
                    style = headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "(${summary.ratingsCount})", style = bodyMedium)
            }

            Spacer(Modifier.height(SpacingS))

            RatingsStars(
                rating = summary.ratingsAverage
            )
        }

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