package com.example.scrollbooker.ui.appointments
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.appointment.domain.model.displayAppointmentDate
import com.example.scrollbooker.entity.booking.appointment.domain.model.getStatusColor
import com.example.scrollbooker.entity.booking.appointment.domain.model.getStatusRes
import com.example.scrollbooker.ui.appointments.components.AppointmentDetailsActions
import com.example.scrollbooker.ui.appointments.components.AppointmentProductPrice
import com.example.scrollbooker.ui.appointments.components.ReviewCTA
import com.example.scrollbooker.ui.appointments.components.VideoReviewCTA
import com.example.scrollbooker.ui.appointments.sheets.AddReviewSheet
import com.example.scrollbooker.ui.shared.location.LocationSection
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.BookingsSheet
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.headlineMedium
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
    var selectedRating by remember { mutableStateOf<Int?>(null) }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val reviewSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

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
            onClose = { scope.launch { reviewSheetState.hide()} }
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
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier
                        .clip(ShapeDefaults.Medium)
                        .background(a.getStatusColor().copy(0.2f))
                        .padding(vertical = SpacingXS, horizontal = SpacingM)
                    ) {
                        Text(
                            text = stringResource(a.getStatusRes()),
                            color = a.getStatusColor(),
                            style = titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(SpacingXL))

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = a.displayAppointmentDate(),
                            style = headlineMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(SpacingS))

                        Text(
                            text = "(${a.totalDuration} min)",
                            style = bodyLarge,
                            color = Color.Gray
                        )
                    }

                    Spacer(Modifier.height(SpacingXL))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if(a.user.ratingsAverage != null) {
                            AvatarWithRating(
                                url = a.user.avatar ?: "",
                                onClick = {},
                                rating = a.user.ratingsAverage,
                            )
                        } else {
                            Avatar(
                                url = a.user.avatar ?: "",
                                onClick = {},
                            )
                        }

                        Spacer(Modifier.width(SpacingM))

                        Column {
                            Text(
                                text = a.user.fullName,
                                style = titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${a.user.profession} - ${a.user.ratingsCount} ${stringResource(R.string.reviews)}",
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

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

                ReviewCTA(
                    modifier = Modifier.padding(bottom = SpacingXL),
                    onRatingClick = {
                        scope.launch {
                            selectedRating = it
                            reviewSheetState.show()
                        }
                    }
                )

                VideoReviewCTA(onClick = {})

                a.message?.let {
                    Row(
                        modifier = Modifier.padding(top = SpacingXL),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            contentDescription = null,
                            tint = Error
                        )
                        Spacer(Modifier.width(SpacingS))
                        Text(
                            text = "${stringResource(R.string.cancelReason)}: ${appointment?.message ?: ""}",
                            color = Error,
                            style = bodyMedium
                        )
                    }
                }
            }

            Spacer(Modifier.height(SpacingXL))

            Text(
                text = stringResource(R.string.location),
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            LocationSection()

            Spacer(Modifier.height(BasePadding))
        }
    }
}