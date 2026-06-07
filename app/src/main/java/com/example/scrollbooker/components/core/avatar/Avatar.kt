package com.example.scrollbooker.components.core.avatar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import com.example.scrollbooker.core.util.rememberScrollBookerImageLoader
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.SurfaceBG
import timber.log.Timber

@Composable
fun Avatar(
    url: String,
    modifier: Modifier = Modifier,
    size: Dp = AvatarSizeS,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var isImageError by remember(url) { mutableStateOf(false) }

    val imageLoader = rememberScrollBookerImageLoader()
    val imageRequest = remember(url) {
        url.takeIf { it.isNotBlank() }?.let {
            ImageRequest.Builder(context)
                .data(it)
                .build()
        }
    }

    val avatarModifier = modifier
        .size(size)
        .clip(CircleShape)
        .background(Divider)
        .border(1.dp, Divider, CircleShape)
        .then(
            if (onClick != null) {
                val interactionSource = remember { MutableInteractionSource() }
                Modifier.clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = null
                )
            } else Modifier
        )

    Box(
        modifier = avatarModifier,
        contentAlignment = Alignment.Center
    ) {
        if (imageRequest != null && !isImageError) {
            AsyncImage(
                model = imageRequest,
                imageLoader = imageLoader,
                contentDescription = "User Avatar",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                onError = {
                    isImageError = true
                    Timber.tag("Avatar Error").e("ERROR: ${it.result.throwable.message}")
                }
            )
        }

        if (imageRequest == null || isImageError) {
            AvatarPlaceholderIcon(
                modifier = Modifier.size(size * 0.55f),
                iconColor = SurfaceBG
            )
        }
    }
}

@Composable
fun AvatarPlaceholderIcon(
    modifier: Modifier = Modifier,
    iconColor: Color
) {
    val bodyPath = remember { androidx.compose.ui.graphics.Path() }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        drawCircle(
            color = iconColor,
            radius = width * 0.28f,
            center = androidx.compose.ui.geometry.Offset(width / 2, height * 0.32f)
        )

        bodyPath.reset()
        bodyPath.moveTo(width * 0.12f, height * 0.95f)
        bodyPath.cubicTo(
            width * 0.12f, height * 0.72f,
            width * 0.28f, height * 0.65f,
            width * 0.50f, height * 0.65f
        )
        bodyPath.cubicTo(
            width * 0.72f, height * 0.65f,
            width * 0.88f, height * 0.72f,
            width * 0.88f, height * 0.95f
        )

        drawPath(
            path = bodyPath,
            color = iconColor
        )
    }
}
