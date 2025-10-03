package com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.core.util.parseTimeStringFromLocalDateTimeString
import com.example.scrollbooker.core.util.toIsoString
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

enum class BlockReason {
    VACATION,
    MEDICAL_LEAVE,
    DOCTOR_APPOINTMENT,
    LEGAL_DAY_OFF,
    OTHER
}

@StringRes
fun BlockReason.toLabel(): Int = when(this) {
    BlockReason.VACATION -> R.string.reason_vacation
    BlockReason.MEDICAL_LEAVE -> R.string.reason_medical_leave
    BlockReason.DOCTOR_APPOINTMENT -> R.string.reason_doctor_appointment
    BlockReason.LEGAL_DAY_OFF -> R.string.reason_legal_day_off
    BlockReason.OTHER -> R.string.otherReason
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockSlotsSheet(
    message: String,
    onMessageChange: (String) -> Unit,
    sheetState: SheetState,
    slotCount: Int,
    selectedDay: LocalDate?,
    selectedSlots: Set<LocalDateTime>,
    onDismiss: () -> Unit,
) {
    val reasons = listOf<BlockReason>(
        BlockReason.VACATION,
        BlockReason.MEDICAL_LEAVE,
        BlockReason.DOCTOR_APPOINTMENT,
        BlockReason.LEGAL_DAY_OFF,
        BlockReason.OTHER
    )
    var selectedReason by rememberSaveable { mutableStateOf(BlockReason.OTHER) }
    val isOtherReason = selectedReason == BlockReason.OTHER
    val maxLength = 50

    val checkNote = checkLength(LocalContext.current, message, minLength = 3, maxLength = maxLength)
    val isInputValid = checkNote.isNullOrBlank()

    val dayLabel = selectedDay?.toIsoString()
    val timeLabel = if(slotCount > 1) {
        selectedSlots.joinToString(" • ") { parseTimeStringFromLocalDateTimeString(it) }
    } else {
        selectedSlots.firstOrNull()?.let { parseTimeStringFromLocalDateTimeString(it) }
    }

    val title = if(slotCount > 1) stringResource(R.string.blockSelectedSlots)
                else stringResource(R.string.blockSlot)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Background,
        dragHandle = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(
                    horizontal = BasePadding,
                    vertical = SpacingM
                )
        ) {
            SheetHeader(
                title = "",
                onClose = onDismiss,
                padding = 0.dp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = title,
                style = titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(SpacingXS))

            Text(
                text = "$dayLabel $timeLabel",
                style = bodyLarge,
                color = Color.Gray
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
                        onClick = { selectedReason = reason },
                        label = {
                            Text(text = stringResource(reason.toLabel()))
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            AnimatedVisibility(visible = isOtherReason) {
                EditInput(
                    value = message,
                    placeholder = stringResource(R.string.addMessage),
                    onValueChange = {
                        if (it.length <= maxLength) onMessageChange(it)
                    },
                    singleLine = false,
                    minLines = 3,
                    maxLines = 3,
                    isInputValid = isInputValid,
                    errorMessage = checkNote.toString(),
                    maxLength = maxLength
                )
            }

            Spacer(Modifier.height(16.dp))

            MainButton(
                onClick = {},
                title = stringResource(R.string.block),
                enabled = if(isOtherReason) isInputValid else true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Error.copy(alpha = 0.2f),
                    contentColor = Error
                )
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}
