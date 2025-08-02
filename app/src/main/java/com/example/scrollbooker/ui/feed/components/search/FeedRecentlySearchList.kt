package com.example.scrollbooker.ui.feed.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.domain.model.RecentlySearch
import com.example.scrollbooker.entity.search.domain.model.UserSearch
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun FeedRecentlySearchList(
    userSearch: FeatureState<UserSearch>,
    onDeleteRecentlySearch: (Int) -> Unit,
    onClick: (String) -> Unit,
    onNavigateToUserProfile: (Int) -> Unit
) {
    when(val userSearchState = userSearch) {
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Success -> {
            var firstRecently = userSearchState.data.recentlySearch.take(3)
            val allRecently = userSearchState.data.recentlySearch
            var recently by rememberSaveable { mutableStateOf<List<RecentlySearch>>(firstRecently) }
            val isExpanded = recently.size > 3

            LazyColumn(modifier = Modifier.padding(top = SpacingS)) {
                item {
                    recently.map { rec ->
                        FeedSearchRecentlyHistory(
                            recentlySearch = rec,
                            onDeleteRecentlySearch = onDeleteRecentlySearch,
                            onClick = { onClick(it) }
                        )
                    }

                    if(allRecently.size > 3) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(BasePadding)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    recently =
                                        if (isExpanded) firstRecently else allRecently
                                }
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row {
                                Text(
                                    text = if(isExpanded) stringResource(R.string.collapse)
                                    else stringResource(R.string.seeMore),
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Gray,
                                    style = bodyLarge
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        }
                    }

                    Text(
                        modifier = Modifier.padding(BasePadding),
                        text = stringResource(R.string.nearServices),
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }

                items(userSearchState.data.recommendedBusiness) { recommendedBusiness ->
                    FeedSearchRecommendedBusiness(
                        recommendedBusiness = recommendedBusiness,
                        onNavigateToUserProfile = { onNavigateToUserProfile(it) }
                    )
                }
            }
        }
    }
}