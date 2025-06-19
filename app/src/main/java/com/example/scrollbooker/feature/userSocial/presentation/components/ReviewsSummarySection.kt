package com.example.scrollbooker.feature.userSocial.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.shared.reviews.domain.model.ReviewsSummary
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(ShapeDefaults.Small)
            .background(Background)
            .padding(bottom = SpacingM),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = BasePadding
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = String.format("%.1f", summary.averageRating),
                    style = headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "(${summary.totalReviews})", style = bodyMedium)
            }

            Spacer(Modifier.height(SpacingS))

            RatingsStars(
                rating = summary.averageRating
            )
        }

        summary.breakdown.sortedByDescending { it.rating }.forEach { item ->
            val progress = if(maxCount > 0) item.count / maxCount.toFloat() else 0f

            Row(modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Checkbox(
                        checked = item.rating in selectedRatings,
                        onCheckedChange = {
                            onRatingClick(item.rating)
                        },
                        colors = CheckboxColors(
                            checkedCheckmarkColor = Color.White,
                            uncheckedCheckmarkColor = Color.Transparent,
                            checkedBoxColor = Primary,
                            uncheckedBoxColor = Color.Transparent,
                            disabledCheckedBoxColor = Divider,
                            disabledUncheckedBoxColor = Divider,
                            disabledIndeterminateBoxColor = Divider,
                            checkedBorderColor = Primary,
                            uncheckedBorderColor = Divider,
                            disabledBorderColor = Divider,
                            disabledUncheckedBorderColor = Divider,
                            disabledIndeterminateBorderColor = Divider
                        )
                    )
                }

                Text(
                    text = "${item.rating}",
                    style = bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.width(BasePadding))

                LinearProgressIndicator(
                    modifier = Modifier
                        .height(5.dp)
                        .weight(1f),
                    progress = { progress },
                    color = Primary,
                    trackColor = Color.Gray.copy(alpha = 0.4f)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    modifier = Modifier.width(30.dp),
                    text = "${item.count}",
                    textAlign = TextAlign.End,
                    style = bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.width(BasePadding))
            }
        }
    }
}