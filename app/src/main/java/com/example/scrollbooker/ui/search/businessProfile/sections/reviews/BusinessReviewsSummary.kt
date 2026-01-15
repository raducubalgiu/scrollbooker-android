package com.example.scrollbooker.ui.search.businessProfile.sections.reviews

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BusinessReviewsSummary(
    ratingsAverage: Float,
    ratingsCount: Int
) {
    RatingsStars(
        modifier = Modifier.padding(start = BasePadding),
        rating = ratingsAverage,
        maxRating = 5,
    )

    Spacer(Modifier.height(SpacingS))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ratingsAverage.toString(),
            style = titleMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.width(SpacingM))

        Text(
            text = "(${ratingsCount})",
            style = titleMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}