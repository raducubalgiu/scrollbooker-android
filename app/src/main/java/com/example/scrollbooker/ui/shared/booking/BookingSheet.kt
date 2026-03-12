package com.example.scrollbooker.ui.shared.booking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.tabs.TabUI
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServicesResponse
import com.example.scrollbooker.ui.myBusiness.myProducts.ServicesTabsState
import com.example.scrollbooker.ui.shared.booking.steps.BookingCalendarStep
import com.example.scrollbooker.ui.shared.booking.steps.BookingConfirmationStep
import com.example.scrollbooker.ui.shared.booking.steps.BookingProductsStep
import com.example.scrollbooker.ui.shared.booking.steps.BookingSpecialistStep
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.components.BookingSheetHeader

sealed class BookingTab(
    override val route: String,
    override val title: Int
): TabUI {
    object Products: BookingTab(route = "products", title = R.string.chooseServices)
    object Specialists: BookingTab(route = "specialists", title = R.string.chooseSpecialist)
    object Calendar: BookingTab(route = "calendar", title = R.string.verifyAvailability)
    object Confirmation: BookingTab(route = "confirmation", title = R.string.confirmReservation)

    companion object {
        fun getTabs(): List<BookingTab> {
            return listOf(
                Products,
                Specialists,
                Calendar,
                Confirmation
            )
        }
    }
}

@Composable
fun BookingSheet(
    serviceDomains: FeatureState<ServiceDomainWithEmployeeServicesResponse>,
    tabsState: ServicesTabsState
) {
    val scope = rememberCoroutineScope()

    val tabs = remember { BookingTab.getTabs() }
    val pagerState = rememberPagerState { tabs.size }
    val currentStep = pagerState.currentPage

    Column(Modifier.fillMaxSize()) {
        BookingSheetHeader(
            stepTitle = stringResource(tabs[currentStep].title),
            onClose = {}
        )

        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        ) {
            VerticalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                userScrollEnabled = false
            ) { page ->
                val step = tabs[page]

                key(step) {
                    when(step) {
                        BookingTab.Products -> {
                            BookingProductsStep(
                                serviceDomains = serviceDomains,
                                tabsState = tabsState,
                                products = TODO(),
                                onSelectDomain = TODO(),
                                onSelectService = TODO(),
                                onSelectEmployee = TODO()
                            )
                        }
                        BookingTab.Calendar -> BookingCalendarStep()
                        BookingTab.Specialists -> BookingSpecialistStep()
                        BookingTab.Confirmation -> BookingConfirmationStep()
                    }
                }
            }
        }
    }
}