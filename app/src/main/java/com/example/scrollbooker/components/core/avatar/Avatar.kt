package com.example.scrollbooker.components.core.avatar
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import com.example.scrollbooker.core.util.rememberScrollBookerImageLoader
import com.example.scrollbooker.ui.theme.Divider
import timber.log.Timber

@Composable
fun Avatar(
    url: String,
    size: Dp = AvatarSizeS,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

    val imageLoader = rememberScrollBookerImageLoader()
    val imageRequest = remember(url) {
        url.takeIf { it.isNotBlank() }?.let {
            ImageRequest.Builder(context)
                .data(it)
                .build()
        }
    }

    val clickableModifier = if(onClick != null) {
        Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, Divider, CircleShape)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    } else {
        Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, Divider, CircleShape)
    }

    Box(modifier = clickableModifier) {
        if(imageRequest != null) {
            AsyncImage(
                model = imageRequest,
                imageLoader = imageLoader,
                contentDescription = "User Avatar",
                modifier = Modifier.matchParentSize(),
                placeholder = painterResource(R.drawable.ic_user),
                error = painterResource(R.drawable.ic_user),
                contentScale = ContentScale.Crop,
                onError = { Timber.tag("Avatar Error").e("ERROR: ${it.result.throwable.message}") }
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_user),
                contentDescription = "User Avatar Placeholder",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}