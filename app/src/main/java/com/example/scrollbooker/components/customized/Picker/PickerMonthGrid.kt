package com.example.scrollbooker.components.customized.Picker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import java.util.Locale

@Composable
fun PickerMonthGrid(
    month: YearMonth,
    locale: Locale,
    today: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val days = remember(month, locale) {
        PickerUtils.generateMonthDays(month, locale)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(7f / 6f)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        userScrollEnabled = false
    ) {
        items(days) { date ->
            if (date == null) {
                Box(Modifier.aspectRatio(1f))
            } else {

                val isPast = date.isBefore(today)
                val isSelected = selectedDate == date
                val isToday = date == today

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(
                            when {
                                isSelected -> LastMinute
                                else -> Color.Transparent
                            }
                        )
                        .clickable(enabled = !isPast) {
                            onDateSelected(date)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = when {
                            isPast -> Divider
                            isSelected -> OnPrimary
                            else -> OnBackground
                        },
                        fontWeight = if(isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}