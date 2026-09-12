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
    private val scope: CoroutineScope
) {
    var currentSheet: MyCalendarSheet? by mutableStateOf(null)
        private set

    fun open(sheet: MyCalendarSheet) {
        currentSheet = sheet
        scope.launch { sheetState.show() }
    }

    fun close() {
        scope.launch {
            sheetState.hide()
            currentSheet = null
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberMyCalendarSheetController(
    sheetState: SheetState
): MyCalendarSheetController {
    val scope = rememberCoroutineScope()
    return remember(sheetState, scope) {
        MyCalendarSheetController(sheetState, scope)
    }
}
