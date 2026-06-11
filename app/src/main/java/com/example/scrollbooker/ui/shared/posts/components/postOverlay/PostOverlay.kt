package com.example.scrollbooker.ui.shared.posts.components.postOverlay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun PostOverlay(
    post: Post,
    isSavingLike: Boolean,
    isSavingBookmark: Boolean,
    onAction: (PostOverlayActionEnum) -> Unit,

    enableOpacity: Boolean = false,
    onNavigateToUserProfile: (userId: Int, username: String) -> Unit,
    showBookButton: Boolean = true,
    onNavigateToBooking: () -> Unit
) {
    val isVideoReview = post.isVideoReview

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(3f)
    ) {
        // 1. Umbra subtilă: Ocupă doar jumătatea sau treimea inferioară a ecranului
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f) // Schimbă la 0.3f dacă vrei o umbră și mai mică (30% din ecran)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )

        // 2. Conținutul tău original (fără fundalul pe fillMaxSize)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SpacingS, start = SpacingM),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(Modifier.height(SpacingXS))

                    PostOverlayUser(
                        enableOpacity = enableOpacity,
                        user = post.user,
                        isVideoReview = isVideoReview,
                        onNavigateToUser = onNavigateToUserProfile,
                    )

                    Spacer(Modifier.height(SpacingS))

                    post.description
                        ?.takeIf { it.isNotBlank() }
                        ?.let { PostOverlayDescription(it) }
                        ?: PostOverlayDescription("...")

                    if (showBookButton) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = BasePadding),
                            onClick = { onNavigateToBooking() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary,
                                contentColor = OnPrimary
                            ),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.bookNow),
                                style = bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = OnPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                PostOverlayActions(
                    user = post.user,
                    isSavingLike = isSavingLike,
                    isSavingBookmark = isSavingBookmark,
                    isVideoReview = post.isVideoReview,
                    enableOpacity = enableOpacity,
                    counters = post.counters,
                    userActions = post.userActions,
                    onAction = onAction,
                    onNavigateToUser = { onNavigateToUserProfile(post.user.id, post.user.username) },
                )
            }
        }
    }
}