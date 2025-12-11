package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block

import androidx.compose.runtime.Immutable
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Immutable
data class BlockSlotsSheetState(
    val message: String,
    val slotCount: Int,
    val selectedSlots: Set<LocalDateTime>,
    val selectedDay: LocalDate?,
    val isSaving: Boolean
)

sealed interface BlockSlotsAction {
    data object Dismiss: BlockSlotsAction
    data class MessageChanged(val value: String): BlockSlotsAction
    data object Confirm: BlockSlotsAction
}