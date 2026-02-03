package com.example.scrollbooker.ui.shared.reviews.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.components.customized.PostGrid.PostGrid
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.core.util.rememberFlingBehavior
import com.example.scrollbooker.entity.social.post.domain.model.Post

@Composable
fun VideoReviewsTab(
    videoReviews: LazyPagingItems<Post>
) {
    val flingBehavior = rememberFlingBehavior()

    when (videoReviews.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingScreen(
                modifier = Modifier.padding(top = 50.dp),
                arrangement = Arrangement.Top
            )
        }
        is LoadState.Error -> ErrorScreen()
        is LoadState.NotLoading -> {
            if(videoReviews.itemCount == 0) {
                MessageScreen(
                    modifier = Modifier.padding(top = 50.dp),
                    arrangement = Arrangement.Top,
                    icon = painterResource(R.drawable.ic_video_outline),
                    message = stringResource(R.string.notFoundVideoReviews),
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier.fillMaxSize(),
                flingBehavior = flingBehavior
            ) {
                items(videoReviews.itemCount) { index ->
                    videoReviews[index]?.let { review ->
                        PostGrid(
                            post = review,
                            onNavigateToPost = {}
                        )
                    }
                }

                item {
                    when(videoReviews.loadState.append) {
                        is LoadState.Loading -> LoadMoreSpinner()
                        else -> Unit
                    }
                }
            }
        }
    }
}