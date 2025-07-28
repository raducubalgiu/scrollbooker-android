package com.example.scrollbooker.ui.feed.components.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.data.remote.SearchTypeEnum
import com.example.scrollbooker.entity.search.domain.model.Search
import com.example.scrollbooker.ui.feed.search.FeedSearchUserItem

@Composable
fun FeedSearchList(
    query: String,
    searchState: FeatureState<List<Search>>?,
    handleSearch: (String) -> Unit,
    onNavigateToUserProfile: (Int) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        when(val search = searchState) {
            is FeatureState.Error -> {
                ErrorScreen(
                    modifier = Modifier.padding(top = 50.dp),
                    arrangement = Arrangement.Top
                )
            }
            is FeatureState.Loading -> {
                LoadingScreen(
                    modifier = Modifier.padding(top = 50.dp),
                    arrangement = Arrangement.Top
                )
            }
            is FeatureState.Success -> {
                LazyColumn(modifier = Modifier
                    .weight(1f)
                    .padding(top = SpacingS)
                ) {
                    item {
                        if(query.isNotEmpty()) {
                            FeedSearchKeyword(
                                keyword = query,
                                onClick = { handleSearch(it) }
                            )
                        }
                    }

                    itemsIndexed(search.data) { index, searchResult ->
                        when(searchResult.type) {
                            SearchTypeEnum.USER -> searchResult.user?.let {
                                FeedSearchUserItem(
                                    user = searchResult.user,
                                    onNavigateToUserProfile = onNavigateToUserProfile
                                )
                            }
                            SearchTypeEnum.SERVICE -> {
                                FeedSearchKeyword(
                                    keyword = searchResult.label,
                                    icon = R.drawable.ic_shopping_outline,
                                    onClick = { handleSearch(it) }
                                )
                            }
                            SearchTypeEnum.BUSINESS_TYPE -> {
                                FeedSearchKeyword(
                                    keyword = searchResult.label,
                                    icon = R.drawable.ic_store_solid,
                                    onClick = { handleSearch(it) }
                                )
                            }
                            SearchTypeEnum.KEYWORD -> {
                                FeedSearchKeyword(
                                    keyword = searchResult.label,
                                    onClick = { handleSearch(it) }
                                )
                            }
                            else -> Unit
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}