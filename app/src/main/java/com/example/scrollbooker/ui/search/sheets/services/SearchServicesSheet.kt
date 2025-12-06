package com.example.scrollbooker.ui.search.sheets.services
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.getIcon
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.components.SearchAdvancedFilters
import com.example.scrollbooker.ui.search.sheets.SearchSheetActions
import com.example.scrollbooker.ui.search.sheets.services.components.SearchBusinessDomainCard
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.search.sheets.services.components.SearchBusinessDomainsSheetList
import com.example.scrollbooker.ui.search.sheets.services.components.SearchServicesSheetFooter
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.headlineLarge
import com.example.scrollbooker.ui.theme.headlineSmall
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.TextStyle
import java.util.Locale

enum class ServicesSheetStep {
    MAIN_FILTERS,
    DATE_TIME
}

@Composable
fun SearchServicesSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit,
    onClear: () -> Unit,
    onFilter: (SearchServicesFiltersSheetState) -> Unit
) {
    val requestState by viewModel.request.collectAsState()
    val filters = requestState.filters

    LaunchedEffect(Unit) {
        if(filters.businessDomainId != null) {
            viewModel.syncBusinessDomain(filters.businessDomainId)
        }
    }

    val businessDomains by viewModel.businessDomains.collectAsState()
    val businessTypes by viewModel.businessTypes.collectAsState()
    val services by viewModel.services.collectAsState()
    val serviceFilters by viewModel.filters.collectAsState()

    val state by viewModel.servicesSheetFilters.collectAsState()

    val businessTypesOptions = when(val state = businessTypes) {
        is FeatureState.Success -> state.data.map { bt ->
            Option(
                value = bt.id.toString(),
                name = bt.name
            )
        }
        else -> emptyList()
    }

    val servicesOptions = when(val state = services) {
        is FeatureState.Success -> state.data.map { bt ->
            Option(
                value = bt.id.toString(),
                name = bt.name
            )
        }
        else -> emptyList()
    }

    var step by remember { mutableStateOf(ServicesSheetStep.MAIN_FILTERS) }

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                // dacă mergem MAIN_FILTERS -> DATE_TIME (înainte)
                if (initialState == ServicesSheetStep.MAIN_FILTERS &&
                    targetState == ServicesSheetStep.DATE_TIME
                ) {
                    slideInHorizontally(
                        animationSpec = tween(250)
                    ) { fullWidth -> fullWidth } + fadeIn() togetherWith
                            slideOutHorizontally(
                                animationSpec = tween(250)
                            ) { fullWidth -> -fullWidth } + fadeOut()
                } else {
                    // invers: DATE_TIME -> MAIN_FILTERS (înapoi)
                    slideInHorizontally(
                        animationSpec = tween(250)
                    ) { fullWidth -> -fullWidth } + fadeIn() togetherWith
                            slideOutHorizontally(
                                animationSpec = tween(250)
                            ) { fullWidth -> fullWidth } + fadeOut()
                }.using(
                    SizeTransform(clip = false)
                )
            },
            label = "servicesSheetStep"
        ) { currentStep ->
            when (currentStep) {
                ServicesSheetStep.MAIN_FILTERS -> {
                    SearchServicesMainFilters(
                        viewModel = viewModel,
                        businessDomains = businessDomains,
                        businessTypes = businessTypes,
                        businessTypesOptions = businessTypesOptions,
                        services = services,
                        servicesOptions = servicesOptions,
                        serviceFilters = serviceFilters,
                        state = state,
                        onOpenDate = {
                            if(step == ServicesSheetStep.MAIN_FILTERS) {
                                step = ServicesSheetStep.DATE_TIME
                            } else {
                                step = ServicesSheetStep.MAIN_FILTERS
                            }
                        }
                    )
                }

                ServicesSheetStep.DATE_TIME -> {
                    val initialDateTime = DateTimeSelection()

                    SearchServicesDateTime(
                        initialSelection = initialDateTime,
                        onCancel = { step = ServicesSheetStep.MAIN_FILTERS },
                        onConfirm = {}
                    )
                }
            }
        }
    }
}

enum class TimePreset {
    ANYTIME,
    MORNING,
    AFTERNOON,
    EVENING,
    CUSTOM
}

data class DateTimeSelection(
    val date: LocalDate = LocalDate.now(),
    val preset: TimePreset = TimePreset.ANYTIME,
    val customStart: LocalTime? = null,
    val customEnd: LocalTime? = null,
)

fun LocalDate.toPrettyLabel(): String {
    val dayOfWeek = this.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val month = this.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    return "$dayOfWeek ${this.dayOfMonth} $month"
}


