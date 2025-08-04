package com.example.scrollbooker.ui.sharedModules.posts.components

import BottomBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.PostActionButtonUIModel
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun PostBottomBar(
    uiModel: PostActionButtonUIModel?,
    onAction: () -> Unit,
    shouldDisplayBottomBar: Boolean,
    appointmentsNumber: Int,
    onChangeTab: (MainTab) -> Unit
) {
    val currentOnAction by rememberUpdatedState(onAction)
    val currentOnChangeTab by rememberUpdatedState(onChangeTab)

    val animatedContainerColor by animateColorAsState(
        targetValue = uiModel?.containerColor ?: Primary,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "Button Color Animation"
    )

    AnimatedContent(
        targetState = shouldDisplayBottomBar,
        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
        label = "PostBottomBar"
    ) { display ->
        key(display) {
            if(display) {
                BottomBar(
                    appointmentsNumber = appointmentsNumber,
                    currentTab = MainTab.Feed,
                    currentRoute = MainRoute.Feed.route,
                    onNavigate = currentOnChangeTab
                )
            } else {
                val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = BasePadding)
                        .padding(bottom = bottomPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    uiModel?.let {
                        MainButton(
                            fullWidth = false,
                            contentPadding = PaddingValues(SpacingM),
                            leadingIcon = uiModel.icon,
                            onClick = currentOnAction,
                            title = uiModel.title,
                            colors = ButtonColors(
                                containerColor = animatedContainerColor,
                                contentColor = uiModel.contentColor,
                                disabledContainerColor = uiModel.containerColor,
                                disabledContentColor = uiModel.contentColor
                            )
                        )
                    }
                }
            }
        }
    }
}