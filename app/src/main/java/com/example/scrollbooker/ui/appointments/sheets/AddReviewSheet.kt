package com.example.scrollbooker.ui.appointments.sheets
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentUser
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewSheet(
    isSaving: Boolean,
    reviewUpdate: RatingReviewUpdate,
    user: AppointmentUser,
    onClose: () -> Unit,
    onSave: (RatingReviewUpdate) -> Unit
) {
    var reviewState by rememberSaveable(reviewUpdate) {
        mutableStateOf(
            RatingReviewUpdate(
                rating = reviewUpdate.rating,
                review = reviewUpdate.review
            )
        )
    }

    val ratingLabel = reviewState.rating.let { ReviewLabel.fromValue(it) }.labelRes
    val ratingLabelRes = stringResource(ratingLabel)

    Column(Modifier.fillMaxSize()) {
        AddReviewSheetHeader(
            user = user,
            onClose = onClose
        )

        Column(
            modifier = Modifier
                .padding(horizontal = SpacingXL)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            AddReviewRatingSection(
                selectedRating = reviewState.rating,
                onRatingClick = { clickedRating ->
                    reviewState = reviewState.copy(rating = clickedRating)
                },
                ratingLabel = ratingLabelRes
            )

            Spacer(Modifier.height(SpacingXL))

            AddReviewCaptureInput(
                selectedWrittenReview = reviewState,
                onValueChange = { typedText ->
                    reviewState = reviewState.copy(review = typedText)
                },
                isSaving = isSaving,
                onCreateReview = { onSave(reviewState) }
            )
        }
    }
}


