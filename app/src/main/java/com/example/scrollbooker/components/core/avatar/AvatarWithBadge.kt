package com.example.scrollbooker.components.core.avatar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXL
import com.example.scrollbooker.core.util.rememberScrollBookerImageLoader
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import timber.log.Timber

@Composable
fun AvatarWithBadge(
    url: String,
    modifier: Modifier = Modifier,
    size: Dp = AvatarSizeXL,
    background: Color = Background,
    contentScale: ContentScale = ContentScale.Crop,

    badgeIconPainter: Int? = null,
    badgeIconImageVector: ImageVector? = null,

    badgeTint: Color = OnPrimary,
    badgeSizeFraction: Float = 0.4f,
    badgeAlignment: Alignment = Alignment.BottomEnd,
    badgeOffset: DpOffset = DpOffset(5.dp, 5.dp),
    badgeBackground: Color = Primary,
    badgeBorder: BorderStroke? = BorderStroke(2.dp, Background),
    badgeElevation: Dp = 3.dp,
) {
    val context = LocalContext.current
    val imageLoader = rememberScrollBookerImageLoader()

    val imageRequest = remember(url) {
        url.takeIf { it.isNotBlank() }?.let {
            ImageRequest.Builder(context)
                .data(it)
                .crossfade(true)
                .build()
        }
    }

    val badgeSize = size * badgeSizeFraction

    Box(modifier = modifier.size(size)) {
        if (imageRequest != null) {
            AsyncImage(
                model = imageRequest,
                imageLoader = imageLoader,
                contentDescription = "User Avatar",
                contentScale = contentScale,
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .background(background)
                    .border(1.dp, Divider, CircleShape),
                placeholder = painterResource(R.drawable.ic_user),
                error = painterResource(R.drawable.ic_user),
                onError = { Timber.tag("Avatar Error").e("ERROR: ${it.result.throwable.message}") }
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_user),
                contentDescription = "User Avatar Placeholder",
                contentScale = contentScale,
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .background(background)
                    .border(1.dp, Divider, CircleShape),
            )
        }

        Surface(
            modifier = Modifier
                .size(badgeSize)
                .align(badgeAlignment)
                .offset(badgeOffset.x, badgeOffset.y)
                .zIndex(1f),
            shape = CircleShape,
            color = badgeBackground,
            border = badgeBorder,
            tonalElevation = badgeElevation,
            shadowElevation = badgeElevation
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when {
                    badgeIconPainter != null -> {
                        Icon(
                            painter = painterResource(badgeIconPainter),
                            contentDescription = null,
                            tint = badgeTint
                        )
                    }
                    badgeIconImageVector != null -> {
                        Icon(
                            imageVector = badgeIconImageVector,
                            contentDescription = null,
                            tint = badgeTint
                        )
                    }
                }
            }
        }
    }
}
