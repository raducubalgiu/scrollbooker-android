package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import toPrettyFullDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockSlotsSheet(
    state: BlockSlotsSheetState,
    onAction: (BlockSlotsAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current

    val context = LocalContext.current
    var previousIsSaving by rememberSaveable { mutableStateOf(false) }
    val isSaving = state.isSaving

    var message by rememberSaveable { mutableStateOf("") }
    var selectedReason by rememberSaveable { mutableStateOf(BlockReasonEnum.OTHER) }
    val isOtherReason = selectedReason == BlockReasonEnum.OTHER

    val imeInsets = WindowInsets.ime

    val imeVisible by remember {
        derivedStateOf {
            imeInsets.getBottom(density) > 0
        }
    }

    LaunchedEffect(isSaving) {
        if(previousIsSaving && !isSaving) {
            focusManager.clearFocus(force = true)
            keyboard?.hide()

            if(imeVisible) {
                withTimeoutOrNull(500) {
                    snapshotFlow { imeVisible }
                        .filter { !it }
                        .first()
                }
                delay(150)
            }

            onAction(BlockSlotsAction.Dismiss)
        }
        previousIsSaving = isSaving
    }

    val minLength = 3
    val maxLength = 50

    val checkNote = checkLength(LocalContext.current, message, minLength, maxLength)
    val isInputValid = checkNote.isNullOrBlank()

    val dayLabel = state.selectedDay?.toPrettyFullDate()
    val hasMultipleSlots = state.slotCount > 1

    val title = if(hasMultipleSlots) stringResource(R.string.blockSelectedSlots)
                else stringResource(R.string.blockSlot)

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
                BlockHeader(
                    title = title,
                    dayLabel = dayLabel ?: "",
                    selectedSlots = state.selectedSlots
                )

                BlockContent(
                    selectedReason = selectedReason,
                    message = message,
                    isMessageVisible = isOtherReason,
                    isError = !isInputValid,
                    errorMessage = checkNote,
                    maxLength = maxLength,
                    onReasonClick = { selectedReason = it },
                    onMessageChanged = { message = it }
                )
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
