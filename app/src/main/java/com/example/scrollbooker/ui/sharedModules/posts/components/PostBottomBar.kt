package com.example.scrollbooker.ui.sharedModules.posts.components

import BottomBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.routes.MainRoute

@Composable
fun PostBottomBar(
    actionButtonTitle: String,
    onAction: () -> Unit,
    shouldDisplayBottomBar: Boolean,
    appointmentsNumber: Int,
    onChangeTab: (MainTab) -> Unit
) {
    AnimatedContent(
        targetState = shouldDisplayBottomBar,
        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
        label = "PostBottomBar"
    ) { display ->
        if(display) {
            BottomBar(
                appointmentsNumber = appointmentsNumber,
                currentTab = MainTab.Feed,
                currentRoute = MainRoute.Feed.route,
                onNavigate = onChangeTab
            )
        } else {
            val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

            MainButton(
                modifier = Modifier
                    .padding(horizontal = BasePadding)
                    .padding(bottom = bottomPadding),
                contentPadding = PaddingValues(SpacingM),
                leadingIcon = R.drawable.ic_calendar_outline,
                onClick = onAction,
                title = actionButtonTitle
            )
        }
    }
}