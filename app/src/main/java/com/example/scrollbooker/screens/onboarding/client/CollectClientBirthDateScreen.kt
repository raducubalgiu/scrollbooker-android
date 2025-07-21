package com.example.scrollbooker.screens.onboarding.client
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.bodyLarge
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.YearMonth
import org.threeten.bp.format.TextStyle
import java.util.Locale

@Composable
fun CollectClientBirthDateScreen(
    viewModel: CollectClientBirthDateViewModel,
    onNext: () -> Unit
) {
    val isSaving by viewModel.isSaving.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()

    val isEnabled by viewModel.isBirthDateValid.collectAsState()
    val isLoading = isSaving is FeatureState.Loading

    FormLayout(
        headLine = stringResource(R.string.dateOfBirth),
        subHeadLine = stringResource(R.string.dateOfBirthLabelDescription),
        buttonTitle = stringResource(R.string.nextStep),
        isEnabled = isEnabled && !isLoading,
        isLoading = isLoading,
        enableBack = false,
        onNext = onNext,
    ) {
        val currentYear = LocalDate.now().year

        val monthOptions = remember {
            (1..12).map { month ->
                val name = Month.of(month)
                    .getDisplayName(TextStyle.FULL, Locale("ro"))
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
            } catch (e: Exception) {
                emptyList()
            }
        }

        LaunchedEffect(selectedMonth, selectedYear) {
            val month = selectedMonth?.toIntOrNull()
            val year = selectedYear?.toIntOrNull()

            val newDayOptions = getValidDayOptions(month, year)

            if(selectedDay !in newDayOptions.map { it.value }) {
                viewModel.setSelectedDay(null)
            }

            dayOptions = newDayOptions
        }

        Row(
            modifier = Modifier.padding(horizontal = SpacingXL),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(0.3f)) {
                InputSelect(
                    options = dayOptions,
                    placeholder = stringResource(R.string.day),
                    selectedOption = selectedDay.toString(),
                    onValueChange = { viewModel.setSelectedDay(it) }
                )
            }
            Spacer(Modifier.width(SpacingS))

            Column(Modifier.weight(0.35f)) {
                InputSelect(
                    options = monthOptions,
                    placeholder = stringResource(R.string.month),
                    selectedOption = selectedMonth.toString(),
                    onValueChange = { viewModel.setSelectedMonth(it) }
                )
            }
            Spacer(Modifier.width(SpacingS))

            Column(Modifier.weight(0.35f)) {
                InputSelect(
                    options = yearOptions,
                    placeholder = stringResource(R.string.year),
                    selectedOption = selectedYear.toString(),
                    onValueChange = { viewModel.setSelectedYear(it) }
                )
            }
        }

        Spacer(Modifier.height(SpacingXXL))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = onNext) {
                Text(
                    text = stringResource(R.string.preferNotToSay),
                    style = bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}