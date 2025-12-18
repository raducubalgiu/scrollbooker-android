package com.example.scrollbooker.ui.camera.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import timber.log.Timber

@Composable
fun CreatePostHeader(
    previewHeight: Dp,
    coverUri: String?,
    coverKey: String?,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .height(previewHeight)
                .aspectRatio(9f / 12f)
                .clip(shape = ShapeDefaults.Medium)
                .background(SurfaceBG)
                .clickable(onClick = {})
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(coverUri)
                    .memoryCacheKey(coverKey)
                    .diskCacheKey(coverKey)
                    .crossfade(true)
                    .build(),
                contentDescription = "Post Grid Preview",
                contentScale = ContentScale.Crop,
                onError = {
                    Timber.tag("Post Grid Preview Error").e("ERROR: ${it.result.throwable.message}")
                },
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpacingS)
                    .clip(shape = ShapeDefaults.ExtraSmall)
                    .background(Color.Black.copy(0.4f))
                    .padding(5.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Preview",
                    fontWeight = FontWeight.SemiBold,
                    color = Background,
                    style = bodyMedium
                )
            }
        }

        Spacer(Modifier.width(SpacingS))

        TextField(
            modifier = Modifier
                .weight(1f)
                .height(previewHeight),
            value = description,
            onValueChange = onDescriptionChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.addDescription),
                    color = Divider,
                    style = bodyMedium
                )
            },
            singleLine = false,
            minLines = 4,
            maxLines = 6,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = Primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Divider,
                unfocusedLabelColor = Color.Transparent,
                focusedTextColor = OnBackground,
                unfocusedTextColor = OnBackground,
                disabledContainerColor = Color.Transparent
            )
        )
    }
}