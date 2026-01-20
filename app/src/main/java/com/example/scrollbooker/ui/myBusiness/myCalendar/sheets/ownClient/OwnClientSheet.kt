package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientAction.Close
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientAction.CreateLastMinute
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientAction.CreateOwnClient
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import toPrettyDate
import toPrettyFullDate
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnClientSheet(
    state: OwnClientSheetState,
    onAction: (OwnClientAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current

    val context = LocalContext.current
    var previousIsSaving by rememberSaveable { mutableStateOf(false) }
    val isSaving = state.isSaving

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 2 }

    val (form, onEvent) = rememberOwnClientFormState()

    val validation = remember(form) { form.validate(context) }

    var showErrors by rememberSaveable { mutableStateOf(false) }

    val selectedOwnClientSlot = state.selectedOwnClientSlot

    var lastMinuteDiscount by rememberSaveable { mutableStateOf("0") }

    val imeInsets = WindowInsets.ime

    val imeVisible by remember {
        derivedStateOf {
            imeInsets.getBottom(density) > 0
        }
    }

    LaunchedEffect(isSaving) {
        if(previousIsSaving && !isSaving) {
            focusManager.clearFocus(force = true)
            keyboard?.hide()

            if(imeVisible) {
                withTimeoutOrNull(500) {
                    snapshotFlow { imeVisible }
                        .filter { !it }
                        .first()
                }
                delay(150)
            }

            onAction(Close)
        }
        previousIsSaving = isSaving
    }

    Column(modifier = Modifier.statusBarsPadding()) {
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

        val subTitle = if (selectedOwnClientSlot != null) "$startLocalDate \u2022 $startLocalTime - $endLocalTime"
                       else state.selectedDay?.toPrettyFullDate()

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
                    subTitle = subTitle ?: "",
                    onClose = { onAction(Close) }
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
                                showErrors = showErrors,
                                hasSelectedSlot = selectedOwnClientSlot != null
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
                                onAction(CreateOwnClient(
                                    AppointmentOwnClientCreate(
                                        startDate = slot.startDateUtc,
                                        endDate = slot.endDateUtc,
                                        customerFullname = form.customerName,
                                        productName = form.productName,
                                        price = BigDecimal(form.price),
                                        priceWithDiscount = BigDecimal(form.price),
                                        discount = BigDecimal(0),
                                        duration = state.slotDuration,
                                    )
                                ))
                            }
                        } else {
                            selectedOwnClientSlot?.let { slot ->
                                onAction(CreateLastMinute(
                                    AppointmentLastMinuteRequest(
                                        startDate = slot.startDateUtc,
                                        endDate = slot.endDateUtc,
                                        discount = BigDecimal(lastMinuteDiscount)
                                    )
                                ))
                            }
                        }
                    }
                )
            }
        }
    }
}