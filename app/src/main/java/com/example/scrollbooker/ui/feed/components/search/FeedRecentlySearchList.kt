package com.example.scrollbooker.ui.feed.components.search
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.domain.model.RecentlySearch

@Composable
fun FeedRecentlySearchList(
    userSearch: FeatureState<List<RecentlySearch>>,
    onDeleteRecentlySearch: (Int) -> Unit,
    onClick: (String) -> Unit
) {
    when(val userSearchState = userSearch) {
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Success -> {
            LazyColumn(modifier = Modifier.padding(top = SpacingS)) {
                item {
                    userSearchState.data.map { rec ->
                        FeedSearchRecentlyHistory(
                            recentlySearch = rec,
                            onDeleteRecentlySearch = onDeleteRecentlySearch,
                            onClick = { onClick(it) }
                        )
                    }
                }
            }
        }
    }
}