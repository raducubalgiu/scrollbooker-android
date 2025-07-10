package com.example.scrollbooker.screens.profile.components.common.tab.reposts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.PostGrid
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.entity.post.domain.model.Post
import com.example.scrollbooker.screens.profile.components.common.tab.posts.ProfilePostsTabViewModel
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun ProfileRepostsTab(
    userId: Int,
    onNavigate: (String) -> Unit
) {
    val viewModel: ProfileRepostsTabViewModel = hiltViewModel()

    LaunchedEffect(userId) {
        viewModel.setUserId(userId)
    }

    val posts = viewModel.userReposts.collectAsLazyPagingItems()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when(posts.loadState.refresh) {
            is LoadState.Error -> ErrorScreen()
            is LoadState.Loading -> {
                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    items(10) {
                        Box(modifier = Modifier
                            .aspectRatio(9f / 12f)
                            .border(0.5.dp, Divider)
                            .background(SurfaceBG)
                        )
                    }
                }
            }
            is LoadState.NotLoading -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(posts.itemCount) { index ->
                        val post = posts[index]
                        if(post != null) {
                            PostGrid(
                                post = post,
                                onNavigateToPost = onNavigate
                            )
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
                        message = stringResource(R.string.notFoundPosts),
                        icon = painterResource(R.drawable.ic_video_outline)
                    )
                }
            }
        }
    }
}