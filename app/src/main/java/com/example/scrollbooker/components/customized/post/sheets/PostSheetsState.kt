package com.example.scrollbooker.components.customized.post.sheets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class PostSheetsState internal constructor(
    val sheetState: SheetState,
    private val scope: CoroutineScope,
) {
    private val contentState = mutableStateOf<PostSheetsContent>(PostSheetsContent.None)
    val content: PostSheetsContent get() = contentState.value

    fun open(target: PostSheetsContent) {
        scope.launch {
            sheetState.show()
            contentState.value = target
        }
    }

    fun swap(target: PostSheetsContent) {
        scope.launch {
            sheetState.hide()
            contentState.value = target
        }
    }

    fun close(after: () -> Unit = {}) {
        scope.launch {
            sheetState.hide()
            contentState.value = PostSheetsContent.None
            after()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberPostSheetsState(): PostSheetsState {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    return remember(sheetState, scope) { PostSheetsState(sheetState, scope) }
}
