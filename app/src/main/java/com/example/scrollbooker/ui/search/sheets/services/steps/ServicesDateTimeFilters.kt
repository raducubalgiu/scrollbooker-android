package com.example.scrollbooker.ui.search.sheets.services.steps

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
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
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneOffset

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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesDateTimeFilters(
    initialSelection: DateTimeSelection,
    onCancel: () -> Unit,
    onConfirm: (DateTimeSelection) -> Unit,
) {
    var selection by remember { mutableStateOf(initialSelection) }

    val dateRangePickerState = rememberDateRange()
    val dateFormatter = rememberCapitalizedDateFormatter()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(top = SpacingXXL)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = BasePadding),
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(R.string.dateAndHour)
            )

            ServicesDateTimeDaySuggestions()

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
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(
                color = Divider,
                thickness = 0.55.dp
            )
        }

        ServicesTimeSection(
            selection = selection,
            onCancel = {},
            onPresetSelection = {
                selection = selection.copy(preset = it)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberCapitalizedDateFormatter(): DatePickerFormatter {
    val defaultFormatter = remember { DatePickerDefaults.dateFormatter() }

    return remember {
        object : DatePickerFormatter {
            override fun formatDate(
                dateMillis: Long?,
                locale: CalendarLocale,
                forContentDescription: Boolean
            ): String? {
                return defaultFormatter.formatDate(dateMillis, locale, forContentDescription)
            }

            override fun formatMonthYear(
                monthMillis: Long?,
                locale: CalendarLocale
            ): String? {
                val original = defaultFormatter.formatMonthYear(monthMillis, locale) ?: return null
                return original.replaceFirstChar { it.uppercaseChar() }
            }
        }
    }
}