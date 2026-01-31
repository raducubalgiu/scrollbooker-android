package com.example.scrollbooker.ui.appointments
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.sheet.Sheet
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
import com.example.scrollbooker.ui.shared.location.LocationSection
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.BookingsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.BookingsSheetUser
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailsScreen(
    viewModel: AppointmentsViewModel,
    onBack: () -> Unit,
    onNavigateToCancel: () -> Unit,
    onNavigateToCamera: () -> Unit
) {
    val appointment by viewModel.selectedAppointment.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val appointmentId = appointment?.id

    val isFinished = appointment?.status == AppointmentStatusEnum.FINISHED

    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val reviewSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cancelReviewSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if(sheetState.isVisible) {
        Sheet(
            modifier = Modifier.statusBarsPadding(),
            sheetState = sheetState,
            onClose = { scope.launch { sheetState.hide() } }
        ) {
            appointment.let { app ->
                val user = app?.user ?: return@Sheet
                val userId = user.id ?: return@Sheet
                val username = user.username ?: return@Sheet
                val profession = user.profession ?: return@Sheet
                val ratingsCount = user.ratingsCount ?: return@Sheet
                val ratingsAverage = user.ratingsAverage ?: return@Sheet

                BookingsSheet(
                    user = BookingsSheetUser(
                        id = userId,
                        username = username,
                        fullName = user.fullName,
                        avatar = user.avatar,
                        profession = profession,
                        ratingsCount = ratingsCount,
                        ratingsAverage = ratingsAverage
                    ),
                    initialIndex = 0,
                    appointmentId = app.id,
                    onClose = { scope.launch { sheetState.hide() } }
                )
            }
        }
    }

    if(reviewSheetState.isVisible) {
        appointment?.user?.let {
            AddReviewSheet(
                viewModel = viewModel,
                sheetState = reviewSheetState,
                user = it,
                onClose = { scope.launch { reviewSheetState.hide()} },
                isSaving = isSaving,
                onCreateReview = { update ->
                    val reviewId = appointment?.writtenReview?.id

                    if(reviewId != null) {
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
    }

    if(cancelReviewSheetState.isVisible) {
        CancelReviewSheet(
            viewModel = viewModel,
            sheetState = cancelReviewSheetState,
            isLoadingDelete = isSaving,
            onClose = { scope.launch { cancelReviewSheetState.hide()} },
            onEdit = {
                val rating = appointment?.writtenReview?.rating
                val review = appointment?.writtenReview?.review

                scope.launch {
                    cancelReviewSheetState.hide()

                    if(!cancelReviewSheetState.isVisible && rating != null) {
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
                if(appointmentId == null) return@CancelReviewSheet

                appointment?.writtenReview?.id?.let {
                    viewModel.deleteReview(appointmentId, it)
                }
            }
        )
    }

    Scaffold(
        topBar = { Header(onBack = onBack) },
        containerColor = Background
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding())
            .padding(horizontal = SpacingXL)
            .verticalScroll(rememberScrollState())
        ) {
            appointment?.let { a ->
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

                    if(index < a.products.size - 1) {
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
                    onNavigateToCancel = onNavigateToCancel,
                    onShowBookingsSheet = { scope.launch { sheetState.show() } }
                )

                if(!a.hasWrittenReview && isFinished && a.isCustomer) {
                    ReviewCTA(
                        modifier = Modifier.padding(bottom = SpacingXL),
                        onRatingClick = {
                            viewModel.setSelectedWrittenReview(
                                RatingReviewUpdate(
                                    rating = it,
                                    review = ""
                                )
                            )

                            scope.launch {
                                reviewSheetState.show()
                            }
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

                if(!a.hasVideoReview && isFinished && a.isCustomer) {
                    VideoReviewCTA(onNavigateToCamera = onNavigateToCamera)
                }

                a.message?.let { AppointmentDetailsMessage(it) }
            }

            if(appointment?.isCustomer == true) {
                Text(
                    modifier = Modifier.padding(top = SpacingXL),
                    text = stringResource(R.string.location),
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                LocationSection()
            }

            Spacer(Modifier.height(BasePadding))
        }
    }
}