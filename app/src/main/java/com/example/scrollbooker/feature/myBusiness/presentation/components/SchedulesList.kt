package com.example.scrollbooker.feature.myBusiness.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.inputs.InputSelect
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun SchedulesList(schedules: List<Schedule>) {
    var selectedOption by remember { mutableStateOf("") }
    val options = listOf("Tuns", "Pensat", "Coafat")

    LazyColumn(Modifier.fillMaxSize()) {
        items(schedules) { schedule ->
            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(text = schedule.dayOfWeek, color = OnBackground)
                Spacer(Modifier.width(BasePadding))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InputSelect(
                        modifier = Modifier.weight(1f),
                        options = options,
                        selectedOption = selectedOption,
                        onOptionSelected = { selectedOption = it }
                    )
                    Spacer(Modifier.width(BasePadding))
                    InputSelect(
                        modifier = Modifier.weight(1f),
                        options = options,
                        selectedOption = selectedOption,
                        onOptionSelected = { selectedOption = it }
                    )
                }
            }
        }
    }
}