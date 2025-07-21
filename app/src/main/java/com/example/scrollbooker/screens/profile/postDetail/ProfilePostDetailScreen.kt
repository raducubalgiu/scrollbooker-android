package com.example.scrollbooker.screens.profile.postDetail
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.modules.posts.PostsPager
import com.example.scrollbooker.ui.theme.labelLarge

@OptIn(UnstableApi::class)
@Composable
fun ProfilePostDetailScreen(
    postId: Int?,
    posts: LazyPagingItems<Post>,
    onBack: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0
    ) { posts.itemCount }

    LaunchedEffect(posts.itemSnapshotList.items, postId) {
        val index = posts.itemSnapshotList.items.indexOfFirst { it.id == postId }
        if (index >= 0) {
            pagerState.scrollToPage(index)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF121212))
    ) {
        PostsPager(
            posts = posts,
            isVisibleTab = true,
            paddingBottom = 80.dp,
        )

        Box(modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
            contentAlignment = Alignment.TopStart
        ) {
            Box(modifier = Modifier.clickable(onClick = onBack)) {
                Box(modifier = Modifier.padding(BasePadding),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        //modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null,
                        tint = Color(0xFFE0E0E0)
                    )
                }
            }
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .align(Alignment.BottomCenter)
            .background(Color(0xFF121212)),
            contentAlignment = Alignment.TopCenter
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = BasePadding,
                        vertical = SpacingM
                    )
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    ),
                onClick = {}
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        text = "Vezi sloturi libere",
                        style = labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            }
        }
    }
}