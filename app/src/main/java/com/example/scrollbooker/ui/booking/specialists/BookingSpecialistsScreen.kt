package com.example.scrollbooker.ui.booking.specialists
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.ProductOffering
import com.example.scrollbooker.navigation.navigators.BookingNavigator
import com.example.scrollbooker.ui.booking.BookingLayout
import com.example.scrollbooker.ui.booking.BookingViewModel
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun BookingSpecialistsScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel,
    bookingNavigate: BookingNavigator
) {
    val selectedEmployeeId by viewModel.selectedEmployeeId.collectAsStateWithLifecycle()
    val selectedBookingItems by viewModel.selectedBookingItems.collectAsStateWithLifecycle()
    val bookingFlowState by viewModel.bookingFlowState.collectAsStateWithLifecycle()
    val bookingTotals by viewModel.bookingTotals.collectAsStateWithLifecycle()

    val bookingFlow = (bookingFlowState as FeatureState.Success).data

    val allowedEmployeeIds: Set<Int> = remember(selectedBookingItems) {
        selectedBookingItems
            .flatMap { item -> item.offerings.map { offering -> offering.user.id } }
            .toSet()
    }

    val filteredEmployees = remember(bookingFlow.employees, allowedEmployeeIds) {
        bookingFlow.employees.filter { employee -> employee.id in allowedEmployeeIds }
    }

    fun getSelectedOffering(item: SelectedBookingItem): ProductOffering? {
        return item.offerings.find { o -> o.user.id == selectedEmployeeId }
    }

    BookingLayout(
        modifier = modifier,
        onBack = { bookingNavigate.back() },
        onNext = { bookingNavigate.toDateTime() },
        bookingTotals = bookingTotals,
        isEnabled = selectedEmployeeId != null,
        displayBottomBar = true
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(BasePadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(R.string.chooseSpecialist)
            )

            EmployeeSelectDropdown(
                selectedEmployeeId = selectedEmployeeId,
                employees = filteredEmployees,
                onEmployeeSelected = { id ->
                    viewModel.setSelectedEmployeeId(id)
                }
            )

            if (selectedEmployeeId == null) UnselectedSpecialistOverlay()
            else LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(selectedBookingItems, key = { it.productId }) { item ->
                        ProductOfferingCard(
                            item = item,
                            selectedEmployeeId = selectedEmployeeId,
                            employees = filteredEmployees,
                            currentOffering = getSelectedOffering(item),
                            onRemoveItem = { }
                        )
                    }
            }
        }
    }
}