package com.example.scrollbooker.components.customized.post.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.customized.post.sheets.PostSheetActionEnum
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostCounters
import com.example.scrollbooker.entity.social.post.domain.model.PostUser
import com.example.scrollbooker.entity.social.post.domain.model.UserPostActions
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Rating

@Composable
fun PostActions(
    user: PostUser,
    isSavingLike: Boolean,
    isSavingBookmark: Boolean,
    isVideoReview: Boolean,
    counters: PostCounters,
    userActions: UserPostActions,
    onAction: (PostSheetActionEnum) -> Unit,
    onLike: () -> Unit,
    onBookmark: () -> Unit,
    onNavigateToUser: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(isVideoReview) {
            Avatar(
                url = user.avatar ?: "",
                size = 55.dp,
                onClick = onNavigateToUser
            )
        } else {
            AvatarWithRating(
                url = user.avatar ?: "",
                rating = user.ratingsAverage,
                size = 55.dp,
                onClick = onNavigateToUser
            )
        }

        Spacer(Modifier.height(SpacingS))

        PostAction(
            isEnabled = !isSavingLike,
            counter = counters.likeCount,
            icon = R.drawable.ic_heart_solid,
            tint = if (userActions.isLiked) Error
                   else Color.White,
            onClick = onLike
        )

        if(!isVideoReview) {
            PostAction(
                counter = user.ratingsCount,
                icon = R.drawable.ic_clipboard_check_solid,
                tint = Color.White,
                onClick = { onAction(PostSheetActionEnum.OPEN_REVIEWS) }
            )
        }

        PostAction(
            counter = counters.commentCount,
            icon = R.drawable.ic_comment_solid,
            tint = Color.White,
            onClick = { onAction(PostSheetActionEnum.OPEN_COMMENTS) }
        )

        PostAction(
            isEnabled = !isSavingBookmark,
            counter = counters.bookmarkCount,
            icon = R.drawable.ic_bookmark_solid,
            tint = if (userActions.isBookmarked) Rating else Color.White,
            onClick = onBookmark
        )

        PostAction(
            icon = R.drawable.ic_elipsis_horizontal,
            tint = Color.White,
            onClick = { onAction(PostSheetActionEnum.OPEN_MORE_OPTIONS) }
        )

        Spacer(Modifier.height(SpacingS))
    }
}