package com.example.scrollbooker.ui.shared.posts.sheets.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.sheet.SheetHeader

@Composable
fun CalendarSheet(
    userId: Int,
    onClose: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        SheetHeader(
            title = "Calendar",
            onClose = onClose
        )
    }
}