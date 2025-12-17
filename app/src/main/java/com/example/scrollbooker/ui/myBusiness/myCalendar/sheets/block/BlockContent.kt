package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BlockContent(
    selectedReason: BlockReasonEnum,
    message: String,
    isMessageVisible: Boolean,
    isError: Boolean,
    errorMessage: String?,
    maxLength: Int,
    onReasonClick: (BlockReasonEnum) -> Unit,
    onMessageChanged: (String) -> Unit
) {
    val reasons = listOf<BlockReasonEnum>(
        BlockReasonEnum.VACATION,
        BlockReasonEnum.MEDICAL_LEAVE,
        BlockReasonEnum.DOCTOR_APPOINTMENT,
        BlockReasonEnum.LEGAL_DAY_OFF,
        BlockReasonEnum.DO_NOT_WANT_TO_SAY,
        BlockReasonEnum.OTHER
    )

    Text(
        modifier = Modifier.padding(vertical = BasePadding),
        style = titleMedium,
        text = stringResource(R.string.reason)
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 3,
        modifier = Modifier.fillMaxWidth()
    ) {
        reasons.forEach { reason ->
            FilterChip(
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Primary.copy(alpha = 0.2f),
                    selectedLabelColor = Primary,
                    labelColor = OnBackground
                ),
                border = FilterChipDefaults.filterChipBorder(
                    selected = selectedReason == reason,
                    borderColor = if(selectedReason == reason) Color.Transparent else Divider,
                    borderWidth = 1.dp,
                    enabled = true
                ),
                selected = selectedReason == reason,
                onClick = { onReasonClick(reason) },
                label = {
                    Text(text = stringResource(reason.toLabel()))
                }
            )
        }
    }

    Spacer(Modifier.height(BasePadding))

    AnimatedVisibility(visible = isMessageVisible) {
        EditInput(
            value = message,
            placeholder = stringResource(R.string.addMessage),
            onValueChange = onMessageChanged,
            singleLine = false,
            minLines = 3,
            maxLines = 3,
            isError = isError,
            errorMessage = errorMessage,
            maxLength = maxLength
        )
    }

    Spacer(Modifier.height(BasePadding))
}