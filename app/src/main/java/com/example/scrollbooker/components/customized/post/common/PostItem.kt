package com.example.scrollbooker.components.customized.post.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.customized.post.videoItem.PostVideo
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.shared.post.domain.model.Post

@Composable
fun PostItem(
    post: Post,
    playWhenReady: Boolean,
    onOpenReviews: () -> Unit,
    onOpenComments: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        PostVideo(
            url = post.mediaFiles.first().url,
            playWhenReady = playWhenReady,
        )

        PostOverlay(
            userActions = post.userActions,
            counters = post.counters,
            onOpenReviews = onOpenReviews,
            onOpenComments = onOpenComments,
        )

        Spacer(Modifier.height(BasePadding))
    }
}