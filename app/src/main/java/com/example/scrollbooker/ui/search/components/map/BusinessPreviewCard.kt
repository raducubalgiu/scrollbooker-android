package com.example.scrollbooker.ui.search.components.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.ui.search.components.card.SearchCardBusinessInfo
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun BusinessPreviewCard(
    modifier: Modifier = Modifier,
    selectedMarker: BusinessMarker?,
    isVisible: Boolean,
    paddingBottom: Dp,
    onCloseClick: () -> Unit = {},
    onCardClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(Modifier.fillMaxSize().zIndex(13f)) {
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = paddingBottom,
                    start = BasePadding,
                    end = BasePadding
                ),
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300)),
            visible = isVisible
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Background)
                    .clickable(
                        onClick = onCardClick,
                        interactionSource = interactionSource,
                        indication = null
                    ),
                shape = ShapeDefaults.Medium,
                tonalElevation = 2.dp,
                shadowElevation = 1.dp,
            ) {
                val marker = selectedMarker
                if(marker != null) {
                    Column(
                        modifier = Modifier
                            .background(Background)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            IconButton(
                                onClick = onCloseClick,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(32.dp)
                                    .padding(BasePadding)
                                    .zIndex(14f),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color.Red,
                                    contentColor = OnBackground
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close"
                                )
                            }
                            AsyncImage(
                                model = "https://media.scrollbooker.ro/business-video-1-cover.jpeg",
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        SearchCardBusinessInfo(
                            modifier = Modifier.padding(BasePadding),
                            fullName = marker.owner.fullName,
                            ratingsAverage = marker.owner.ratingsAverage,
                            ratingsCount = marker.owner.ratingsCount,
                            profession = marker.owner.profession,
                            address = marker.address,
                            distance = "4.5km"
                        )
                    }
                }
            }
        }
    }
}