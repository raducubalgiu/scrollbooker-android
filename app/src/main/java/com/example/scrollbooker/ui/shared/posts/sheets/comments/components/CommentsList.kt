package com.example.scrollbooker.ui.shared.posts.sheets.comments.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.entity.social.comment.data.remote.LikeCommentEnum
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.ui.shared.posts.sheets.comments.CommentPatch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CommentsList(
    comments: LazyPagingItems<Comment>,
    newComments: List<Comment>,
    inFlight: Set<Int>,
    patches: Map<Int, CommentPatch>,
    onToggleLike: (comment: Comment, action: LikeCommentEnum) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        if(comments.itemCount == 0 && newComments.size == 0) {
            EmptyScreen(
                icon = painterResource(R.drawable.ic_comment_outline),
                message = stringResource(R.string.notFoundComments),
            )
        } else {
            LazyColumn(Modifier.fillMaxSize()) {
                items(newComments) { comment ->
                    val shown = applyPatch(comment, patches[comment.id])
                    CommentItem(
                        comment = shown,
                        likedInFlight = inFlight.contains(comment.id),
                        onLikeClick = {
                            val action = if(shown.isLiked) LikeCommentEnum.UNLIKE else LikeCommentEnum.LIKE
                            onToggleLike(comment, action)
                        }
                    )
                }

                items(comments.itemCount) { index ->
                    comments[index]?.let { comment ->
                        val shown = applyPatch(comment, patches[comment.id])
                        CommentItem(
                            comment = shown,
                            likedInFlight = inFlight.contains(comment.id),
                            onLikeClick = {
                                val action = if(shown.isLiked) LikeCommentEnum.UNLIKE else LikeCommentEnum.LIKE
                                onToggleLike(comment, action)
                            }
                        )

                        Spacer(Modifier.height(SpacingS))

                        if(comment.repliesCount > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                HorizontalDivider(Modifier.width(25.dp))
                                Spacer(Modifier.width(BasePadding))
                                Text(
                                    text = "Vezi ${comment.repliesCount} raspunsuri",
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(Modifier.height(BasePadding))
                    }
                }

                item {
                    when (comments.loadState.append) {
                        is LoadState.Loading -> LoadMoreSpinner()
                        is LoadState.Error -> "Something went wrong"
                        is LoadState.NotLoading -> Unit
                    }
                }
            }
        }
    }
}

private fun applyPatch(original: Comment, patch: CommentPatch?): Comment =
    if(patch == null) original else original.copy(
        isLiked = patch.isLiked,
        likeCount = patch.likeCount
    )