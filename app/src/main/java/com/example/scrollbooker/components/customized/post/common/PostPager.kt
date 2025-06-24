package com.example.scrollbooker.components.customized.post.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.BottomSheet
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.shared.post.domain.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PostPager(
    posts: LazyPagingItems<Post>,
    pagerState: PagerState,
    isVisibleTab: Boolean,
    modifier: Modifier = Modifier
) {
    var showReviewsSheet by remember { mutableStateOf(false) }
    var showCommentsSheet by remember { mutableStateOf(false) }

    BottomSheet(
        onDismiss = { showReviewsSheet = false },
        showBottomSheet = showReviewsSheet,
        showHeader = true,
        enableCloseButton = true,
        headerTitle = stringResource(R.string.reviews)
    ) {
        Box(Modifier.height(500.dp))
    }

    BottomSheet(
        onDismiss = { showCommentsSheet = false },
        showBottomSheet = showCommentsSheet,
        showHeader = true,
        enableCloseButton = true,
        headerTitle = stringResource(R.string.comments)
    ) {
        Box(Modifier.height(500.dp))
    }

    VerticalPager(
        state = pagerState,
        beyondViewportPageCount = 1,
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 90.dp)
    ) { page ->
        when(posts.loadState.append) {
            is LoadState.Error -> "Something went wrong"
            is LoadState.Loading -> LoadMoreSpinner()
            is LoadState.NotLoading -> {
                val post = posts[page]

                if(post != null) {
                    PostItem(
                        post = post,
                        playWhenReady = pagerState.currentPage == page && isVisibleTab,
                        onOpenReviews = { showReviewsSheet = true },
                        onOpenComments = { showCommentsSheet = true }
                    )
                }
            }
        }
    }
}