package com.example.scrollbooker.ui.profile.edit
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.AppLocaleProvider
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.YearMonth
import org.threeten.bp.format.TextStyle
import java.util.Locale

@Composable
fun EditBirthDateScreen(
    viewModel: MyProfileViewModel,
    onBack: () -> Unit
) {
    val userState by viewModel.profile.collectAsState()
    val user = (userState as? FeatureState.Success)?.data

    val state = viewModel.editState.collectAsState().value
    val selectedDay by viewModel.selectedDay.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()
    val isBirthDateValid by viewModel.isBirthDateValid.collectAsState()

    val isLoading = state is FeatureState.Loading
    val currentYear = LocalDate.now().year

    val currentLocale = AppLocaleProvider.current()

    LaunchedEffect(user?.dateOfBirth) {
        val birthDateStr = user?.dateOfBirth
        if (!birthDateStr.isNullOrBlank() && selectedDay == null && selectedMonth == null && selectedYear == null) {
            try {
                val parsedDate = LocalDate.parse(birthDateStr)

                val dayStr = parsedDate.dayOfMonth.toString().padStart(2, '0')
                val monthStr = parsedDate.monthValue.toString().padStart(2, '0')
                val yearStr = parsedDate.year.toString()

                viewModel.setSelectedDay(dayStr)
                viewModel.setSelectedMonth(monthStr)
                viewModel.setSelectedYear(yearStr)
            } catch (_: Exception) {
            }
        }
    }

    val monthOptions = remember(currentLocale) {
        (1..12).map { month ->
            val name = Month.of(month)
                .getDisplayName(TextStyle.FULL, currentLocale)
                .replaceFirstChar { it.uppercase(Locale.ROOT) }

            Option(
                value = month.toString().padStart(2, '0'),
                name = name
            )
        }
    }

    val yearOptions = remember {
        (1900..currentYear).reversed().map {
            Option(value = it.toString(), name = it.toString())
        }
    }

    if(viewModel.isSaved) {
        LaunchedEffect(state) {
            onBack()
            viewModel.isSaved = false
        }
    }

    var dayOptions by remember { mutableStateOf<List<Option>>(emptyList()) }

    fun getValidDayOptions(month: Int?, year: Int?): List<Option> {
        return try {
            if(month != null && year != null) {
                val lastDay = YearMonth.of(year, month).lengthOfMonth()
                (1..lastDay).map {
                    Option(value = it.toString().padStart(2, '0'), name = it.toString())
                }
            } else {
                (1..31).map {
                    Option(value = it.toString().padStart(2, '0'), name = it.toString())
                }
            }
        } catch (_: Exception) { // Curățat warning
            emptyList()
        }
    }

    LaunchedEffect(selectedMonth, selectedYear) {
        val month = selectedMonth?.toIntOrNull()
        val year = selectedYear?.toIntOrNull()

        val newDayOptions = getValidDayOptions(month, year)

        if (selectedDay != null && selectedDay !in newDayOptions.map { it.value }) {
            if (user?.dateOfBirth?.contains(selectedDay.toString()) == false) {
                viewModel.setSelectedDay(null)
            }
        }

        dayOptions = newDayOptions
    }

    FormLayout(
        headLine = stringResource(R.string.dateOfBirth),
        subHeadLine = stringResource(R.string.dateOfBirthLabelDescription),
        buttonTitle = stringResource(R.string.save),
        isEnabled = isBirthDateValid && !isLoading,
        isLoading = isLoading,
        onBack = onBack,
        onNext = {
            val birthDate = LocalDate.of(
                selectedYear!!.toInt(),
                selectedMonth!!.toInt(),
                selectedDay!!.toInt()
            ).toString()

            viewModel.updateBirthDate(birthDate)
        },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = SpacingXL),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(0.3f)) {
                InputSelect(
                    options = dayOptions,
                    placeholder = stringResource(R.string.day),
                    selectedOption = selectedDay ?: "",
                    onValueChange = { viewModel.setSelectedDay(it) }
                )
            }

            Spacer(Modifier.width(SpacingS))

            Column(Modifier.weight(0.35f)) {
                InputSelect(
                    options = monthOptions,
                    placeholder = stringResource(R.string.month),
                    selectedOption = selectedMonth ?: "",
                    onValueChange = { viewModel.setSelectedMonth(it) }
                )
            }

            Spacer(Modifier.width(SpacingS))

            Column(Modifier.weight(0.35f)) {
                InputSelect(
                    options = yearOptions,
                    placeholder = stringResource(R.string.year),
                    selectedOption = selectedYear ?: "",
                    onValueChange = { viewModel.setSelectedYear(it) }
                )
            }
        }
    }
}
