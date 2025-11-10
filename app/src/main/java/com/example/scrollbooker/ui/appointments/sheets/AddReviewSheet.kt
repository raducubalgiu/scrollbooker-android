package com.example.scrollbooker.ui.appointments.sheets
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentUser
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewLabel
import com.example.scrollbooker.ui.appointments.AppointmentsViewModel
import com.example.scrollbooker.ui.appointments.RatingReviewUpdate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewSheet(
    viewModel: AppointmentsViewModel,
    sheetState: SheetState,
    user: AppointmentUser,
    isSaving: Boolean,
    onClose: () -> Unit,
    onCreateReview: (RatingReviewUpdate) -> Unit
) {
    val selectedWrittenReview by viewModel.selectedWrittenReview.collectAsState()

    val ratingLabel = selectedWrittenReview?.rating?.let { ReviewLabel.fromValue(it) }?.labelRes
    val ratingLabelRes = ratingLabel?.let { stringResource(it) }
    val createState by viewModel.createReviewState.collectAsState()

    LaunchedEffect(createState) {
        when(createState) {
            is FeatureState.Success -> {
                viewModel.consumeCreateReviewState()
                onClose()
            }
            is FeatureState.Error -> {
                viewModel.consumeCreateReviewState()
            }
            else -> Unit
        }
    }

    Sheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onClose = onClose
    ) {
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
                    selectedRating = selectedWrittenReview?.rating,
                    onRatingClick = {
                        selectedWrittenReview?.let { selected ->
                            viewModel.setSelectedWrittenReview(
                                RatingReviewUpdate(
                                    rating = it,
                                    review = selected.review
                                )
                            )
                        }
                    },
                    ratingLabel = ratingLabelRes ?: ""
                )

                Spacer(Modifier.height(SpacingXL))

                AddReviewCaptureInput(
                    selectedWrittenReview = selectedWrittenReview,
                    onValueChange = {
                        selectedWrittenReview?.let { selected ->
                            viewModel.setSelectedWrittenReview(
                                RatingReviewUpdate(
                                    rating = selected.rating,
                                    review = it
                                )
                            )
                        }
                    },
                    isSaving = isSaving,
                    onCreateReview = {
                        selectedWrittenReview?.let { selected ->
                            onCreateReview(
                                RatingReviewUpdate(
                                    rating = selected.rating,
                                    review = selected.review
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}

