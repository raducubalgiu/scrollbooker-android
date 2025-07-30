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
import com.example.scrollbooker.entity.social.post.domain.model.PostCounters
import com.example.scrollbooker.entity.social.post.domain.model.PostProduct
import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.example.scrollbooker.ui.sharedModules.posts.PostInteractionState
import java.math.BigDecimal

@Composable
fun PostOverlay(
    product: PostProduct,
    counters: PostCounters,
    user: UserSocialDto,
    description: String?,
    interactionState: PostInteractionState,
    onAction: (PostOverlayActionEnum) -> Unit,
) {
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
            Column(modifier = Modifier.weight(1f)) {
                if(product.discount > BigDecimal.ZERO) {
                    PostOverlayLabel(
                        icon = R.drawable.ic_percent_badge_solid,
                        title = "Reducere"
                    )
                }

                Spacer(Modifier.height(SpacingM))

                PostOverlayUser(
                    fullName = user.fullName,
                    profession = user.profession ?: "",
                    ratingsAverage = "4.5",
                    distance = 5f,
                )

//                description?.takeIf { it.isNotBlank() }?.let { description ->
//                    Spacer(Modifier.height(SpacingM))
//                    Text(
//                        text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's ",
//                        color = Color.White,
//                        maxLines = 2,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                    Spacer(Modifier.height(SpacingM))
//                }

                Spacer(Modifier.height(SpacingM))
                Text(
                    text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's ",
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(SpacingM))

//                PostOverlayProduct(product = product)
//
//                PostOverlayButton(
//                    onClick = { onAction(PostOverlayActionEnum.OPEN_CALENDAR) },
//                    title = "Intervale disponibile"
//                )
            }

            PostOverlayActions(
                interactionState = interactionState,
                onAction = onAction,
                commentCount = counters.commentCount,
                shareCount = counters.shareCount
            )
        }

        Spacer(Modifier.height(SpacingS))

        PostOverlayMoreProducts(
            fullName = user.fullName,
            onClick = {}
        )
    }
}