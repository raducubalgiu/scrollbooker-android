package com.example.scrollbooker.screens.feed.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.shared.post.domain.model.Post
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PostPager(
    posts: LazyPagingItems<Post>,
    pagerState: PagerState,
    isVisibleTab: Boolean,
    modifier: Modifier = Modifier
) {
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
                    Box(Modifier.fillMaxSize()) {
                        var videoHeightFraction by remember { mutableFloatStateOf(1f) }
                        val screenHeight = LocalConfiguration.current.screenHeightDp.dp - 90.dp
                        val videoHeight = screenHeight * videoHeightFraction

                        PostVideoItem(
                            post = post,
                            playWhenReady = pagerState.currentPage == page && isVisibleTab,
                            videoHeight = videoHeight,
                            modifier = modifier
                        )

//                        Box(modifier = Modifier
//                            .fillMaxSize(),
//                            contentAlignment = Alignment.BottomCenter
//                        ) {
//                            Row(modifier = Modifier
//                                .fillMaxWidth()
//                                .background(Color.Green.copy(alpha = 0.2f))
//                            ) {
//                                Column(modifier = Modifier
//                                    .weight(1f)
//                                    .background(Color.Yellow.copy(alpha = 0.2f))
//                                ) {
//
//                                }
//
//                                Column {
//                                    Box {
//                                        Icon(
//                                            imageVector = if(post.userActions.isLiked) Icons.Default.Favorite
//                                            else Icons.Outlined.FavoriteBorder,
//                                            contentDescription = null
//                                        )
//                                        Spacer(Modifier.height(BasePadding))
//                                        Text(
//                                            text = "12.k"
//                                        )
//                                    }
//                                }
//                            }
//                        }
                    }
                }
            }
        }
    }
}