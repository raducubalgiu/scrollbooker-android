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
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.feed.CurrentPostUi
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum

@Composable
fun PostBottomBar(
    onAction: (PostOverlayActionEnum) -> Unit,
    shouldDisplayBottomBar: Boolean,
    currentPostUi: CurrentPostUi?
) {
    val latestOnAction by rememberUpdatedState(onAction)

    val stableOnAction = remember(currentPostUi?.id) {
        {  action: PostOverlayActionEnum ->
            latestOnAction(action)
        }
    }

    AnimatedContent(
        targetState = shouldDisplayBottomBar,
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
                    currentPostUi?.let { postUi ->
                        MainButton(
                            fullWidth = false,
                            contentPadding = PaddingValues(SpacingM),
                            onClick = { stableOnAction(postUi.action) },
                            title = postUi.ctaTitle,
                        )
                    }
                }
            }
        }
    }
}