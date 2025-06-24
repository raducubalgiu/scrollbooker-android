package com.example.scrollbooker.components.customized.post.common

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.components.customized.post.comments.CommentsSheet
import com.example.scrollbooker.components.customized.post.reviews.ReviewsListSheet
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.shared.post.domain.model.Post
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.SurfaceBG
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PostPager(
    posts: LazyPagingItems<Post>,
    pagerState: PagerState,
    isVisibleTab: Boolean,
    paddingBottom: Dp = 90.dp,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetContent by remember { mutableStateOf<PostSheetsContent>(PostSheetsContent.None) }
    val isDarkTheme = isSystemInDarkTheme()

    val title = when(sheetContent) {
        is PostSheetsContent.CalendarSheet -> stringResource(R.string.calendar)
        is PostSheetsContent.ReviewsSheet -> stringResource(R.string.reviews)
        is PostSheetsContent.CommentsSheet -> stringResource(R.string.comments)
        is PostSheetsContent.None -> ""
    }

    if(sheetContent != PostSheetsContent.None) {
        ModalBottomSheet(
            dragHandle = null,
            onDismissRequest = { sheetContent = PostSheetsContent.None },
            containerColor = if(isDarkTheme) SurfaceBG else Background,
            sheetState = sheetState,
            modifier = Modifier.fillMaxWidth().then(modifier)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
            ) {
                SheetHeader(
                    title = title,
                    onClose = {
                        sheetContent = PostSheetsContent.None
                        coroutineScope.launch { sheetState.hide() }
                    }
                )

                when (val content = sheetContent) {
                    is PostSheetsContent.ReviewsSheet -> ReviewsListSheet(content.postId)
                    is PostSheetsContent.CommentsSheet -> CommentsSheet(content.postId)
                    is PostSheetsContent.CalendarSheet -> Unit
                    PostSheetsContent.None -> Unit
                }
            }
        }
    }

    VerticalPager(
        state = pagerState,
        beyondViewportPageCount = 1,
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = paddingBottom)
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
                        onOpenReviews = {
                            sheetContent = PostSheetsContent.ReviewsSheet(post.id)
                            coroutineScope.launch {
                                sheetState.show()
                            }
                        },
                        onOpenComments = {
                            sheetContent = PostSheetsContent.CommentsSheet(post.id)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        }
                    )
                }
            }
        }
    }
}