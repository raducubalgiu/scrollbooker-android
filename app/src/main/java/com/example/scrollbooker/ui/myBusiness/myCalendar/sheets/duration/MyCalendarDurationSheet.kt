package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.duration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.myBusiness.myCalendar.durations
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
private fun intervalStepLabel(minutes: Int): String {
    val resId = if (minutes in 2..19) R.string.intervalStep else R.string.intervalStepDe
    return stringResource(resId, minutes)
}

@Composable
fun MyCalendarDurationSheet(
    selected: String,
    onAction: (DurationSheetAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.6f)
    ) {
        SheetHeader(
            modifier = Modifier.padding(horizontal = BasePadding),
            title = stringResource(R.string.interval),
            onClose = { onAction(DurationSheetAction.Close) }
        )

        Spacer(Modifier.height(BasePadding))

        LazyColumn(
            contentPadding = PaddingValues(horizontal = BasePadding)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(bottom = BasePadding),
                    text = stringResource(R.string.intervalDescription),
                    style = bodyLarge,
                    color = Color.Gray
                )
            }

            itemsIndexed(durations) { index, option ->
                InputRadio(
                    paddingHorizontal = BasePadding,
                    selected = option.value == selected,
                    headLine = option.name ?: "",
                    headLineStyle = titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    subHeadline = option.value?.toIntOrNull()?.let { intervalStepLabel(it) },
                    onSelect = {
                        option.value?.let { onAction(DurationSheetAction.Select(it)) }
                    }
                )

                if (index < durations.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = BasePadding),
                        color = Divider,
                        thickness = 0.55.dp
                    )
                }
            }
        }
    }
}
