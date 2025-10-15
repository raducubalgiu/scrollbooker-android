package com.example.scrollbooker.components.core.avatar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import timber.log.Timber

@Composable
fun Avatar(
    url: String,
    size: Dp = AvatarSizeS,
    onClick: (() -> Unit)? = null
) {
    Box(modifier = Modifier.clickable(
        onClick = { onClick?.invoke() },
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    )) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, Color(0xFFCCCCCC), CircleShape),
            placeholder = painterResource(R.drawable.ic_user),
            error = painterResource(R.drawable.ic_user),
            contentScale = ContentScale.Crop,
            onError = { Timber.tag("Avatar Error").e("ERROR: ${it.result.throwable.message}") }
        )
    }
}