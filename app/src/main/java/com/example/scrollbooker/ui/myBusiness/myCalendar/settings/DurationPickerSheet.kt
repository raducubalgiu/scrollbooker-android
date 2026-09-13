package com.example.scrollbooker.ui.myBusiness.myCalendar.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun DurationPickerSheet(
    title: String,
    options: List<DurationOption>,
    selectedMinutes: Int,
    isSaving: Boolean,
    onSave: (Int) -> Unit,
    onClose: () -> Unit
) {
    var localSelection by remember(selectedMinutes) { mutableStateOf(selectedMinutes) }
    var pendingSave by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(selectedMinutes) {
        if (pendingSave != null && selectedMinutes == pendingSave) {
            onClose()
        }
    }

    val isConfirmEnabled = localSelection != selectedMinutes && !isSaving

    Column(Modifier.navigationBarsPadding()) {
        SheetHeader(title = title, onClose = onClose)

        options.forEachIndexed { index, option ->
            InputRadio(
                selected = option.minutes == localSelection,
                onSelect = { localSelection = option.minutes },
                headLine = stringResource(option.labelRes)
            )

            if (index < options.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = SpacingXXL),
                    color = Divider,
                    thickness = 0.55.dp
                )
            }
        }

        HorizontalDivider(color = Divider, thickness = 0.55.dp)

        MainButton(
            modifier = Modifier.padding(BasePadding),
            title = stringResource(R.string.save),
            enabled = isConfirmEnabled,
            isLoading = isSaving,
            onClick = {
                pendingSave = localSelection
                onSave(localSelection)
            }
        )
    }
}
