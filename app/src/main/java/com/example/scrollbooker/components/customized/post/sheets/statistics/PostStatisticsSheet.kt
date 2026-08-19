package com.example.scrollbooker.components.customized.post.sheets.statistics

import androidx.compose.foundation.layout.Column
import com.example.scrollbooker.components.core.sheet.SheetHeader
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.R

@Composable
fun PostStatisticsSheet(
    postId: Int,
    onClose: () -> Unit
) {
    val viewModel: PostStatisticsViewModel = hiltViewModel()
    val uiState by viewModel.analyticsState.collectAsStateWithLifecycle()

    LaunchedEffect(postId) {
        viewModel.setPostId(postId)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.85f)
    ) {
        SheetHeader(
            title = stringResource(R.string.postStatistics),
            onClose = onClose
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(
                    start = BasePadding,
                    end = BasePadding,
                    bottom = BasePadding
                )
        ) {
            when (val state = uiState) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> StatisticsContent(summary = state.data)
                is FeatureState.Error -> ErrorScreen()
            }
        }
    }
}
