package com.example.scrollbooker.ui.profile.tabs.reposts
import androidx.compose.runtime.Composable
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.profile.components.SelectedPostUi

@Composable
fun ProfileRepostsTab(
    viewModel: MyProfileViewModel,
    onNavigateToPost: (SelectedPostUi, Post) -> Unit,
    onUpdateTop: (Boolean) -> Unit
) {
//    val posts = viewModel.userReposts.collectAsLazyPagingItems()
//
//    val refreshState = posts.loadState.refresh
//    val appendState = posts.loadState.refresh
//
//    val gridState = rememberLazyGridState()
//
//    LaunchedEffect(gridState) {
//        snapshotFlow {
//            gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
//        }.collect { onUpdateTop(it) }
//    }
//
//    when(refreshState) {
//        is LoadState.Error -> ErrorScreen()
//        is LoadState.Loading -> Unit
//        is LoadState.NotLoading -> {
//            Box(Modifier.fillMaxSize()) {
//                if(posts.itemCount == 0) {
//                    EmptyScreen(
//                        modifier = Modifier.padding(top = 50.dp),
//                        arrangement = Arrangement.Top,
//                        message = stringResource(R.string.notFoundPosts),
//                        icon = painterResource(R.drawable.ic_video_outline)
//                    )
//                }
//
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(3),
//                    verticalArrangement = Arrangement.spacedBy(1.dp),
//                    horizontalArrangement = Arrangement.spacedBy(1.dp),
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    items(posts.itemCount) { index ->
//                        posts[index]?.let {
//                            PostGrid(
//                                post = it,
//                                onNavigateToPost = { id ->
//                                    onNavigateToPost(
//                                        SelectedPostUi(
//                                            postId = id,
//                                            tab = PostTabEnum.REPOSTS,
//                                            index = index
//                                        ),
//                                        it
//                                    )
//                                }
//                            )
//                        }
//                    }
//
//                    item {
//                        when(appendState) {
//                            is LoadState.Loading -> LoadMoreSpinner()
//                            else -> Unit
//                        }
//                    }
//                }
//            }
//        }
//    }
}