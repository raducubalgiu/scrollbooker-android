package com.example.scrollbooker.ui.shared.calendar.components.slots
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.calendar.domain.model.Slot
import com.example.scrollbooker.ui.shared.calendar.CalendarViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotsList(
    viewModel: CalendarViewModel,
    onSelectSlot: (Slot) -> Unit
) {
    val scope = rememberCoroutineScope()

    val state by viewModel.availableDayState.collectAsState()
    val isRefreshing = state is FeatureState.Loading
    val pullToRefreshState = rememberPullToRefreshState()

    when(state) {
        is FeatureState.Error -> FullyBookedDayMessage(onClick = {})
        is FeatureState.Loading -> SlotsShimmer()
        is FeatureState.Success -> {
            val day = (state as FeatureState.Success).data

            when {
                day.isClosed -> ClosedDayMessage(onClick = {})
                day.availableSlots.isEmpty() -> FullyBookedDayMessage(onClick = {})
            }

            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = {
                    scope.launch {
                        viewModel.handleRefresh()
                    }
                }
            ) {
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
}