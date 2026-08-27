package com.example.scrollbooker.ui.profile.tabs.bookmarks
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.postGrid.PostGrid
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.core.util.rememberFlingBehavior
import com.example.scrollbooker.entity.social.post.domain.model.Post

@Composable
fun ProfileBookmarksTab(
    posts: LazyPagingItems<Post>,
    onNavigateToPost: (postIndex: Int, userId: Int) -> Unit,
    onLoadFinished: () -> Unit
) {
    val refreshState = posts.loadState.refresh
    val appendState = posts.loadState.append

    LaunchedEffect(refreshState) {
        if (refreshState !is LoadState.Loading) {
            onLoadFinished()
        }
    }

    val flingBehavior = rememberFlingBehavior()

    when {
        refreshState is LoadState.Error && posts.itemCount == 0 -> ErrorScreen()
        refreshState is LoadState.Loading && posts.itemCount == 0 -> {
            LoadingScreen(
                modifier = Modifier.padding(top = 50.dp),
                arrangement = Arrangement.Top
            )
        }
        else -> {
            Box(Modifier.fillMaxSize()) {
                if(posts.itemCount == 0) {
                    EmptyScreen(
                        modifier = Modifier.padding(top = 50.dp),
                        arrangement = Arrangement.Top,
                        message = stringResource(R.string.notFoundPosts),
                        icon = painterResource(R.drawable.ic_video_outline)
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    modifier = Modifier.fillMaxSize(),
                    flingBehavior = flingBehavior
                ) {
                    items(posts.itemCount) { index ->
                        posts[index]?.let {
                            PostGrid(
                                post = it,
                                onNavigateToPost = { onNavigateToPost(index, it.user.id) }
                            )
                        }
                    }

                    item {
                        when(appendState) {
                            is LoadState.Loading -> LoadMoreSpinner()
                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}