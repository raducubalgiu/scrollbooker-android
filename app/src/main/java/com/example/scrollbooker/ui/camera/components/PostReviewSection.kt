package com.example.scrollbooker.ui.camera.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewLabel
import com.example.scrollbooker.ui.appointments.sheets.AddReviewRatingSection
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun PostReviewSection(
    rating: Int,
    review: String,
    onRatingChange: (Int) -> Unit,
    onReviewChange: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.yourReview),
            style = titleLarge,
            fontSize = 23.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = stringResource(R.string.clickOnRatingToEvaluate),
            color = Color.Gray,
            style = bodyMedium
        )

        Spacer(Modifier.height(SpacingXL))

        AddReviewRatingSection(
            selectedRating = if (rating > 0) rating else null,
            onRatingClick = onRatingChange,
            ratingLabel = stringResource(ReviewLabel.fromValue(rating).labelRes)
        )

        Spacer(Modifier.height(SpacingXL))

        EditInput(
            value = review,
            onValueChange = onReviewChange,
            placeholder = stringResource(R.string.shareSomeDetailsAboutYourExperience),
            singleLine = false,
            minLines = 4,
            maxLines = 4,
            maxLength = 200
        )

        Spacer(Modifier.height(SpacingM))
    }
}
