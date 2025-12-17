package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Stable
class MyCalendarSheetController(
    private val sheetState: SheetState,
    private val scope: CoroutineScope,
    private val onSheetCleared: () -> Unit,
    private val onDismissEnabledChanged: (Boolean) -> Unit,
    private val onAllowHideChanged: (Boolean) -> Unit
) {
    var currentSheet: MyCalendarSheet? by mutableStateOf(null)
        private set

    private var allowHide: Boolean = false
        set(value) {
            field = value
            onAllowHideChanged(value)
        }

    fun dismissEnabled(): Boolean = currentSheet != MyCalendarSheet.OwnClient

    fun open(sheet: MyCalendarSheet) {
        if (sheet == MyCalendarSheet.OwnClient) allowHide = false
        currentSheet = sheet
        onDismissEnabledChanged(dismissEnabled())
        scope.launch { sheetState.show() }
    }

    fun close() {
        allowHide = false
        scope.launch {
            sheetState.hide()
            currentSheet = null
            onDismissEnabledChanged(true)
            onSheetCleared()
        }
    }

    fun closeOwnClient() {
        allowHide = true
        scope.launch {
            sheetState.hide()
            currentSheet = null
            allowHide = false
            onDismissEnabledChanged(true)
            onSheetCleared()
        }
    }

    fun canDismissRequest(): Boolean = dismissEnabled()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberMyCalendarSheetController(
    sheetState: SheetState,
    onSheetCleared: () -> Unit,
    onDismissEnabledChanged: (Boolean) -> Unit,
    onAllowHideChanged: (Boolean) -> Unit
): MyCalendarSheetController {
    val scope = rememberCoroutineScope()
    return remember(sheetState, scope, onSheetCleared, onDismissEnabledChanged, onAllowHideChanged) {
        MyCalendarSheetController(
            sheetState, scope, onSheetCleared, onDismissEnabledChanged, onAllowHideChanged
        )
    }
}