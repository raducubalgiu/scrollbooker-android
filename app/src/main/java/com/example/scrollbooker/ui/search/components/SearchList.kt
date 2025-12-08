package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.ui.search.components.card.SearchCard

@Composable
fun SearchList(
    isInitialLoading: Boolean,
    appendState: LoadState,
    businessesSheet: LazyPagingItems<BusinessSheet>,
    onNavigateToBusinessProfile: () -> Unit
) {
    val isAppending = appendState is LoadState.Loading

    Column(Modifier.fillMaxSize()) {
        if(isInitialLoading) {
            LoadingScreen()
        } else {
            Box(Modifier.fillMaxSize()) {
                if(businessesSheet.itemCount == 0) {
                    EmptyScreen(
                        message = stringResource(R.string.notFoundLocations),
                        icon = painterResource(R.drawable.ic_store_outline)
                    )
                }

                LazyColumn {
                    items(businessesSheet.itemCount) { index ->
                        businessesSheet[index]?.let { business ->
                            SearchCard(
                                business = business,
                                onNavigateToBusinessProfile = onNavigateToBusinessProfile
                            )
                        }
                    }

                    if (isAppending) {
                        item { LoadMoreSpinner() }
                    }
                }
            }
        }
    }
}