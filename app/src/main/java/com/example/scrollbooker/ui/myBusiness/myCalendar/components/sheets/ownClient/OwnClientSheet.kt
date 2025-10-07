package com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.ownClient

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.buttons.SegmentedButtons
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.parseDateStringFromLocalDateTimeString
import com.example.scrollbooker.core.util.parseTimeStringFromLocalDateTimeString
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnClientSheet(
    sheetState: SheetState,
    paddingTop: Dp,
    selectedOwnClientSlot: CalendarEventsSlot?,
    slotDuration: Int,
    onCreateOwnClient: (AppointmentOwnClientCreate) -> Unit,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 2 }

    val (form, onEvent) = rememberOwnClientFormState()

    ModalBottomSheet(
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

        BoxWithConstraints(modifier = Modifier
            .fillMaxWidth()
        ) {
            val targetHeight = maxHeight - paddingTop

            val density = LocalDensity.current
            var buttonHeightPx by remember { mutableIntStateOf(0) }
            val buttonHeightDp = with(density) { buttonHeightPx.toDp() }

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(targetHeight)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = SpacingXL)
                ) {
                    OwnClientHeader(
                        title = "Administreaza intervalul",
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

                    HorizontalPager(
                        state = pagerState
                    ) { page ->
                        when(page) {
                            0 -> {
                                OwnClientCreateTab(
                                    buttonHeightDp = buttonHeightDp,
                                    ownClientState = form,
                                    onEvent = onEvent,
                                )
                            }
                            1 -> OwnClientLastMinuteTab(buttonHeightDp)
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
                        onClick = {
                            selectedOwnClientSlot?.let { slot ->
                                Timber.tag("CREATE OWN CLIENT").e("FORM STATE, $form")
                                val currencyId = form.selectedCurrencyId?.toInt()

                                Timber.tag("CREATE OWN CLIENT").e("SELECTED CURRENCY ID, $currencyId")

                                if(currencyId != null) {
                                    onCreateOwnClient(AppointmentOwnClientCreate(
                                        startDate = slot.startDateUtc,
                                        endDate = slot.endDateUtc,
                                        customerFullname = form.customerName,
                                        serviceName = form.serviceName,
                                        productName = form.productName,
                                        productPrice = BigDecimal(form.price),
                                        productPriceWithDiscount = BigDecimal(form.priceWithDiscount),
                                        productDiscount = BigDecimal(form.discount),
                                        productDuration = slotDuration,
                                        currencyId = currencyId,
                                        serviceId = form.selectedServiceId?.toInt(),
                                        productId = form.selectedProductId?.toInt(),
                                    ))
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}