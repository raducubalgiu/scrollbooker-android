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
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs.ProductsTab
import com.example.scrollbooker.ui.shared.products.UserProductsViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentScrollBookerCreate

@Composable
fun BookingsSheet(
    modifier: Modifier = Modifier,
    userId: Int,
    initialPage: Int = 0,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val steps = listOf(
        "Alege serviciile",
        "Alege ora",
        "Confirma programarea"
    )

    val bookingsSheetViewModel: BookingsSheetViewModel = hiltViewModel()
    val productsViewModel: UserProductsViewModel = hiltViewModel()
    val calendarViewModel: CalendarViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        productsViewModel.reset()
        calendarViewModel.reset()
    }

    val selectedProducts by productsViewModel.selectedProducts.collectAsState()
    val totalDuration = selectedProducts.sumOf { it.duration }
    val totalPrice = selectedProducts.sumOf { it.priceWithDiscount }

    val selectedSlot by calendarViewModel.selectedSlot.collectAsState()

    val pagerState = rememberPagerState(initialPage = initialPage) { steps.size }
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
            stepTitle = steps[currentStep],
            onClose = onClose,
            totalSteps = steps.size,
            currentStep = currentStep
        )

        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            VerticalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                userScrollEnabled = false
            ) { page ->
                when(page) {
                    0 -> ProductsTab(
                        productsViewModel = productsViewModel,
                        onSelect = { productsViewModel.toggleProductId(it) },
                        userId = userId,
                        selectedProducts = selectedProducts,
                        totalPrice = totalPrice,
                        totalDuration = totalDuration,
                        onNext = {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        },
                    )
                    1 -> CalendarTab(
                        calendarViewModel = calendarViewModel,
                        slotDuration = totalDuration,
                        userId = userId,
                        onSelectSlot = {
                            calendarViewModel.setSelectedSlot(it)
                            scope.launch { pagerState.animateScrollToPage(page + 1) }
                        }
                    )
                    2 -> ConfirmTab(
                        selectedSlot = selectedSlot,
                        isSaving = isSaving,
                        onSave = {
                            selectedSlot?.let { slot ->
                                bookingsSheetViewModel.createAppointment(
                                    AppointmentScrollBookerCreate(
                                        startDate = slot.startDateUtc,
                                        endDate = slot.endDateUtc,
                                        userId = userId,
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