package com.example.scrollbooker.ui.appointments
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXS
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.appointments.components.AppointmentDetailsActions
import com.example.scrollbooker.ui.appointments.components.AppointmentDetailsHeader
import com.example.scrollbooker.ui.appointments.components.AppointmentDetailsMessage
import com.example.scrollbooker.ui.appointments.components.AppointmentProductPrice
import com.example.scrollbooker.ui.appointments.components.ReviewCTA
import com.example.scrollbooker.ui.appointments.components.VideoReviewCTA
import com.example.scrollbooker.ui.appointments.sheets.AddReviewSheet
import com.example.scrollbooker.ui.shared.location.LocationSection
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.BookingsSheet
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailsScreen(
    viewModel: AppointmentsViewModel,
    onBack: () -> Unit,
    onNavigateToCancel: () -> Unit,
) {
    val appointment by viewModel.selectedAppointment.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    var selectedRating by remember { mutableStateOf<Int?>(null) }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val reviewSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if(sheetState.isVisible) {
        Sheet(
            modifier = Modifier.statusBarsPadding(),
            sheetState = sheetState,
            onClose = { scope.launch { sheetState.hide() } }
        ) {
            appointment?.user?.id?.let { userId ->
                BookingsSheet(
                    userId = 13,
                    initialPage = 0,
                    onClose = { scope.launch { sheetState.hide() } }
                )
            }
        }
    }

    if(reviewSheetState.isVisible) {
        AddReviewSheet(
            sheetState = reviewSheetState,
            selectedRating = selectedRating,
            onRatingClick = { selectedRating = it },
            onClose = { scope.launch { reviewSheetState.hide()} },
            isSaving = isSaving,
            onCreateReview = {
                selectedRating?.let { rating ->
                    viewModel.createReview(
                        appointment = appointment,
                        rating = rating,
                        review = it
                    )
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
                    onNavigateToCancel = onNavigateToCancel,
                    onShowBookingsSheet = { scope.launch { sheetState.show() } }
                )

                if(!a.hasWrittenReview && a.status == AppointmentStatusEnum.FINISHED) {
                    ReviewCTA(
                        modifier = Modifier.padding(bottom = SpacingXL),
                        onRatingClick = {
                            scope.launch {
                                selectedRating = it
                                reviewSheetState.show()
                            }
                        }
                    )
                }

                a.writtenReview?.let { rev -> AppointmentDetailsWrittenReview(
                    customerAvatar = a.customer.avatar ?: "",
                    review = rev.review,
                    rating = rev.rating
                )}

                if(!a.hasVideoReview && a.status == AppointmentStatusEnum.FINISHED) {
                    VideoReviewCTA(onClick = {})
                }

                a.message?.let { AppointmentDetailsMessage(it) }
            }

            Text(
                modifier = Modifier.padding(top = SpacingXL),
                text = stringResource(R.string.location),
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            LocationSection()

            Spacer(Modifier.height(BasePadding))
        }
    }
}