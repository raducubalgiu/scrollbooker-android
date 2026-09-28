package com.example.scrollbooker.ui.search.businessProfile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMediaFile
import com.example.scrollbooker.ui.search.components.card.SearchCardCarousel
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BusinessProfileHeader(
    mediaFiles: List<BusinessMediaFile>,
    fullName: String,
    onBack: () -> Unit,
    onShare: () -> Unit,
    imageAlpha: Float,
    imageHeight: Dp,
    imageTranslationY: Float
) {
    if (imageAlpha > 0.01f) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(imageHeight)
            .graphicsLayer {
                alpha = imageAlpha
                translationY = imageTranslationY
            }
            .zIndex(2f)
        ) {
            SearchCardCarousel(
                imageHeight = imageHeight,
                radius = 0.dp,
                mediaFiles = mediaFiles
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(SpacingM)
            .zIndex(3f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Background, CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBack
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(22.5.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = OnBackground
            )
        }

        AnimatedVisibility(
            visible = imageAlpha == 0f,
            enter = fadeIn(),
            exit = fadeOut(animationSpec = tween(0)),
            modifier = Modifier.weight(1f).padding(horizontal = SpacingS)
        ) {
            Text(
                text = fullName,
                style = titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (imageAlpha > 0f) {
            Spacer(modifier = Modifier.weight(1f))
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Background, CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onShare
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(22.5.dp),
                imageVector = Icons.Default.IosShare,
                contentDescription = null,
                tint = OnBackground
            )
        }
    }
}
