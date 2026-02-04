package com.example.scrollbooker.ui.search.sheets.services.steps
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.core.extensions.toUtcLocalDate
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.search.sheets.SearchSheetActions
import com.example.scrollbooker.ui.search.sheets.services.SearchServicesFiltersSheetState
import com.example.scrollbooker.ui.search.sheets.services.components.ServicesDateTimeDaySuggestions
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.headlineLarge
import org.threeten.bp.LocalDate
import toUtcEpochMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeStep(
    state: SearchServicesFiltersSheetState,
    onBack: () -> Unit,
    onConfirm: (SearchServicesFiltersSheetState) -> Unit,
) {
    var localState by rememberSaveable {
        mutableStateOf(state)
    }

    val verticalScroll = rememberScrollState()

    val dateRangePickerState = rememberDateRange(
        startDate = localState.startDate,
        endDate = localState.endDate
    )
    val dateFormatter = rememberCapitalizedDateFormatter()

    val today = remember { LocalDate.now() }
    val tomorrow = remember { today.plusDays(1) }

    val selectedStart = dateRangePickerState.selectedStartDateMillis?.toUtcLocalDate()
    val selectedEnd = dateRangePickerState.selectedEndDateMillis?.toUtcLocalDate()

    val isTodaySelected = selectedStart == today && selectedEnd == today
    val isTomorrowSelected = selectedStart == tomorrow && selectedEnd == tomorrow

    val isClearEnabled =
        selectedStart != null ||
        selectedEnd != null ||
        localState.startTime != null ||
        localState.endTime != null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        CustomIconButton(
            boxSize = 60.dp,
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            onClick = onBack
        )

        Column(modifier = Modifier
            .weight(1f)
            .padding(top = BasePadding)
            .verticalScroll(verticalScroll)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = BasePadding),
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(R.string.dateAndHour)
            )

            DateRangePicker(
                state = dateRangePickerState,
                title = null,
                headline = null,
                dateFormatter = dateFormatter,
                colors = DatePickerDefaults.colors(
                    containerColor = Background,
                    weekdayContentColor = Color.Gray,
                    subheadContentColor = OnBackground,
                    selectedDayContainerColor = Primary,
                    selectedDayContentColor = OnPrimary,
                    dayInSelectionRangeContainerColor = SurfaceBG,
                    dayInSelectionRangeContentColor = OnSurfaceBG
                ),
                showModeToggle = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )

            ServicesDateTimeDaySuggestions(
                today = today,
                tomorrow = tomorrow,
                onTodayClick = {
                    val millis = today.toUtcEpochMillis()
                    dateRangePickerState.setSelection(millis, millis)
                },
                onTomorrowClick = {
                    val millis = tomorrow.toUtcEpochMillis()
                    dateRangePickerState.setSelection(millis, millis)
                },
                isTodaySelected = isTodaySelected,
                isTomorrowSelected = isTomorrowSelected
            )

            TimeSection(
                startTime = localState.startTime,
                endTime = localState.endTime,
                onTimeChange = { start, end ->
                    localState = localState.copy(
                        startTime = start,
                        endTime = end
                    )
                }
            )
        }

        HorizontalDivider(
            color = Divider,
            thickness = 0.55.dp
        )

        SearchSheetActions(
            onClear = {
                dateRangePickerState.setSelection(null, null)
                localState.copy(
                    startDate = null,
                    endDate = null,
                    startTime = null,
                    endTime = null
                )
            },
            onConfirm = {
                val start = dateRangePickerState.selectedStartDateMillis?.toUtcLocalDate()
                val end = dateRangePickerState.selectedEndDateMillis?.toUtcLocalDate()

                onConfirm(
                    localState.copy(
                        startDate = start,
                        endDate = end
                    )
                )
            },
            displayIcon = false,
            primaryActionText = R.string.confirm,
            isConfirmEnabled = true,
            isClearEnabled = isClearEnabled
        )
    }
}