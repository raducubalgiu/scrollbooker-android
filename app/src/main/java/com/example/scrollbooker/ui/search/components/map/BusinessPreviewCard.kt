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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.ui.search.components.card.SearchCardBusinessInfo
import com.example.scrollbooker.ui.theme.Background

@Composable
fun BusinessPreviewCard(
    modifier: Modifier = Modifier,
    selectedMarker: BusinessMarker?,
    isVisible: Boolean,
    paddingBottom: Dp,
    onCloseClick: () -> Unit = {},
    onNavigateToBusinessProfile: (Int) -> Unit,
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
            enter = fadeIn(animationSpec = tween(100)),
            exit = fadeOut(animationSpec = tween(100)),
            visible = isVisible
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { selectedMarker?.id?.let { onNavigateToBusinessProfile(it) } },
                        interactionSource = interactionSource,
                        indication = null
                    ),
                shape = ShapeDefaults.Medium,
                tonalElevation = 2.dp,
                shadowElevation = 1.dp,
            ) {
                val marker = selectedMarker

                marker?.let {
                    Column(modifier = Modifier.background(Background)) {
                        BusinessPreviewHeader(
                            mediaFiles = it.mediaFiles,
                            onCloseClick = onCloseClick
                        )

                        SearchCardBusinessInfo(
                            modifier = Modifier.padding(BasePadding),
                            fullName = it.owner.fullName,
                            ratingsAverage = it.owner.ratingsAverage,
                            ratingsCount = it.owner.ratingsCount,
                            profession = it.owner.profession,
                            address = it.address,
                            distance = "4.5km"
                        )
                    }
                }
            }
        }
    }
}