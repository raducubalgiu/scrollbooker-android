package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.core.extensions.displayShortDayOfWeek
import com.example.scrollbooker.core.util.AppLocaleProvider
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderActions
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderState
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderStateAction
import com.example.scrollbooker.ui.shared.calendar.components.CalendarDayTab
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun MyCalendarHeader(
    state: MyCalendarHeaderState,
    onAction: (MyCalendarHeaderStateAction) -> Unit
) {
    Header(
        customTitle = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.calendar),
                    color = Color.Gray,
                    style = bodyLarge
                )
                Text(
                    text = state.period,
                    fontWeight = FontWeight.SemiBold,
                    style = titleMedium
                )
            }
        },
        onBack = { onAction(MyCalendarHeaderStateAction.Back) },
        actions = {
            CustomIconButton(
                painter = R.drawable.ic_settings_outline,
                onClick = { onAction(MyCalendarHeaderStateAction.Settings) }
            )
        }
    )

    MyCalendarHeaderActions(
        isBlocking = state.isBlocking,
        slotDuration = state.slotDuration.toString(),
        enableBack = state.enableBack,
        enableNext = state.enableNext,
        hasFreeSlots = state.hasFreeSlots,
        handlePreviousWeek = { onAction(MyCalendarHeaderStateAction.HandlePreviousWeek) },
        handleNextWeek = { onAction(MyCalendarHeaderStateAction.HandleNextWeek) },
        onSlotChange = { onAction(MyCalendarHeaderStateAction.OnSlotChange(it)) },
        onBlockToggle = { onAction(MyCalendarHeaderStateAction.OnBlockToggle) }
    )

    HorizontalPager(
        state = state.weekPagerState,
        pageSize = PageSize.Fill,
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .padding(horizontal = BasePadding),
        beyondViewportPageCount = 0
    ) { page ->
        val weekDates = state.calendarDays.drop(page * 7).take(7)

        TabRow(
            selectedTabIndex = state.selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = OnBackground,
            indicator = {},
        ) {
            weekDates.forEachIndexed { index, date ->
                val isAvailable = state.availableDays.contains(date) == true
                val isCurrentTab = index == state.selectedTabIndex

                CalendarDayTab(
                    date = date,
                    isCurrentTab = isCurrentTab,
                    onChangeTab = { onAction(MyCalendarHeaderStateAction.OnChangeTab(date, index)) },
                    bgColor = if(isCurrentTab) Primary else Color.Transparent,
                    label = displayShortDayOfWeek(date, AppLocaleProvider.current()),
                    isLoading = false,
                    isDayAvailable = isAvailable
                )
            }
        }
    }
}