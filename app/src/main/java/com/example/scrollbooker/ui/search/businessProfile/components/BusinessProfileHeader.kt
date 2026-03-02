package com.example.scrollbooker.ui.search.businessProfile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMediaFile
import com.example.scrollbooker.ui.search.components.card.SearchCardCarousel
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BusinessProfileHeader(
    mediaFiles: List<BusinessMediaFile>,
    fullName: String,
    onBack: () -> Unit,
    imageAlpha: Float,
    imageHeight: Dp,
    imageTranslationY: Float
) {
    if(imageAlpha > 0.01f) {
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
            .padding(8.dp)
            .size(36.dp)
            .zIndex(3f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .clip(CircleShape)
                .background(Background)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }

        AnimatedVisibility(
            visible = imageAlpha == 0f,
            enter = fadeIn(),
            exit = fadeOut(animationSpec = tween(0))
        ) {
            Text(
                text = fullName,
                style = titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        IconButton(
            onClick = {},
            modifier = Modifier
                .clip(CircleShape)
                .background(Background)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_star_outline),
                contentDescription = null
            )
        }
    }
}