package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectDateTime
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.customized.Refresh
import com.example.scrollbooker.components.customized.calendar.FullyBookedDayMessage
import com.example.scrollbooker.components.customized.calendar.SlotItem
import com.example.scrollbooker.components.customized.calendar.SlotsShimmer
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.availability.domain.model.Slot
import com.example.scrollbooker.ui.theme.titleLarge
import org.threeten.bp.LocalDate
import toPrettyDate

@Composable
fun DateTimeSlotsView(
    day: LocalDate?,
    daySlots: FeatureState<AvailableDay>?,
    isRefreshing: Boolean,
    pendingSlotUtc: String?,
    onSlotSelected: (Slot) -> Unit,
    onRefresh: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        if (day != null) {
            Text(
                modifier = Modifier.padding(horizontal = SpacingXL, vertical = BasePadding),
                text = day.toPrettyDate(),
                style = titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Refresh(isRefreshing = isRefreshing, onRefresh = onRefresh) {
            when (val slots = daySlots) {
                null, is FeatureState.Loading -> SlotsShimmer()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success -> {
                    val availableSlotsList = slots.data.availableSlots

                    if (availableSlotsList.isEmpty()) {
                        FullyBookedDayMessage()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = BasePadding),
                            verticalArrangement = Arrangement.spacedBy(SpacingS)
                        ) {
                            items(availableSlotsList) { slot ->
                                SlotItem(
                                    slot = slot,
                                    isSelected = slot.startDateUtc == pendingSlotUtc,
                                    onSelectSlot = onSlotSelected
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
