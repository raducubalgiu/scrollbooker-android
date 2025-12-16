package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.core.extensions.parseTimeStringFromLocalDateTimeString
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.myBusiness.myCalendar.BlockUiState
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium
import toIsoString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockSlotsSheet(
    sheetState: SheetState,
    state: BlockSlotsSheetState,
    onAction: (BlockSlotsAction) -> Unit
) {
    val context = LocalContext.current

    val reasons = listOf<BlockReasonEnum>(
        BlockReasonEnum.VACATION,
        BlockReasonEnum.MEDICAL_LEAVE,
        BlockReasonEnum.DOCTOR_APPOINTMENT,
        BlockReasonEnum.LEGAL_DAY_OFF,
        BlockReasonEnum.OTHER
    )
    var message by rememberSaveable { mutableStateOf("") }
    var selectedReason by rememberSaveable { mutableStateOf(BlockReasonEnum.OTHER) }
    val isOtherReason = selectedReason == BlockReasonEnum.OTHER

    val minLength = 3
    val maxLength = 50

    val checkNote = checkLength(LocalContext.current, message, minLength, maxLength)
    val isInputValid = checkNote.isNullOrBlank()

    val dayLabel = state.selectedDay?.toIsoString()

    val hasMultipleSlots = state.slotCount > 1

    val title = if(hasMultipleSlots) stringResource(R.string.blockSelectedSlots)
                else stringResource(R.string.blockSlot)

    ModalBottomSheet(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        onDismissRequest = { onAction(BlockSlotsAction.Dismiss) },
        sheetState = sheetState,
        containerColor = Background,
        dragHandle = {}
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = BasePadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SheetHeader(
                    title = "",
                    onClose = { onAction(BlockSlotsAction.Dismiss) },
                )

                Spacer(Modifier.height(BasePadding))

                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = title,
                        style = titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(SpacingM))

                    Text(
                        text = "$dayLabel",
                        style = bodyLarge,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(SpacingM))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.selectedSlots.forEach {
                            Row(
                                modifier = Modifier
                                    .clip(shape = ShapeDefaults.ExtraLarge)
                                    .background(SurfaceBG)
                                    .padding(vertical = 4.dp, horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier.size(17.dp),
                                    painter = painterResource(R.drawable.ic_clock_outline),
                                    contentDescription = null,
                                    tint = Color.Gray
                                )

                                Spacer(Modifier.width(4.dp))

                                Text(
                                    text = parseTimeStringFromLocalDateTimeString(it),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

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

                    Spacer(Modifier.height(BasePadding))

                    AnimatedVisibility(visible = isOtherReason) {
                        EditInput(
                            value = message,
                            placeholder = stringResource(R.string.addMessage),
                            onValueChange = { message = it },
                            singleLine = false,
                            minLines = 3,
                            maxLines = 3,
                            isError = !isInputValid,
                            errorMessage = checkNote,
                            maxLength = maxLength
                        )
                    }

                    Spacer(Modifier.height(BasePadding))
                }
            }

            Box(
                Modifier
                    .background(Background)
                    .zIndex(5f)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
            ) {
                HorizontalDivider(color=Divider, thickness = 0.55.dp)

                MainButton(
                    modifier = Modifier.padding(vertical = BasePadding),
                    onClick = {
                        val blockedMessage = if(selectedReason == BlockReasonEnum.OTHER) message
                                             else context.getString(selectedReason.toLabel())

                        onAction(BlockSlotsAction.Confirm(blockedMessage))
                    },
                    title = stringResource(R.string.block),
                    enabled = if(state.isSaving) false else if(isOtherReason) isInputValid else true,
                    isLoading = state.isSaving,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Error.copy(alpha = 0.2f),
                        contentColor = Error
                    )
                )
            }
        }
    }
}
