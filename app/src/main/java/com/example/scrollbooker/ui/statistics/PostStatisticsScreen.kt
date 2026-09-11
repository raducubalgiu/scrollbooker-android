package com.example.scrollbooker.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.Background

@Composable
fun PostStatisticsScreen(
    viewModel: PostStatisticsViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.analyticsState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Header(
            title = stringResource(R.string.postStatistics),
            onBack = onBack
        )

        Column(modifier = Modifier.padding(BasePadding)) {
            when (val state = uiState) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> PostStatisticsContent(summary = state.data)
                is FeatureState.Error -> ErrorScreen()
            }
        }
    }
}
