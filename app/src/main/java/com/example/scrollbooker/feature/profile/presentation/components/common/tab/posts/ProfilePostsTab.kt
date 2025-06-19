package com.example.scrollbooker.feature.profile.presentation.components.common.tab.posts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.PostGrid
import com.example.scrollbooker.core.util.EmptyScreen
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.shared.posts.domain.model.Post

@Composable
fun ProfilePostsTab(
    posts: LazyPagingItems<Post>,
    lazyListState: LazyGridState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            posts.loadState.refresh is LoadState.Loading -> LoadingScreen()
            posts.loadState.refresh is LoadState.Error -> ErrorScreen()
            posts.itemCount == 0 -> EmptyScreen(
                fillMaxSize = false,
                message = stringResource(R.string.notFoundPosts),
                icon = Icons.Outlined.Videocam
            )

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(2.dp),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(posts.itemCount) { index ->
                        val post = posts[index]
                        if(post != null) {
                            PostGrid()
                        }
                    }

                    when(posts.loadState.append) {
                        is LoadState.Loading -> item { LoadMoreSpinner() }
                        is LoadState.Error -> item { Text("Ceva nu a mers cum trebuie") }
                        is LoadState.NotLoading -> Unit
                    }
                }
            }
        }
    }
}