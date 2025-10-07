package com.example.scrollbooker.ui.myBusiness.myCalendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.displayDatePeriod
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.CalendarBlockAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.DayTimeline
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarHeader
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarHeaderState
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.MyCalendarHeaderStateAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.block.BlockSlotsAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.block.BlockSlotsSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.block.BlockSlotsSheetState
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.settings.MyCalendarSettingsSheet
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.Locale
import androidx.compose.material3.SheetValue
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.ownClient.OwnClientSheet

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyCalendarScreen(
    viewModel: MyCalendarViewModel,
    onBack: () -> Unit
) {
    val locale = Locale("ro")
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

    var showOwnClientSheet by remember { mutableStateOf(false) }

    val ownClientSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newState ->
            when (newState) {
                SheetValue.Hidden -> {
                    false
                }
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

    Scaffold(
        topBar = { ScreenHeader(onBack, onClick = { scope.launch { settingsSheetState.show() } }) },
        bottomBar = { if(isBlocking) {
            CalendarBlockAction(
                isEnabled = defaultBlockedLocalDates != blockedLocalDates,
                onCancel = {
                    viewModel.resetSelectedLocalDates()
                    isBlocking = false
                },
                onBlockConfirm = { scope.launch { blockSheetState.show() } }
            )}
        }
    ) { innerPadding ->
        if(showOwnClientSheet) {
            OwnClientSheet(
                viewModel = viewModel,
                sheetState = ownClientSheetState,
                paddingTop = innerPadding.calculateTopPadding(),
                selectedOwnClientSlot = selectedOwnClient,
                slotDuration = slotDuration,
                onCreateOwnClient = { viewModel.createOwnClientAppointment(it) },
                onClose = {
                    scope.launch { ownClientSheetState.hide() }
                    showOwnClientSheet = false
                },
            )
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
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
                    val period = displayDatePeriod(currentWeekDates.first(), currentWeekDates.last(), locale)
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
                                                slotDuration = slotDuration,
                                                slots = slots,
                                                dayStart = dayStart,
                                                dayEnd = dayEnd,
                                                isBlocking = isBlocking,
                                                defaultBlockedLocalDates = defaultBlockedLocalDates,
                                                blockedLocalDates = blockedLocalDates,
                                                onBlock = { viewModel.setBlockDate(it) },
                                                onSlotClick = {
                                                    viewModel.setSelectedOwnClient(it)
                                                    scope.launch {
                                                        ownClientSheetState.show()
                                                        showOwnClientSheet = true
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

@Composable
private fun ScreenHeader(
    onBack: () -> Unit,
    onClick: () -> Unit
) {
    Header(
        title = stringResource(R.string.calendar),
        onBack = onBack,
        actions = {
            CustomIconButton(
                painter = R.drawable.ic_settings_outline,
                onClick = onClick
            )
        }
    )
}