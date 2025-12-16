package com.example.scrollbooker.ui.myBusiness.myCalendar

import org.threeten.bp.LocalDateTime

data class BlockUiState(
    val isBlocking: Boolean,
    val defaultBlockedLocalDates: Set<LocalDateTime>,
    val blockedLocalDates: Set<LocalDateTime>
)