@Composable
private fun SearchServicesMainFilters(
    viewModel: SearchViewModel,
    businessDomains: FeatureState<List<BusinessDomain>>,
    businessTypes: FeatureState<List<BusinessType>>?,
    businessTypesOptions: List<Option>,
    services: FeatureState<List<Service>>?,
    servicesOptions: List<Option>,
    serviceFilters: FeatureState<List<Filter>>?,
    state: SearchServicesFiltersSheetState,
    onOpenDate: () -> Unit
) {
    val verticalScroll = rememberScrollState()

    Column(Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(top = BasePadding)
            .verticalScroll(verticalScroll)
        ) {
            SearchSheetsHeader(
                title = "",
                onClose = {}
            )

            Column(Modifier.padding(horizontal = BasePadding)) {
                Text(
                    style = headlineLarge,
                    color = OnBackground,
                    fontWeight = FontWeight.ExtraBold,
                    text = stringResource(R.string.services)
                )
                Spacer(Modifier.height(SpacingXXS))
                Text(
                    style = bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    text = stringResource(R.string.youCanFilterServicesBasedOnCategoryTypeAndFilter),
                )
            }

            Spacer(Modifier.height(SpacingXXL))

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SearchBusinessDomainsSheetList(
                    businessDomains = businessDomains,
                    selectedBusinessDomainId = state.businessDomainId,
                    onClick = { viewModel.onSheetBusinessDomainSelected(it) }
                )

                Spacer(Modifier.height(SpacingXXL))

                Column(Modifier.padding(horizontal = BasePadding)) {
                    InputSelect(
                        options = businessTypesOptions,
                        selectedOption = state.businessTypeId.toString(),
                        placeholder = "Alege Business-ul",
                        onValueChange = {
                            viewModel.onSheetBusinessTypeSelected(it?.toInt())
                        },
                        isLoading = businessTypes is FeatureState.Loading,
                    )

                    Spacer(Modifier.height(BasePadding))

                    InputSelect(
                        options = servicesOptions,
                        selectedOption = state.serviceId.toString(),
                        placeholder = stringResource(R.string.chooseServices),
                        onValueChange = {
                            viewModel.onSheetServiceSelected(it?.toInt())
                        },
                        isLoading = services is FeatureState.Loading,
                    )

                    Spacer(Modifier.height(BasePadding))

                    when(val filters = serviceFilters) {
                        is FeatureState.Loading -> {
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(rememberShimmerBrush())
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                        is FeatureState.Success -> {
                            SearchAdvancedFilters(
                                selectedSubFilterIds = state.subFilterIds,
                                onSubFilterAppend = {
                                    viewModel.onSheetSubFilterSelected(it)
                                },
                                filters = filters.data
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }

        SearchServicesSheetFooter(
            onFilter = {
//                onFilter(
//                    SearchServicesFiltersSheetState(
//                        businessDomainId = state.businessDomainId,
//                        businessTypeId = state.businessTypeId,
//                        serviceId = state.serviceId,
//                        subFilterIds = state.subFilterIds
//                    )
//                )
            },
            onClear = {},
            onOpenDate = onOpenDate
        )
    }
}

fun LocalDate.toUtcMillis(): Long =
    this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberFutureOnlyDatePickerState(
    initialDate: LocalDate = LocalDate.now(),
    monthsAhead: Long = 6
): DatePickerState {
    val today = remember { LocalDate.now() }
    val maxDate = remember { today.plusMonths(monthsAhead) }

    val minMillis = remember { today.toUtcMillis() }
    val maxMillis = remember { maxDate.toUtcMillis() }

    val selectableDates = remember {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis in minMillis..maxMillis
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year in today.year..maxDate.year
            }
        }
    }

    return rememberDatePickerState(
        initialSelectedDateMillis = initialDate.toUtcMillis(),
        selectableDates = selectableDates
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchServicesDateTime(
    initialSelection: DateTimeSelection,
    onCancel: () -> Unit,
    onConfirm: (DateTimeSelection) -> Unit,
) {
    val today = remember { LocalDate.now() }
    val tomorrow = remember { today.plusDays(1) }

    var selection by remember { mutableStateOf(initialSelection) }

    fun Long.toLocalDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

    val datePickerState = rememberFutureOnlyDatePickerState()

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val newDate = millis.toLocalDate()
            if (newDate != selection.date) {
                selection = selection.copy(date = newDate)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(Modifier.weight(1f)) {
            CustomIconButton(
                boxSize = 60.dp,
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onCancel
            )

            Text(
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = "Data si ora"
            )
            Spacer(Modifier.height(SpacingXXL))

            // Calendar
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                headline = null,
                colors = DatePickerDefaults.colors(
                    containerColor = Background
                ),
                title = null
//            title = {
////                Text(
////                    text = selection.date.month.name.lowercase()
////                        .replaceFirstChar { it.titlecase(Locale.getDefault()) } +
////                            " ${selection.date.year}",
////                    style = MaterialTheme.typography.titleMedium,
////                    modifier = Modifier.padding(bottom = 8.dp)
////                )
//            }
            )

            Spacer(Modifier.height(16.dp))

            // Select time section
            Text(
                text = "Select time",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val presets = listOf(
                TimePreset.ANYTIME to "Anytime",
                TimePreset.MORNING to "Morning\n9:00 – 12:00",
                TimePreset.AFTERNOON to "Afternoon\n12:00 – 18:00",
                TimePreset.EVENING to "Evening\n18:00 – 22:00",
                TimePreset.CUSTOM to "Custom"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                presets.forEach { (preset, label) ->
                    val isSelected = selection.preset == preset
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selection = selection.copy(preset = preset)
                        },
                        label = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        modifier = Modifier.heightIn(min = 40.dp)
                    )
                }
            }

            if (selection.preset == TimePreset.CUSTOM) {
                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TimeDropdown(
                        label = "From",
                        selectedTime = selection.customStart,
                        onTimeSelected = { newStart ->
                            selection = selection.copy(customStart = newStart)
                        }
                    )
                    TimeDropdown(
                        label = "To",
                        selectedTime = selection.customEnd,
                        onTimeSelected = { newEnd ->
                            selection = selection.copy(customEnd = newEnd)
                        }
                    )
                }
            }
        }

        SearchSheetActions(
            onClear = {},
            onConfirm = {},
            isConfirmEnabled = true
        )
    }
}

@Composable
private fun TimeDropdown(
    label: String,
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit
) {
    val options = remember {
        (8..22).map { hour ->
            LocalTime.of(hour, 0)
        }
    }

    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selectedTime?.toString() ?: "",
            onValueChange = {},
            modifier = Modifier
                .weight(1f)
                .clickable { expanded = true },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { time ->
                DropdownMenuItem(
                    text = { Text(time.toString()) },
                    onClick = {
                        expanded = false
                        onTimeSelected(time)
                    }
                )
            }
        }
    }
}
