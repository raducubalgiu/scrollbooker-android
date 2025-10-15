package com.example.scrollbooker.ui.shared.posts.sheets.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.shared.calendar.Calendar
import com.example.scrollbooker.ui.shared.calendar.CalendarViewModel
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun PostCalendarBody(
    targetState: PostCalendarState,
    viewModel: CalendarViewModel
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            slideInHorizontally(
                animationSpec = tween(300),
                initialOffsetX = { fullHeight -> fullHeight }
            ) togetherWith slideOutHorizontally(
                animationSpec = tween(300),
                targetOffsetX = { fullHeight -> fullHeight }
            )
        },
        label = "SheetContentTransition"
    ) { state ->
        when(state) {
            PostCalendarState.CALENDAR -> {
                Calendar(
                    viewModel = viewModel,
                    userId = 1,
                    productId = 1,
                    slotDuration = 30,
                    onSelectSlot = {}
                )
            }
            PostCalendarState.CONFIRM -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = BasePadding)
                        .background(SurfaceBG)
                ) {
                    Text("Hello World")
                }
            }
        }
    }
}