package com.example.scrollbooker.ui.profile
import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.media3.common.util.UnstableApi
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.shared.posts.PostActionUiState
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlay
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.OnBackground
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource


@OptIn(UnstableApi::class)
@Composable
fun MyProfilePostDetailScreen(
    viewModel: MyProfileViewModel,
    posts: LazyPagingItems<Post>,
    onBack: () -> Unit
) {
    val postId by viewModel.selectedPostId.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0) { posts.itemCount }

    LaunchedEffect(posts.itemSnapshotList.items, postId) {
        val index = posts.itemSnapshotList.items.indexOfFirst { it.id == postId }
        if (index >= 0) {
            pagerState.scrollToPage(index)
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            Header(
                modifier = Modifier.zIndex(14f),
                onBack = onBack,
                title = "Postari",
                withBackground = false
            )
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize()) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                overscrollEffect = null,
                pageSize = PageSize.Fill,
                pageSpacing = 0.dp,
                beyondViewportPageCount = 1
            ) { page ->
                posts[page]?.let { post ->
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = post.mediaFiles.first().thumbnailUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    PostOverlay(
                        post = post,
                        postActionState = PostActionUiState(),
                        onAction = {},
                        enableOpacity = false,
                        showBottomBar = false,
                        onShowBottomBar = null,
                        onNavigateToUserProfile = {}
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(horizontal = BasePadding)
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MainButton(
                    modifier = Modifier.weight(0.5f),
                    fullWidth = false,
                    contentPadding = PaddingValues(14.dp),
                    onClick = {  },
                    title = "Rezervă instant"
                )

                Spacer(Modifier.width(SpacingM))

                MainButton(
                    modifier = Modifier.weight(0.5f),
                    fullWidth = false,
                    contentPadding = PaddingValues(14.dp),
                    onClick = {  },
                    leadingIcon = R.drawable.ic_call_outline,
                    title = "Sună",
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Background,
                        contentColor = OnBackground
                    )
                )
            }
        }
    }
}