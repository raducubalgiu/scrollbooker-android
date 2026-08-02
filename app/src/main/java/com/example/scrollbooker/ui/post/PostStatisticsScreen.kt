package com.example.scrollbooker.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.ui.theme.Background

@Composable
fun PostStatisticsScreen(
    viewModel: PostStatisticsViewModel,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(Background)) {
        Header(
            title = "Statistici",
            onBack = onBack
        )
    }
}