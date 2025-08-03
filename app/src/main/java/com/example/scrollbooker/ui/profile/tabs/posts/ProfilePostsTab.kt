package com.example.scrollbooker.ui.profile.tabs.posts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.customized.PostGrid
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.social.post.domain.model.Post

@Composable
fun ProfilePostsTab(
    isOwnProfile: Boolean,
    posts: LazyPagingItems<Post>,
    onNavigateToPostDetail: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when(posts.loadState.refresh) {
            is LoadState.Error -> ErrorScreen()
            is LoadState.Loading -> Unit
            is LoadState.NotLoading -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(posts.itemCount) { index ->
                        val post = posts[index]
                        if(post != null) {
                            PostGrid(post = post, onNavigateToPost = onNavigateToPostDetail)
                        }
                    }
                }

                when(posts.loadState.append) {
                    is LoadState.Loading -> LoadMoreSpinner()
                    is LoadState.Error -> { Text("Ceva nu a mers cum trebuie") }
                    is LoadState.NotLoading -> Unit
                }

                if(posts.itemCount == 0) {
                    EmptyScreen(
                        modifier = Modifier.padding(top = 50.dp),
                        arrangement = Arrangement.Top,
                        message = if(isOwnProfile) "Inca nu ai postat nici un review video" else stringResource(R.string.notFoundPosts),
                        icon = painterResource(R.drawable.ic_video_outline)
                    )
                }
            }
        }
    }
}