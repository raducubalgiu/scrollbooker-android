package com.example.scrollbooker.ui.myBusiness.myCalendar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.extensions.displayDatePeriod
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.CalendarBlockAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarHeader
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarHeaderState
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarHeaderStateAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsSheetState
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.settings.MyCalendarSettingsSheet
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.enums.toDomainColor
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.DayTimeline
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientSheet
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarScreen(
    viewModel: MyCalendarViewModel,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val headerState by viewModel.calendarHeader.collectAsState()
    val slotDuration by viewModel.slotDuration.collectAsState()

    val defaultBlockedLocalDates by viewModel.defaultBlockedStartLocale.collectAsState()
    val blockedLocalDates by viewModel.selectedStartLocale.collectAsState()

    val calendarEvents by viewModel.calendarEvents.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val selectedOwnClient by viewModel.selectedOwnClient.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    var message by rememberSaveable { mutableStateOf("") }
    var isBlocking by rememberSaveable { mutableStateOf(false) }

    fun handleDayChange(day: LocalDate) { viewModel.setDay(day) }

    val blockSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val settingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showOwnClientSheet by rememberSaveable { mutableStateOf(false) }

    val ownClientSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newState ->
            when (newState) {
                SheetValue.Hidden -> false
                else -> true
            }
        }
    )

    val blockSheetUIState = BlockSlotsSheetState(
        message = message,
        slotCount = blockedLocalDates.size - defaultBlockedLocalDates.size,
        selectedSlots = blockedLocalDates - defaultBlockedLocalDates,
        selectedDay = selectedDay,
        isSaving = isSaving
    )

    if (blockSheetState.isVisible) {
        BlockSlotsSheet(
            sheetState = blockSheetState,
            state = blockSheetUIState,
            onAction = { action ->
                when(action) {
                    is BlockSlotsAction.Confirm -> {
                        viewModel.blockAppointments(message)
                        isBlocking = false
                    }
                    is BlockSlotsAction.Dismiss -> scope.launch { blockSheetState.hide() }
                    is BlockSlotsAction.MessageChanged -> { message = action.value }
                }
            }
        )
    }

    if(settingsSheetState.isVisible) {
        MyCalendarSettingsSheet(
            sheetState = settingsSheetState,
            onDismiss = { scope.launch { settingsSheetState.hide() } }
        )
    }

    if(showOwnClientSheet) {
        OwnClientSheet(
            sheetState = ownClientSheetState,
            isSaving = isSaving,
            selectedOwnClientSlot = selectedOwnClient,
            slotDuration = slotDuration,
            onCreateOwnClient = { viewModel.createOwnClientAppointment(it) },
            onCreateLastMinute = { viewModel.createLastMinute(it) },
            onClose = {
                scope.launch { ownClientSheetState.hide() }
                showOwnClientSheet = false
            },
        )
    }

    Scaffold(
        bottomBar = { if(isBlocking) {
            CalendarBlockAction(
                isEnabled = defaultBlockedLocalDates != blockedLocalDates,
                onCancel = {
                    viewModel.resetSelectedLocalDates()
                    isBlocking = false
                },
                onBlockConfirm = { scope.launch { blockSheetState.show() } }
            )}
        },
        floatingActionButton = {
            val calendarEventsData = (calendarEvents as? FeatureState.Success)?.data
            val containerColor = calendarEventsData?.businessShortDomain?.toDomainColor() ?: Primary

            if(calendarEvents is FeatureState.Success) {
                IconButton(
                    modifier = Modifier.size(50.dp),
                    onClick = {},
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = containerColor,
                        contentColor = OnPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when(headerState) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    val calendar = (headerState as FeatureState.Success).data
                    val config = calendar.config
                    val calendarDays = calendar.calendarDays
                    val availableDays = calendar.calendarAvailableDays

                    if(calendar.calendarDays.isEmpty()) {
                        ErrorScreen()
                        return@Box
                    }

                    val weekPagerState = rememberPagerState(initialPage = config.initialWeekPage) { config.totalWeeks }
                    val dayPagerState = rememberPagerState(initialPage = config.initialDayPage) { 7 }

                    val currentDayIndex = dayPagerState.currentPage
                    val currentWeekIndex = weekPagerState.currentPage

                    LaunchedEffect(currentWeekIndex, currentDayIndex) {
                        val dayIndex = currentWeekIndex * 7 + currentDayIndex
                        val day = calendarDays.getOrNull(dayIndex)
                        if(day != null) {
                            handleDayChange(day)
                        }
                    }

                    val currentWeekDates = calendarDays.drop(currentWeekIndex * 7).take(7)
                    val period = displayDatePeriod(currentWeekDates.first(), currentWeekDates.last())

                    val enableBack = currentWeekIndex > 0
                    val enableNext = currentWeekIndex < config.totalWeeks - 1

                    fun handlePreviousWeek() {
                        scope.launch { weekPagerState.animateScrollToPage(currentWeekIndex - 1) }
                    }

                    fun handleNextWeek() {
                        scope.launch { weekPagerState.animateScrollToPage(currentWeekIndex + 1) }
                    }

                    val myCalendarUIState = MyCalendarHeaderState(
                        weekPagerState = weekPagerState,
                        selectedTabIndex = dayPagerState.currentPage,
                        period = period,
                        slotDuration = slotDuration,
                        isBlocking = isBlocking,
                        enableBack = enableBack,
                        enableNext = enableNext,
                        availableDays = availableDays,
                        calendarDays = calendarDays
                    )

                    Column(modifier = Modifier.fillMaxSize()) {
                        Header(
                            customTitle = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = stringResource(R.string.calendar),
                                        color = Color.Gray,
                                        style = bodyLarge
                                    )
                                    Text(
                                        text = myCalendarUIState.period,
                                        fontWeight = FontWeight.SemiBold,
                                        style = titleMedium
                                    )
                                }
                            },
                            onBack = onBack,
                            actions = {
                                CustomIconButton(
                                    painter = R.drawable.ic_settings_outline,
                                    onClick = {}
                                )
                            }
                        )

                        MyCalendarHeader(
                            state = myCalendarUIState,
                            onAction = { action ->
                                when(action) {
                                    is MyCalendarHeaderStateAction.HandleNextWeek -> handleNextWeek()
                                    is MyCalendarHeaderStateAction.HandlePreviousWeek -> handlePreviousWeek()
                                    is MyCalendarHeaderStateAction.OnChangeTab -> {
                                        scope.launch {
                                            handleDayChange(action.date)
                                            dayPagerState.animateScrollToPage(action.index)
                                        }
                                    }
                                    is MyCalendarHeaderStateAction.OnIsBlocking -> {
                                        if(!isBlocking) isBlocking = true
                                        else {
                                            viewModel.resetSelectedLocalDates()
                                            isBlocking = false
                                        }
                                    }
                                    is MyCalendarHeaderStateAction.OnSlotChange -> {
                                        viewModel.setSlotDuration(duration = action.slotDuration)
                                    }
                                }
                            },
                        )

                        HorizontalPager(
                            state = dayPagerState,
                            pageSize = PageSize.Fill,
                            beyondViewportPageCount = 0,
                            modifier = Modifier.fillMaxSize()
                        ) { index ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = BasePadding),
                                contentAlignment = Alignment.TopStart
                            ) {
                                when(val events = calendarEvents) {
                                    is FeatureState.Error -> ErrorScreen()
                                    is FeatureState.Loading -> LoadingScreen()
                                    is FeatureState.Success -> {
                                        val calendarEvents = events.data
                                        val slots = events.data.days.first().slots

                                        val dayStart = calendarEvents.minSlotTime?.toLocalTime()
                                        val dayEnd = calendarEvents.maxSlotTime?.toLocalTime()

                                        if(dayStart != null && dayEnd != null) {
                                            DayTimeline(
                                                slotDurationMinutes = slotDuration,
                                                slots = slots,
                                                dayStart = dayStart,
                                                dayEnd = dayEnd,
                                                isBlocking = isBlocking,
                                                onStyleResolver = { slot -> with(calendarEvents) { slot.resolveUiStyle() } },
                                                defaultBlockedLocalDates = defaultBlockedLocalDates,
                                                blockedLocalDates = blockedLocalDates,
                                                onSlotClick = { slot ->
                                                    when {
                                                        isBlocking -> {
                                                            slot.startDateLocale?.let {
                                                                viewModel.setBlockDate(slot.startDateLocale)
                                                            }
                                                        }
                                                        slot.isBooked -> {
                                                            // Should Redirect to DetailScreen
                                                        }
                                                        else -> {
                                                            viewModel.setSelectedOwnClient(slot)
                                                            scope.launch {
                                                                ownClientSheetState.show()
                                                                showOwnClientSheet = true
                                                            }
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}