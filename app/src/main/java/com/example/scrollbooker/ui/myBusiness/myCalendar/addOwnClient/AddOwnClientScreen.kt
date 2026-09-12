package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.extensions.parseDateTimeStringToLocalDateTime
import com.example.scrollbooker.core.extensions.parseTimeStringFromLocalDateTimeString
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentProductOfferingCreateDto
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentProductVariantCreateDto
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.availability.domain.model.Slot
import com.example.scrollbooker.ui.myBusiness.myCalendar.MyCalendarViewModel
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.AddOwnClientSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.OwnClientSheets
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.OwnClientSheetsState
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.handleOwnClientSheetsAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectDateTime.DateTimeSummaryButton
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyMedium
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import org.threeten.bp.Duration
import toDayMonthShort
import java.math.BigDecimal

private fun Slot.toCalendarEventsSlot(): CalendarEventsSlot = CalendarEventsSlot(
    id = null,
    startDateLocale = parseDateTimeStringToLocalDateTime(startDateLocale),
    endDateLocale = parseDateTimeStringToLocalDateTime(endDateLocale),
    startDateUtc = startDateUtc,
    endDateUtc = endDateUtc,
    isBooked = false,
    isBlocked = false,
    isLastMinute = isLastMinute,
    lastMinuteDiscount = lastMinuteDiscount,
    info = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOwnClientScreen(
    myCalendarViewModel: MyCalendarViewModel,
    viewModel: AddOwnClientViewModel,
    onBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current

    val isSaving by myCalendarViewModel.isSaving.collectAsStateWithLifecycle()
    val selectedOwnClientSlot by myCalendarViewModel.selectedOwnClient.collectAsStateWithLifecycle()
    val successTick by myCalendarViewModel.actionSucceededTick.collectAsStateWithLifecycle()
    val providerUserId by myCalendarViewModel.userId.collectAsStateWithLifecycle()

    val clientQuery by viewModel.query.collectAsStateWithLifecycle()
    val clientSearchState by viewModel.searchState.collectAsStateWithLifecycle()
    val selectedClient by viewModel.selectedClient.collectAsStateWithLifecycle()
    val isCreatingClient by viewModel.isCreating.collectAsStateWithLifecycle()

    val userProducts by viewModel.userProducts.collectAsStateWithLifecycle()
    val linkedProducts by viewModel.linkedProducts.collectAsStateWithLifecycle()

    val calendarHeaderState by viewModel.calendarHeader.collectAsStateWithLifecycle()
    val selectedCalendarDay by viewModel.selectedCalendarDay.collectAsStateWithLifecycle()
    val daySlots by viewModel.daySlots.collectAsStateWithLifecycle()

    var previousIsSaving by rememberSaveable { mutableStateOf(false) }

    var currentSheet by remember { mutableStateOf<AddOwnClientSheet?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val servicesSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.loadUserProducts()
    }

    val imeInsets = WindowInsets.ime
    val imeVisible by remember {
        derivedStateOf { imeInsets.getBottom(density) > 0 }
    }

    fun close() {
        myCalendarViewModel.setSelectedOwnClient(null)
        onBack()
    }

    LaunchedEffect(isSaving) {
        if (previousIsSaving && !isSaving) {
            focusManager.clearFocus(force = true)
            keyboard?.hide()
        }
        previousIsSaving = isSaving
    }

    var baselineSuccessTick by rememberSaveable { mutableStateOf(successTick) }
    LaunchedEffect(successTick) {
        if (successTick != baselineSuccessTick) {
            if (imeVisible) {
                withTimeoutOrNull(500) {
                    snapshotFlow { imeVisible }.filter { !it }.first()
                }
                delay(150)
            }
            close()
        }
        baselineSuccessTick = successTick
    }

    LaunchedEffect(selectedClient) {
        if (selectedClient != null &&
            (currentSheet == AddOwnClientSheet.SelectClient || currentSheet == AddOwnClientSheet.AddClient)
        ) {
            currentSheet = null
        }
    }

    val slot = selectedOwnClientSlot

    val dateTimeSummary = slot?.startDateLocale?.let { start ->
        val datePart = start.toLocalDate().toDayMonthShort()
        val startTime = parseTimeStringFromLocalDateTimeString(start)
        val end = slot.endDateLocale

        if (end != null) {
            val endTime = parseTimeStringFromLocalDateTimeString(end)
            val duration = Duration.between(start, end).toMinutes().toInt()
            "$datePart, $startTime - $endTime ${stringResource(R.string.durationInMinutesParens, duration)}"
        } else {
            "$datePart, $startTime"
        }
    }

    fun handleSlotSelected(pickedSlot: Slot) {
        myCalendarViewModel.setSelectedOwnClient(pickedSlot.toCalendarEventsSlot())
        currentSheet = null
    }

    fun openDateTimePicker() {
        slot?.startDateLocale?.toLocalDate()?.let { viewModel.selectCalendarDay(it) }
        currentSheet = AddOwnClientSheet.DateTime
    }

    val totalDurationMinutes = linkedProducts.sumOf { it.startingOffering.duration }
    val totalPrice = linkedProducts.fold(BigDecimal.ZERO) { acc, product ->
        acc + product.startingOffering.priceWithDiscount
    }

    val isFormValid = selectedClient != null && linkedProducts.isNotEmpty() && slot != null

    fun buildOwnClientRequest(): AppointmentOwnClientCreate? {
        val client = selectedClient ?: return null
        val pickedSlot = slot ?: return null
        val currentUserId = providerUserId ?: return null
        if (linkedProducts.isEmpty()) return null

        return AppointmentOwnClientCreate(
            startDate = pickedSlot.startDateUtc,
            endDate = pickedSlot.endDateUtc,
            userId = currentUserId,
            businessClientId = client.id,
            productVariants = linkedProducts.map { product ->
                AppointmentProductVariantCreateDto(
                    id = product.startingOffering.variantId,
                    offering = AppointmentProductOfferingCreateDto(userId = product.startingOffering.userId)
                )
            }
        )
    }

    OwnClientSheets(
        state = OwnClientSheetsState(
            currentSheet = currentSheet,
            sheetState = sheetState,
            servicesSheetState = servicesSheetState,
            clientQuery = clientQuery,
            clientSearchState = clientSearchState,
            selectedClient = selectedClient,
            isCreatingClient = isCreatingClient,
            linkedProducts = linkedProducts,
            userProducts = userProducts,
            calendarHeaderState = calendarHeaderState,
            selectedCalendarDay = selectedCalendarDay,
            daySlots = daySlots,
            startOnSlotsStep = slot != null,
            initialPendingSlotUtc = slot?.startDateUtc,
        ),
        onAction = { action ->
            handleOwnClientSheetsAction(
                action = action,
                viewModel = viewModel,
                onCloseSheet = { currentSheet = null },
                onSlotConfirmed = ::handleSlotSelected
            )
        }
    )

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        containerColor = Background,
        topBar = {
            OwnClientHeader(
                title = stringResource(R.string.newAppointment),
                onClose = { close() }
            )
        },
        bottomBar = {
            Column(Modifier.imePadding().navigationBarsPadding()) {
                HorizontalDivider(color = Divider, thickness = 0.55.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = BasePadding, vertical = SpacingS),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${stringResource(R.string.durationLabel)}: $totalDurationMinutes min",
                        style = bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "${stringResource(R.string.total)}: ${totalPrice.toTwoDecimals()} RON",
                        style = bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                DateTimeSummaryButton(
                    modifier = Modifier.padding(horizontal = BasePadding),
                    value = dateTimeSummary,
                    enabled = totalDurationMinutes > 0,
                    onClick = ::openDateTimePicker
                )

                MainButton(
                    modifier = Modifier.padding(BasePadding),
                    title = stringResource(R.string.saveAppointment),
                    enabled = isFormValid,
                    isLoading = isSaving,
                    onClick = {
                        buildOwnClientRequest()?.let { request ->
                            myCalendarViewModel.createOwnClientAppointment(request)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpacingXL)
        ) {
            AddOwnClientForm(
                selectedClient = selectedClient,
                onAddNewClient = { currentSheet = AddOwnClientSheet.AddClient },
                onSelectClient = { currentSheet = AddOwnClientSheet.SelectClient },
                onRemoveClient = { viewModel.selectClient(null) },
                linkedProducts = linkedProducts,
                onOpenServicesSheet = { currentSheet = AddOwnClientSheet.Services },
                onRemoveProduct = { viewModel.removeLinkedProduct(it) }
            )
        }
    }
}
