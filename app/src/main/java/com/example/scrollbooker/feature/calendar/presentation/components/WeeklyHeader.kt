package com.example.scrollbooker.feature.calendar.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.labelLarge
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun WeeklyHeader(
    isCalendarExpanded: Boolean,
    weekDates: List<LocalDate>,
    availableDays: Set<LocalDate>,
    selectedIndex: Int,
    onDayClick: (Int) -> Unit,
    onToggleCalendar: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if(isCalendarExpanded) 180f else 0f,
        label = "ArrowRotation"
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = BasePadding)
    ) {
        TabRow(selectedTabIndex = selectedIndex) {
            weekDates.forEachIndexed { index, date ->
                val isAvailable = date in availableDays

                Tab(
                    selected = selectedIndex == index,
                    onClick = { onDayClick(index) },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = date.format(DateTimeFormatter.ofPattern("dd")),
                                style = labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnBackground
                            )
                            Box(modifier = Modifier
                                .padding(top = 4.dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if(isAvailable) Color.Green else Error)
                            )
                        }
                    }
                )
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggleCalendar)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            }
        }
    }
}