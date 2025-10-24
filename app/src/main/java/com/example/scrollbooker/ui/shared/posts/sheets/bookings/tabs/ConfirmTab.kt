package com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.calendar.domain.model.Slot
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun ConfirmTab(
    selectedSlot: Slot?,
    isSaving: Boolean,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            selectedSlot?.let { slot ->
                Text(text = slot.startDateLocale)
                Text(text = slot.endDateLocale)
            }
        }

        Column {
            HorizontalDivider(color = Divider, thickness = 0.55.dp)
            MainButton(
                modifier = Modifier.padding(BasePadding),
                title = stringResource(R.string.book),
                enabled = !isSaving,
                isLoading = isSaving,
                onClick = onSave
            )
        }
    }
}