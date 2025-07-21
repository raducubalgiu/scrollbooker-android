package com.example.scrollbooker.modules.calendar.components.slots

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.calendar.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.calendar.domain.model.Slot

@Composable
fun SlotsList(
    availableDayTimeslots: FeatureState<AvailableDay>,
    onSelectSlot: (Slot) -> Unit
) {
    when(val day = availableDayTimeslots) {
        is FeatureState.Error -> FullyBookedDayMessage(onClick = {})
        is FeatureState.Loading -> SlotsShimmer()
        is FeatureState.Success -> {
            val day = day.data

            when {
                day.isClosed -> ClosedDayMessage(onClick = {})
                day.availableSlots.isEmpty() -> FullyBookedDayMessage(onClick = {})
            }

            LazyColumn {
                items(day.availableSlots) { slot ->
                    Spacer(Modifier.height(BasePadding))

                    SlotItem(
                        slot = slot,
                        onSelectSlot = onSelectSlot
                    )
                }
            }
        }
    }
}