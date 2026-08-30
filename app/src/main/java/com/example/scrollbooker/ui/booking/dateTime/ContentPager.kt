package com.example.scrollbooker.ui.booking.dateTime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.availability.domain.model.Slot
import com.example.scrollbooker.ui.shared.calendar.components.slots.FullyBookedDayMessage
import com.example.scrollbooker.ui.shared.calendar.components.slots.SlotItem
import com.example.scrollbooker.ui.shared.calendar.components.slots.SlotsShimmer
import org.threeten.bp.LocalDate

@Composable
fun ContentPager(
    dayPagerState: PagerState,
    timeSlots: FeatureState<AvailableDay>,
    nextAvailableDay: LocalDate?,
    onNavigateToDay: (LocalDate) -> Unit,
    onSlotSelected: (Slot) -> Unit
) {
    HorizontalPager(
        state = dayPagerState,
        modifier = Modifier.fillMaxWidth(),
        beyondViewportPageCount = 1
    ) { pageIndex ->
        when (val slots = timeSlots) {
            is FeatureState.Loading -> SlotsShimmer()
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                val availableSlotsList = slots.data.availableSlots

                if (availableSlotsList.isEmpty()) {
                    FullyBookedDayMessage(
                        onNextAvailableDayClick = nextAvailableDay?.let { day ->
                            { onNavigateToDay(day) }
                        }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = BasePadding),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(availableSlotsList) { slot ->
                            SlotItem(
                                slot = slot,
                                onSelectSlot = onSlotSelected
                            )
                        }
                    }
                }
            }
        }
    }
}