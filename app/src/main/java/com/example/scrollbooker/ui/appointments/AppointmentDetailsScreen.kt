package com.example.scrollbooker.ui.appointments
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentWrittenReview
import com.example.scrollbooker.ui.appointments.components.AppointmentDetailsActions
import com.example.scrollbooker.ui.appointments.components.AppointmentDetailsHeader
import com.example.scrollbooker.ui.appointments.components.AppointmentDetailsMessage
import com.example.scrollbooker.ui.appointments.components.AppointmentProductPrice
import com.example.scrollbooker.ui.appointments.components.ReviewCTA
import com.example.scrollbooker.ui.appointments.components.VideoReviewCTA
import com.example.scrollbooker.ui.appointments.sheets.AddReviewSheet
import com.example.scrollbooker.ui.appointments.sheets.CancelReviewSheet
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch
import com.example.scrollbooker.components.customized.SectionMap
import com.example.scrollbooker.core.util.FeatureState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailsScreen(
    viewModel: AppointmentDetailsViewModel,
    onBack: () -> Unit,
    onNavigateToCamera: () -> Unit
) {
    val appointmentState by viewModel.appointment.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val reviewSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cancelReviewSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val appointment = (appointmentState as? FeatureState.Success)?.data

    if (reviewSheetState.isVisible && appointment != null) {
        AddReviewSheet(
            viewModel = viewModel,
            sheetState = reviewSheetState,
            user = appointment.user,
            onClose = { scope.launch { reviewSheetState.hide() } },
            isSaving = isSaving,
            onCreateReview = { update ->
                val reviewId = appointment.writtenReview?.id
                if (reviewId != null) {
                    viewModel.editReview(
                        AppointmentWrittenReview(
                            id = reviewId,
                            rating = update.rating,
                            review = update.review
                        )
                    )
                } else {
                    viewModel.createReview(appointment)
                }
            }
        )
    }

    if (cancelReviewSheetState.isVisible && appointment != null) {
        CancelReviewSheet(
            viewModel = viewModel,
            sheetState = cancelReviewSheetState,
            isLoadingDelete = isSaving,
            onClose = { scope.launch { cancelReviewSheetState.hide() } },
            onEdit = {
                val rating = appointment.writtenReview?.rating
                val review = appointment.writtenReview?.review

                scope.launch {
                    cancelReviewSheetState.hide()
                    if (!cancelReviewSheetState.isVisible && rating != null) {
                        viewModel.setSelectedWrittenReview(
                            RatingReviewUpdate(
                                rating = rating,
                                review = review
                            )
                        )
                        reviewSheetState.show()
                    }
                }
            },
            onDelete = {
                appointment.writtenReview?.id?.let { reviewId ->
                    viewModel.deleteReview(appointment.id, reviewId)
                }
            }
        )
    }

    Scaffold(
        topBar = { Header(onBack = onBack) },
        containerColor = Background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            when (val state = appointmentState) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success -> {
                    val a = state.data
                    val isFinished = a.status == AppointmentStatusEnum.FINISHED

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = SpacingXL)
                            .verticalScroll(rememberScrollState())
                    ) {
                        AppointmentDetailsHeader(appointment = a)

                        Text(
                            modifier = Modifier.padding(vertical = SpacingXL),
                            text = "${stringResource(R.string.bookedServices)}:",
                            style = titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        a.products.forEachIndexed { index, prod ->
                            AppointmentProductPrice(
                                name = prod.name,
                                price = prod.price,
                                priceWithDiscount = prod.priceWithDiscount,
                                discount = prod.discount,
                                currencyName = prod.currency.name
                            )

                            if (index < a.products.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = BasePadding),
                                    color = Divider,
                                    thickness = 0.55.dp
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = BasePadding),
                            color = Divider,
                            thickness = 0.55.dp
                        )

                        AppointmentProductPrice(
                            modifier = Modifier.padding(bottom = SpacingXL),
                            name = stringResource(R.string.total),
                            price = a.totalPrice,
                            priceWithDiscount = a.totalPriceWithDiscount,
                            discount = a.totalDiscount,
                            currencyName = a.paymentCurrency.name
                        )

                        AppointmentDetailsActions(
                            modifier = Modifier.padding(bottom = SpacingXL),
                            status = a.status,
                            isCustomer = a.isCustomer,
                            onNavigateToCancel = {}
                        )

                        if (!a.hasWrittenReview && isFinished && a.isCustomer) {
                            ReviewCTA(
                                modifier = Modifier.padding(bottom = SpacingXL),
                                onRatingClick = { rating ->
                                    viewModel.setSelectedWrittenReview(
                                        RatingReviewUpdate(
                                            rating = rating,
                                            review = ""
                                        )
                                    )
                                    scope.launch { reviewSheetState.show() }
                                }
                            )
                        }

                        a.writtenReview?.let { rev ->
                            AppointmentDetailsWrittenReview(
                                customerAvatar = a.customer.avatar ?: "",
                                review = rev.review,
                                rating = rev.rating,
                                isCustomer = a.isCustomer,
                                onOpenCancelSheet = { scope.launch { cancelReviewSheetState.show() } }
                            )
                        }

                        if (!a.hasVideoReview && isFinished && a.isCustomer) {
                            VideoReviewCTA(onNavigateToCamera = onNavigateToCamera)
                        }

                        a.message?.let { AppointmentDetailsMessage(it) }

                        if (a.isCustomer == true) {
                            a.business.mapUrl?.let { mapUrl ->
                                Text(
                                    modifier = Modifier.padding(top = SpacingXL),
                                    text = stringResource(R.string.location),
                                    style = titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(Modifier.height(BasePadding))

                                SectionMap(
                                    mapUrl = mapUrl,
                                    coordinates = a.business.coordinates,
                                    fullName = a.user.fullName
                                )
                            }
                        }
                        Spacer(Modifier.height(BasePadding))
                    }
                }
            }
        }
    }
}


