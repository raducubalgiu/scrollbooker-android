package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.buttons.SegmentedButtons
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.extensions.parseDateStringFromLocalDateTimeString
import com.example.scrollbooker.core.extensions.parseTimeStringFromLocalDateTimeString
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentLastMinuteRequest
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarViewModel
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnClientSheet(
    sheetState: SheetState,
    isSaving: Boolean,
    selectedOwnClientSlot: CalendarEventsSlot?,
    slotDuration: Int,
    onCreateOwnClient: (AppointmentOwnClientCreate) -> Unit,
    onCreateLastMinute: (AppointmentLastMinuteRequest) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var previousIsSaving by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 2 }

    val (form, onEvent) = rememberOwnClientFormState()
    val validation = remember(form) { form.validate(context) }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    var lastMinuteDiscount by rememberSaveable { mutableStateOf("0") }

    LaunchedEffect(isSaving) {
        if(previousIsSaving && !isSaving) onClose()
        previousIsSaving = isSaving
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = {},
        containerColor = Background,
        dragHandle = {}
    ) {
        val tabs = remember { OwnClientTab.getTabs }

        val startLocalDate = parseDateStringFromLocalDateTimeString(
            selectedOwnClientSlot?.startDateLocale
        )
        val startLocalTime = parseTimeStringFromLocalDateTimeString(
            selectedOwnClientSlot?.startDateLocale
        )

        val endLocalTime = parseTimeStringFromLocalDateTimeString(
            selectedOwnClientSlot?.endDateLocale
        )

        val density = LocalDensity.current
        var buttonHeightPx by remember { mutableIntStateOf(0) }
        val buttonHeightDp = with(density) { buttonHeightPx.toDp() }

        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = SpacingXL)
            ) {
                OwnClientHeader(
                    title = stringResource(R.string.manageSlot),
                    subTitle = "$startLocalDate \u2022 $startLocalTime - $endLocalTime",
                    onClose = onClose
                )

                Spacer(Modifier.height(BasePadding))

                SegmentedButtons(
                    tabs = tabs.map { it.route },
                    selectedIndex = pagerState.currentPage,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    }
                )

                Spacer(Modifier.height(BasePadding))

                HorizontalPager(
                    state = pagerState
                ) { page ->
                    when(page) {
                        0 -> {
                            OwnClientCreateTab(
                                buttonHeightDp = buttonHeightDp,
                                ownClientState = form,
                                onEvent = onEvent,
                                validation = validation,
                                showErrors = showErrors
                            )
                        }
                        1 -> OwnClientLastMinuteTab(
                            slotLabel = "$startLocalDate \u2022 $startLocalTime - $endLocalTime",
                            buttonHeightDp = buttonHeightDp,
                            lastMinuteDiscount = lastMinuteDiscount,
                            onValueChange = { lastMinuteDiscount = it }
                        )
                    }
                }
            }

            Box(
                Modifier
                    .background(Background)
                    .zIndex(5f)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
                    .onGloballyPositioned {
                        buttonHeightPx = it.size.height
                    }
            ) {
                HorizontalDivider(color = Divider, thickness = 0.55.dp)

                MainButton(
                    modifier = Modifier.padding(BasePadding),
                    title = stringResource(R.string.save),
                    enabled = !isSaving,
                    isLoading = isSaving,
                    onClick = {
                        if(pagerState.currentPage == 0) {
                            showErrors = true

                            if(!validation.isValid) return@MainButton

                            selectedOwnClientSlot?.let { slot ->
                                onCreateOwnClient(
                                    AppointmentOwnClientCreate(
                                        startDate = slot.startDateUtc,
                                        endDate = slot.endDateUtc,
                                        customerFullname = form.customerName,
                                        productName = form.productName,
                                        price = BigDecimal(form.price),
                                        priceWithDiscount = BigDecimal(form.price),
                                        discount = BigDecimal(0),
                                        duration = slotDuration,
                                    )
                                )
                            }
                        } else {
                            selectedOwnClientSlot?.let { slot ->
                                onCreateLastMinute(
                                    AppointmentLastMinuteRequest(
                                        startDate = slot.startDateUtc,
                                        endDate = slot.endDateUtc,
                                        lastMinuteDiscount = BigDecimal(lastMinuteDiscount)
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}