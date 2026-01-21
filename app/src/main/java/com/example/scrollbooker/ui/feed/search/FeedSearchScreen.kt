package com.example.scrollbooker.ui.feed.search
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.domain.model.UserSearch
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.LocalMainNavController
import com.example.scrollbooker.ui.feed.components.search.FeedSearchList
import com.example.scrollbooker.ui.feed.components.search.FeedRecentlySearchList
import com.example.scrollbooker.ui.feed.components.search.FeedSearchHeader
import com.example.scrollbooker.ui.theme.Background

@Composable
fun FeedSearchScreen(
    viewModel: FeedSearchViewModel,
    userSearch: FeatureState<UserSearch>,
    onBack: () -> Unit,
    onGoToSearch: () -> Unit,
    onCreateUserSearch: (String) -> Unit,
    onDeleteRecentlySearch: (Int) -> Unit
) {
    val mainNavController = LocalMainNavController.current

    val currentSearch by viewModel.currentSearch.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    val display by viewModel.display.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.setDisplay()
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    fun handleNavigateToUserProfile(userId: Int) {
        keyboardController?.hide()
        mainNavController.navigate("${MainRoute.UserProfile.route}/$userId")
    }

    fun handleSearch(keyword: String) {
        keyboardController?.hide()
        onCreateUserSearch(keyword)
        onGoToSearch()
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
            onSearch = { handleSearch(keyword = currentSearch) },
            onClearInput = viewModel::clearSearch,
            onClick = { keyboardController?.show() },
            onBack = {
                keyboardController?.hide()
                onBack()
            },
        )

        if(display) {
            Column(Modifier.fillMaxSize()) {
                if(currentSearch.isEmpty()) {
                    FeedRecentlySearchList(
                        userSearch = userSearch,
                        onDeleteRecentlySearch = onDeleteRecentlySearch,
                        onClick = { handleSearch(it) },
                        onNavigateToUserProfile = { handleNavigateToUserProfile(it) }
                    )
                } else {
                    FeedSearchList(
                        query = currentSearch,
                        searchState = searchState,
                        handleSearch = { handleSearch(it) },
                        onNavigateToUserProfile = { handleNavigateToUserProfile(it) }
                    )
                }
            }
        }
    }
}