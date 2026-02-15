package com.example.scrollbooker.components.customized.Picker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import org.threeten.bp.format.TextStyle
import java.util.Locale

@Composable
fun PickerWeekHeader(
    locale: Locale,
    modifier: Modifier = Modifier
) {
    val days = remember(locale) {
        PickerUtils.getWeekDays(locale)
    }

    Row(modifier = modifier.fillMaxWidth()) {
        days.forEach { day ->
            Text(
                text = day.getDisplayName(TextStyle.NARROW, locale).uppercase(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}