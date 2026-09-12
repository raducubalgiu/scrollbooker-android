package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClient
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectClient.ClientPickerSection
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectServices.ServicesPickerSection

@Composable
fun AddOwnClientForm(
    selectedClient: BusinessClient?,
    onAddNewClient: () -> Unit,
    onSelectClient: () -> Unit,
    onRemoveClient: () -> Unit,
    linkedItems: List<SelectedBookingItem>,
    onOpenServicesSheet: () -> Unit,
    onRemoveItem: (SelectedBookingItem) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ClientPickerSection(
            selectedClient = selectedClient,
            onAddNewClient = onAddNewClient,
            onSelectClient = onSelectClient,
            onRemoveClient = onRemoveClient,
        )

        Spacer(Modifier.height(BasePadding))

        ServicesPickerSection(
            linkedItems = linkedItems,
            onOpenSheet = onOpenServicesSheet,
            onRemoveItem = onRemoveItem,
        )
    }
}
