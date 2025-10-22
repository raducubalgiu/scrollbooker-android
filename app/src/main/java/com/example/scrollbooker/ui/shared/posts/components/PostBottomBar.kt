package com.example.scrollbooker.ui.shared.posts.components

import BottomBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.model.ctaAction
import com.example.scrollbooker.entity.social.post.domain.model.ctaTitle
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
                val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = BasePadding)
                        .padding(bottom = bottomPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainButton(
                        fullWidth = false,
                        contentPadding = PaddingValues(SpacingM),
                        onClick = { currentPost?.let { p -> stableOnAction(p.ctaAction()) } },
                        title = currentPost?.let { p -> stringResource(p.ctaTitle()) } ?: "",
                    )
                }
            }
        }
    }
}