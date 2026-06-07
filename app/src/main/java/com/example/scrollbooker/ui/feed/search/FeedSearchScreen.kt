package com.example.scrollbooker.ui.feed.search
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.Background

@Composable
fun FeedSearchScreen(
    viewModel: FeedSearchViewModel,
    onBack: () -> Unit,
    onNavigateToUserProfile: (Int, String) -> Unit,
) {
    val currentSearch by viewModel.currentSearch.collectAsState()
    val searchState by viewModel.searchState.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .safeDrawingPadding()
    ) {
        FeedSearchHeader(
            value = currentSearch,
            modifier = Modifier.focusRequester(focusRequester),
            onValueChange = viewModel::handleSearch,
            onClick = { keyboardController?.show() },
            onBack = {
                keyboardController?.hide()
                onBack()
            },
        )

        when(searchState) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Success -> {
                val users = (searchState as FeatureState.Success).data

                LazyColumn(Modifier.weight(1f)) {
                    items(users) { user ->
                        Box(modifier = Modifier.clickable(onClick = {onNavigateToUserProfile(user.id, user.username)})) {
                            Text(user.fullName)
                        }
                    }

                    item {
                        if(users.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(BasePadding),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = stringResource(R.string.notFoundAnyResult))
                            }
                        }
                    }
                }
            }
            null -> Unit
        }
    }
}