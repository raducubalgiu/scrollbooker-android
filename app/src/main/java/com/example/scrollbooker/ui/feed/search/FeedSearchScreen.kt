package com.example.scrollbooker.ui.feed.search
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.domain.model.UserSearch
import com.example.scrollbooker.navigation.LocalRootNavController
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.components.search.FeedSearchList
import com.example.scrollbooker.ui.feed.components.search.FeedRecentlySearchList
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.delay

@Composable
fun FeedSearchScreen(
    viewModel: FeedSearchViewModel,
    userSearch: FeatureState<UserSearch>,
    onBack: () -> Unit,
    onGoToSearch: () -> Unit,
    onCreateUserSearch: (String) -> Unit,
    onDeleteRecentlySearch: (Int) -> Unit
) {
    val rootNavController = LocalRootNavController.current
    val searchState by viewModel.searchState.collectAsState()

    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(200)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    fun handleNavigateToUserProfile(userId: Int) {
        keyboardController?.hide()
        rootNavController.navigate(
            "${MainRoute.UserProfile.route}/$userId"
        )
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
            value = query,
            modifier = Modifier.focusRequester(focusRequester),
            onValueChange = {
                query = it
                viewModel.handleSearch(it)
            },
            onSearch = { handleSearch(keyword = query) },
            onClearInput = {
                query = ""
                viewModel.clearSearch()
            },
            onClick = { keyboardController?.show() },
            onBack = {
                keyboardController?.hide()
                onBack()
            },
        )

        Column(Modifier.fillMaxSize()) {
            if(query.isEmpty()) {
                FeedRecentlySearchList(
                    userSearch = userSearch,
                    onDeleteRecentlySearch = onDeleteRecentlySearch,
                    onClick = { handleSearch(it) },
                    onNavigateToUserProfile = { handleNavigateToUserProfile(it) }
                )
            } else {
                FeedSearchList(
                    query = query,
                    searchState = searchState,
                    handleSearch = { handleSearch(it) },
                    onNavigateToUserProfile = { handleNavigateToUserProfile(it) }
                )
            }
        }
    }
}