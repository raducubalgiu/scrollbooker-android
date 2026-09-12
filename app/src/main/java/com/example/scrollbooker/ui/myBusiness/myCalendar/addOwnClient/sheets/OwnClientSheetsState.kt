package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import com.example.scrollbooker.components.customized.calendar.CalendarHeaderState
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClient
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import org.threeten.bp.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
data class OwnClientSheetsState(
    val currentSheet: AddOwnClientSheet?,
    val sheetState: SheetState,
    val servicesSheetState: SheetState,
    // select client
    val clientQuery: String,
    val clientSearchState: FeatureState<List<BusinessClient>>?,
    val selectedClient: BusinessClient?,
    // add client
    val isCreatingClient: Boolean,
    // services
    val linkedItems: List<SelectedBookingItem>,
    val userProducts: FeatureState<UserProducts>,
    // date time
    val calendarHeaderState: FeatureState<CalendarHeaderState>,
    val selectedCalendarDay: LocalDate?,
    val daySlots: FeatureState<AvailableDay>?,
    val startOnSlotsStep: Boolean,
    val initialPendingSlotUtc: String?,
)
