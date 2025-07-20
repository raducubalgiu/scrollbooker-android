package com.example.scrollbooker.screens.feed.search
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FeedSearchScreen(
    viewModel: FeedSearchViewModel,
    onBack: () -> Unit,
    onGoToSearch: () -> Unit
) {
    val search by viewModel.currentSearch.collectAsState()

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(200)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    DisposableEffect(Unit) {
        onDispose {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
    ) {
        FeedSearchHeader(
            value = search,
            onValueChange = { viewModel.updateSearch(it) },
            onSearch = {
                keyboardController?.hide()
                onGoToSearch()
            },
            onBack = {
                coroutineScope.launch {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    delay(150)
                    onBack()
                }
            },
            onClearInput = { viewModel.clearSearch() },
            modifier = Modifier
                .focusRequester(focusRequester),
        )
    }
}