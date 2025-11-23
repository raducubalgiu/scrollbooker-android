package com.example.scrollbooker.ui.shared.posts.sheets.bookings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.ui.shared.calendar.CalendarViewModel
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.components.BookingSheetHeader
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs.CalendarTab
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs.ConfirmTab
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs.products.ProductsTab
import com.example.scrollbooker.ui.shared.products.UserProductsViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentScrollBookerCreate

data class BookingsSheetUser(
    val id: Int,
    val username: String,
    val fullName: String,
    val avatar: String?,
    val profession: String,
    val ratingsCount: Int,
    val ratingsAverage: Float
)

@Composable
fun BookingsSheet(
    modifier: Modifier = Modifier,
    user: BookingsSheetUser,
    initialIndex: Int = 1,
    postId: Int? = null,
    appointmentId: Int? = null,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val steps = listOf(
        stringResource(R.string.chooseServices),
        stringResource(R.string.verifyAvailability),
        stringResource(R.string.confirmReservation)
    )

    val bookingsSheetViewModel: BookingsSheetViewModel = hiltViewModel()
    val productsViewModel: UserProductsViewModel = hiltViewModel()
    val calendarViewModel: CalendarViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        appointmentId?.let { bookingsSheetViewModel.loadAppointmentProducts(it) }
        postId?.let { bookingsSheetViewModel.loadPostProducts(it) }
        productsViewModel.reset()
        calendarViewModel.reset()
    }

    val selectedProducts by productsViewModel.selectedProducts.collectAsState()
    val appointmentProducts by bookingsSheetViewModel.appointmentProducts.collectAsState()
    val postProducts by bookingsSheetViewModel.postProducts.collectAsState()

    val totalDuration = selectedProducts.sumOf { it.duration }
    val totalPrice = selectedProducts.sumOf { it.price }
    val totalPriceWithDiscount = selectedProducts.sumOf { it.priceWithDiscount }

    val selectedSlot by calendarViewModel.selectedSlot.collectAsState()

    val pagerState = rememberPagerState { steps.size }
    val currentStep = pagerState.currentPage

    val isSaving by bookingsSheetViewModel.isSaving.collectAsState()
    val createState by bookingsSheetViewModel.createState.collectAsState()

    LaunchedEffect(createState) {
        when(createState) {
            is FeatureState.Success -> {
                bookingsSheetViewModel.consumeCreateState()
                onClose()
            }
            is FeatureState.Error -> {
                bookingsSheetViewModel.consumeCreateState()
            }
            else -> Unit
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        BookingSheetHeader(
            pagerState = pagerState,
            stepTitle = steps[currentStep],
            onClose = onClose,
            totalSteps = steps.size,
            currentStep = currentStep
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            VerticalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                userScrollEnabled = false
            ) { page ->
                when(page) {
                    0 -> ProductsTab(
                        productsViewModel = productsViewModel,
                        initialIndex = initialIndex,
                        appointmentProducts = appointmentId?.let { appointmentProducts },
                        postProducts = postId?.let { postProducts },
                        selectedProducts = selectedProducts,
                        onSelect = { productsViewModel.toggleProductId(it) },
                        totalPrice = totalPriceWithDiscount,
                        totalDuration = totalDuration,
                        userId = user.id,
                        onNext = {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        },
                    )
                    1 -> CalendarTab(
                        calendarViewModel = calendarViewModel,
                        slotDuration = totalDuration,
                        userId = user.id,
                        onSelectSlot = {
                            calendarViewModel.setSelectedSlot(it)
                            scope.launch { pagerState.animateScrollToPage(page + 1) }
                        }
                    )
                    2 -> ConfirmTab(
                        user = user,
                        totalPriceWithDiscount = totalPriceWithDiscount,
                        totalDuration = totalDuration,
                        products = selectedProducts,
                        isSaving = isSaving,
                        onSave = {
                            selectedSlot?.let { slot ->
                                bookingsSheetViewModel.createAppointment(
                                    AppointmentScrollBookerCreate(
                                        startDate = slot.startDateUtc.toString(),
                                        endDate = slot.endDateUtc.toString(),
                                        userId = user.id,
                                        productIds = selectedProducts.map { it.id },
                                        currencyId = 1
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}