package com.example.scrollbooker.components.customized.MediaLibraryBottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.labelSmall
import timber.log.Timber

@Composable
fun MediaLibraryGridItem(
    item: MediaFile,
    onSelect: (MediaFile) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier
        .aspectRatio(9f / 12f)
        .background(SurfaceBG)
        .clickable(
            onClick = { onSelect(item) },
            interactionSource = interactionSource,
            indication = null
        )
    ) {
        AsyncImage(
            model = if(item.type == MediaType.VIDEO) {
                ImageRequest.Builder(LocalContext.current)
                    .data(item.uri)
                    .videoFrameMillis(500)
                    .decoderFactory { result, options, _ ->
                        VideoFrameDecoder(
                            result.source,
                            options
                        )
                    }
                    .crossfade(true)
                    .build()
            } else item.uri,
            contentDescription = "Post Grid",
            contentScale = ContentScale.Crop,
            onError = { Timber.tag("Post Grid Error").e("ERROR: ${it.result.throwable.message}") },
            modifier = Modifier.matchParentSize()
        )

        Box(modifier = Modifier
            .matchParentSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.2f),
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.4f)
                    )
                )
            )
        )

        if(item.type == MediaType.VIDEO && item.durationMs != null) {
            Text(
                text = formatDuration(item.durationMs),
                style = labelSmall,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .background(
                        color = BackgroundDark.copy(alpha = 0.3f),
                        shape = ShapeDefaults.Small
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun formatDuration(durationMs: Long): String {
    val totalSec = durationMs / 1000
    val m = totalSec / 60
    val s = totalSec % 60
    return "%d:%02d".format(m, s)
}