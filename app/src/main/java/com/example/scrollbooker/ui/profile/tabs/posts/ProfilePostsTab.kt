package com.example.scrollbooker.ui.profile.tabs.posts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.customized.PostGrid.PostGrid
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.profile.PostTabEnum
import com.example.scrollbooker.ui.profile.SelectedPostUi

@Composable
fun ProfilePostsTab(
    posts: LazyPagingItems<Post>,
    onUpdateTop: (Boolean) -> Unit,
    onNavigateToPost: (SelectedPostUi, Post) -> Unit
) {
    val refreshState = posts.loadState.refresh
    val appendState = posts.loadState.refresh

    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow {
            gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
        }.collect { onUpdateTop(it) }
    }

    when(refreshState) {
        is LoadState.Error -> ErrorScreen()
        is LoadState.Loading -> Unit
        is LoadState.NotLoading -> {
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(posts.itemCount) { index ->
                        posts[index]?.let {
                            PostGrid(
                                post = it,
                                onNavigateToPost = { id ->
                                    onNavigateToPost(
                                        SelectedPostUi(
                                            postId = id,
                                            tab = PostTabEnum.MY_POSTS,
                                            index = index
                                        ),
                                        it
                                    )
                                }
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