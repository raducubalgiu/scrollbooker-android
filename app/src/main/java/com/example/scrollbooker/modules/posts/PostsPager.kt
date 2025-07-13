package com.example.scrollbooker.modules.posts

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.modules.reviews.ReviewsViewModel
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.post.domain.model.Post
import com.example.scrollbooker.modules.calendar.Calendar
import com.example.scrollbooker.modules.calendar.CalendarViewModel
import com.example.scrollbooker.modules.posts.comments.CommentsSheet
import com.example.scrollbooker.modules.posts.comments.CommentsViewModel
import com.example.scrollbooker.modules.posts.common.PostItem
import com.example.scrollbooker.modules.posts.common.PostSheetsContent
import com.example.scrollbooker.modules.posts.reviews.ReviewsListSheet
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PostsPager(
    posts: LazyPagingItems<Post>,
    pagerState: PagerState,
    isVisibleTab: Boolean,
    paddingBottom: Dp = 90.dp,
    modifier: Modifier = Modifier
) {
    val pagerViewModel: PostsPagerViewModel = hiltViewModel()
    val commentsViewModel: CommentsViewModel = hiltViewModel()
    val reviewsViewModel: ReviewsViewModel = hiltViewModel()
    val calendarViewModel: CalendarViewModel = hiltViewModel()

    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    var sheetContent by remember { mutableStateOf<PostSheetsContent>(PostSheetsContent.None) }

    fun handleClose() {
        sheetContent = PostSheetsContent.None
        coroutineScope.launch { sheetState.hide() }
    }

    if(sheetState.isVisible) {
        Sheet(
            onClose = { sheetContent = PostSheetsContent.None },
            sheetState = sheetState,
            modifier = Modifier.statusBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxHeight(1f)) {
                when (val content = sheetContent) {
                    is PostSheetsContent.ReviewsSheet -> {
                        reviewsViewModel.setUserId(userId = content.userId)

                        ReviewsListSheet(
                            viewModel = reviewsViewModel,
                            onClose = { handleClose() },
                            onRatingClick = {
                                reviewsViewModel.toggleRatings(it)
                            }
                        )
                    }
                    is PostSheetsContent.CommentsSheet -> {
                        commentsViewModel.setPostId(newPostId = content.postId)

                        CommentsSheet(
                            viewModel = commentsViewModel,
                            postId = content.postId,
                            isSheetVisible = sheetState.isVisible,
                            onClose = { handleClose() }
                        )
                    }
//                    is PostSheetsContent.CalendarSheet -> {
//                        val config by calendarViewModel.calendarConfig.collectAsState()
//                        val calendarDays by calendarViewModel.calendarDays.collectAsState()
//                        val availableDays by calendarViewModel.availableDays.collectAsState()
//                        val availableDayTimeslots by calendarViewModel.availableDay.collectAsState()
//                        val selectedSlot by calendarViewModel.selectedSlot.collectAsState()
//
//                        val targetState = if(selectedSlot == null) "calendar" else "confirm"
//
//                        LaunchedEffect(Unit) {
//                            calendarViewModel.setCalendarConfig(userId = content.userId)
//                        }
//
//                        LaunchedEffect(config?.selectedDay) {
//                            config?.let {
//                                calendarViewModel.loadUserAvailableTimeslots(
//                                    userId = it.userId,
//                                    day = it.selectedDay,
//                                    slotDuration = 30
//                                )
//                            }
//                        }
//
//                        AnimatedContent(
//                            targetState = targetState,
//                            transitionSpec = {
//                                fadeIn(tween(150)) togetherWith fadeOut(tween(150))
//                            },
//                            label = "HeaderTransition"
//                        ) { target ->
//                            when(target) {
//                                "calendar" -> {
//                                    Row(modifier = Modifier
//                                        .fillMaxWidth(),
//                                        verticalAlignment = Alignment.CenterVertically,
//                                        horizontalArrangement = Arrangement.SpaceBetween
//                                    ) {
//                                        Box {
//                                            Box(modifier = Modifier
//                                                .padding(BasePadding),
//                                                contentAlignment = Alignment.Center
//                                            ) {
//                                                Icon(
//                                                    imageVector = Icons.Default.Close,
//                                                    contentDescription = null,
//                                                    tint = Color.Transparent
//                                                )
//                                            }
//                                        }
//
//                                        Text(
//                                            text = "Calendar",
//                                            style = titleMedium,
//                                            fontWeight = FontWeight.SemiBold
//                                        )
//
//                                        Box(modifier = Modifier.clickable { handleClose() }) {
//                                            Box(modifier = Modifier
//                                                .padding(BasePadding),
//                                                contentAlignment = Alignment.Center
//                                            ) {
//                                                Icon(
//                                                    imageVector = Icons.Default.Close,
//                                                    contentDescription = null,
//                                                    tint = OnBackground
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                                "confirm" -> {
//                                    Row(modifier = Modifier.fillMaxWidth(),
//                                        verticalAlignment = Alignment.CenterVertically,
//                                        horizontalArrangement = Arrangement.SpaceBetween
//                                    ) {
//                                        Box(modifier = Modifier.clickable { calendarViewModel.toggleSlot(null) }) {
//                                            Box(modifier = Modifier
//                                                .padding(BasePadding),
//                                                contentAlignment = Alignment.Center
//                                            ) {
//                                                Icon(
//                                                    painter = painterResource(R.drawable.ic_arrow_chevron_left_outline),
//                                                    contentDescription = null,
//                                                )
//                                            }
//                                        }
//
//                                        Text(
//                                            text = "Confirma rezervarea",
//                                            style = titleMedium,
//                                            fontWeight = FontWeight.SemiBold
//                                        )
//
//                                        Box(modifier = Modifier.clickable {  }) {
//                                            Box(modifier = Modifier
//                                                .padding(BasePadding),
//                                                contentAlignment = Alignment.Center
//                                            ) {
//                                                Icon(
//                                                    painter = painterResource(R.drawable.ic_arrow_chevron_left_outline),
//                                                    contentDescription = null,
//                                                    tint = Color.Transparent
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }

//                        AnimatedContent(
//                            targetState = targetState,
//                            transitionSpec = {
//                                slideInHorizontally(
//                                    animationSpec = tween(300),
//                                    initialOffsetX = { fullHeight -> fullHeight }
//                                ) togetherWith slideOutHorizontally(
//                                    animationSpec = tween(300),
//                                    targetOffsetX = { fullHeight -> fullHeight }
//                                )
//                            },
//                            label = "SheetContentTransition"
//                        ) { state ->
//                            when(state) {
//                                "calendar" -> {
//                                    Calendar(
//                                        availableDayTimeslots = availableDayTimeslots,
//                                        calendarDays = calendarDays,
//                                        availableDays = availableDays,
//                                        config = config,
//                                        onDayChange = { calendarViewModel.updateSelectedDay(it) },
//                                        onSelectSlot = {
//                                            calendarViewModel.toggleSlot(it)
//                                        }
//                                    )
//                                }
//                                "confirm" -> {
//                                    Column(
//                                        modifier = Modifier
//                                            .fillMaxSize()
//                                            .padding(horizontal = BasePadding)
//                                            .background(SurfaceBG)
//                                    ) {
//                                        Text("Hello World")
//                                    }
//                                }
//                            }
//                        }
//                    }
                    else -> Unit
                    //is PostSheetsContent.None -> Unit
                }
            }
        }
    }

    fun handleOpenSheet(targetSheet: PostSheetsContent) {
        sheetContent = targetSheet
        coroutineScope.launch {
            sheetState.show()
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
                        viewModel = pagerViewModel,
                        post = post,
                        playWhenReady = pagerState.currentPage == page && isVisibleTab,
                        onOpenReviews = { handleOpenSheet(PostSheetsContent.ReviewsSheet(post.user.id)) },
                        onOpenComments = { handleOpenSheet(PostSheetsContent.CommentsSheet(post.id)) },
                        onOpenCalendar = { handleOpenSheet(PostSheetsContent.CalendarSheet(post.user.id)) }
                    )
                }
            }
        }
    }
}