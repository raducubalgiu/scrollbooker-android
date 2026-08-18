package com.example.scrollbooker.ui.booking.specialists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.placeholderActionBox.PlaceholderActionBox
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlow
import com.example.scrollbooker.entity.booking.products.domain.model.ProductOffering
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSpecialistsList(
    bookingFlow: BookingFlow,
    selectedBookingItems: List<SelectedBookingItem>,
    selectedEmployeeId: Int?,
    onSetSelectedEmployeeId: (Int?) -> Unit,
    onRemoveBookingItem: (SelectedBookingItem) -> Unit
) {
    val allowedEmployeeIds: Set<Int> = remember(selectedBookingItems) {
        selectedBookingItems
            .flatMap { item -> item.offerings.map { offering -> offering.user.id } }
            .toSet()
    }

    val filteredEmployees = remember(bookingFlow.employees, allowedEmployeeIds) {
        bookingFlow.employees.filter { employee -> employee.id in allowedEmployeeIds }
    }

    val selectedEmployee = filteredEmployees.find { it.id == selectedEmployeeId }

    fun getSelectedOffering(item: SelectedBookingItem): ProductOffering? {
        return item.offerings.find { o -> o.user.id == selectedEmployeeId }
    }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if(sheetState.isVisible) {
        SelectSpecialistsSheet(
            sheetState = sheetState,
            employees = filteredEmployees,
            selectedEmployee = selectedEmployee,
            onConfirmEmployee = {
               onSetSelectedEmployeeId(it.id)
                scope.launch { sheetState.hide() }
            },
            onClose = {
                scope.launch { sheetState.hide() }
            }
        )
    }

    EmployeeSelectDropdown(
        selectedEmployee = selectedEmployee,
        onClick = { scope.launch { sheetState.show() } }
    )

    if (selectedEmployeeId == null) {
        PlaceholderActionBox(description = stringResource(R.string.chooseSpecialistDescription))
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = BasePadding)
        ) {
            items(selectedBookingItems, key = { it.productId }) { item ->
                ProductOfferingCard(
                    item = item,
                    selectedEmployeeId = selectedEmployeeId,
                    employees = filteredEmployees,
                    currentOffering = getSelectedOffering(item),
                    onRemoveItem = { onRemoveBookingItem(item) }
                )
            }
        }
    }
}