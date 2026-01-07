package com.example.scrollbooker.ui.shared.posts.components

import BottomBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.model.ctaAction
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum

@Composable
fun PostBottomBar(
    onAction: (PostOverlayActionEnum) -> Unit,
    showBottomBar: Boolean,
    currentPost: Post?
) {
    val latestOnAction by rememberUpdatedState(onAction)

    val stableOnAction = remember(currentPost?.id) {
        {  action: PostOverlayActionEnum -> latestOnAction(action) }
    }

    AnimatedContent(
        targetState = showBottomBar,
        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
        label = "PostBottomBar"
    ) { display ->
        key(display) {
            if(display) BottomBar()
            else {
                MainButton(
                    modifier = Modifier.padding(
                        top = SpacingS,
                        start = BasePadding,
                        end = BasePadding
                    ),
                    fullWidth = false,
                    contentPadding = PaddingValues(14.dp),
                    onClick = { currentPost?.let { p -> stableOnAction(p.ctaAction()) } },
                    title = "Rezervă instant"
                )
            }
        }
    }
}