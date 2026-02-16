package com.example.scrollbooker.ui.search.sheets.services.steps
import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.customized.Picker.Picker
import com.example.scrollbooker.core.util.AppLocaleProvider
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.search.sheets.SearchSheetActions
import com.example.scrollbooker.ui.search.sheets.services.components.ServicesDateTimeDaySuggestions
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineLarge
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

@Parcelize
data class DateTimeState(
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val startDate: LocalDate? = null
): Parcelable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeStep(
    state: DateTimeState,
    onBack: () -> Unit,
    onConfirm: (DateTimeState) -> Unit,
) {
    var localState by rememberSaveable {
        mutableStateOf(state)
    }

    val today = remember { LocalDate.now() }
    val tomorrow = remember { today.plusDays(1) }

    val selectedDate = localState.startDate
    val isTodaySelected = selectedDate == today
    val isTomorrowSelected = selectedDate == tomorrow

    val isClearEnabled =
        selectedDate != null ||
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
        ) {
            Text(
                modifier = Modifier.padding(horizontal = BasePadding),
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(R.string.dateAndHour)
            )

            Spacer(Modifier.height(BasePadding))

            LazyColumn(Modifier.weight(1f)) {
                item {
                    ServicesDateTimeDaySuggestions(
                        today = today,
                        tomorrow = tomorrow,
                        onTodayClick = { localState = localState.copy(startDate = today) },
                        onTomorrowClick = { localState = localState.copy(startDate = tomorrow) },
                        isTodaySelected = isTodaySelected,
                        isTomorrowSelected = isTomorrowSelected
                    )
                }

                item {
                    Spacer(Modifier.height(BasePadding))
                }

                item {
                    Picker(
                        locale = AppLocaleProvider.current(),
                        selectedDate = localState.startDate,
                        monthsForwardLimit = 12,
                        onDateSelected = { date ->
                            localState = localState.copy(startDate = date)
                        }
                    )
                }

                item {
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

                item {
                    Spacer(Modifier.height(SpacingXXL))
                }
            }
        }

        Column {
            HorizontalDivider(
                color = Divider,
                thickness = 0.55.dp
            )

            SearchSheetActions(
                isClearEnabled = isClearEnabled,
                onClear = {
                    localState = localState.copy(
                        startDate = null,
                        startTime = null,
                        endTime = null
                    )
                },
                onConfirm = { onConfirm(localState) },
                displayIcon = false,
                primaryActionText = R.string.confirm
            )
        }
    }
}