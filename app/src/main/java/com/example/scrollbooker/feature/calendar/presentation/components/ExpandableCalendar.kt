package com.example.scrollbooker.feature.calendar.presentation.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.feature.calendar.presentation.components.util.generateCalendarMonths
import com.example.scrollbooker.feature.calendar.presentation.components.util.getWeeksInMonth
import com.example.scrollbooker.feature.calendar.presentation.components.util.toYearMonth
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.headlineSmall
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.labelMedium
import com.example.scrollbooker.ui.theme.labelSmall
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun ExpandableCalendar(
    onDateSelected: (LocalDate) -> Unit,
    selectedDate: LocalDate?,
    availableDates: Set<LocalDate>
) {
    val today = remember { LocalDate.now() }
    val calendarMonths = remember { generateCalendarMonths(today) }
    val weekInitials = listOf("L", "M", "M", "J", "V", "S", "D")

    Row(modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = SpacingXL,
                start = BasePadding,
                end = BasePadding
            ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weekInitials.forEach {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    style = labelSmall,
                    modifier = Modifier.weight(1f),
                    color = Color.Gray
                )
            }
        }

    LazyColumn {
        items(calendarMonths) { month ->
            val weeksInMonth = remember { getWeeksInMonth(month.toYearMonth()) }
            val gridHeight = 48.dp * weeksInMonth

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${month.name.replaceFirstChar { it.uppercase() }} 2025",
                    style = titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(
                            bottom = 8.dp,
                            start = BasePadding
                        )
                )

                Box(modifier = Modifier
                    .padding(horizontal = BasePadding)
                    .height(320.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        userScrollEnabled = false,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(month.days.size) { index ->
                            val date = month.days[index]

                            if(date != null) {
                                val isAvailable = availableDates.contains(date)
                                val isSelected = selectedDate == date

                                Column(modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> Primary
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable { onDateSelected(date) },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = date.format(DateTimeFormatter.ofPattern("dd")),
                                        style = labelLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if(isSelected) OnPrimary else OnBackground
                                    )
                                    Spacer(Modifier.height(SpacingXXS))
                                    Box(modifier = Modifier
                                        .padding(top = 4.dp)
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if(isAvailable) Color.Green else Error)
                                    )
                                }
                            } else {
                                Spacer(Modifier.aspectRatio(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}