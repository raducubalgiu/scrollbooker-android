package com.example.scrollbooker.ui.sharedModules.posts.components.postOverlay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.social.post.domain.model.LastMinute
import com.example.scrollbooker.entity.social.post.domain.model.PostCounters
import com.example.scrollbooker.entity.social.post.domain.model.PostProduct
import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.example.scrollbooker.ui.sharedModules.posts.PostInteractionState
import java.math.BigDecimal

@Composable
fun PostOverlay(
    product: PostProduct?,
    lastMinute: LastMinute,
    counters: PostCounters,
    user: UserSocialDto,
    description: String?,
    interactionState: PostInteractionState,
    onAction: (PostOverlayActionEnum) -> Unit,
    shouldDisplayBottomBar: Boolean,
    onShowBottomBar: () -> Unit
) {
    val discount = product?.discount

    Column(modifier = Modifier
        .fillMaxSize()
        .zIndex(3f)
        .background(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
            )
        ),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = SpacingS, start = SpacingS),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(end = SpacingXL)
            ) {
                when {
                    lastMinute.isLastMinute -> {
                        PostOverlayLabel(
                            icon = R.drawable.ic_bolt_solid,
                            title = "Last Minute",
                            containerColor = Color(0xFF00BCD4)
                        )
                    }

                    discount?.let { it > BigDecimal.ZERO } == true -> {
                        PostOverlayLabel(
                            icon = R.drawable.ic_percent_badge_solid,
                            title = "Reducere"
                        )
                    }
                }

                Spacer(Modifier.height(SpacingM))

                PostOverlayUser(
                    fullName = user.fullName,
                    profession = user.profession ?: "",
                    ratingsAverage = "4.5",
                    distance = 5f,
                )

                Spacer(Modifier.height(SpacingM))

                description?.takeIf { it.isNotBlank() }?.let { description ->
                    Text(
                        text = description,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(SpacingM))
                }

                product?.let {
                    PostOverlayProduct(product = product)
                }
            }

            PostOverlayActions(
                interactionState = interactionState,
                onAction = onAction,
                commentCount = counters.commentCount,
                shareCount = counters.shareCount,
                shouldDisplayBottomBar = shouldDisplayBottomBar,
                onShowBottomBar = onShowBottomBar
            )
        }
    }
}