package com.example.scrollbooker.ui.myBusiness.mySubscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun MySubscriptionScreen(
    viewModel: MySubscriptionViewModel,
    onBack: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 1) { 3 }

    val subscriptions = listOf("Free", "Standard", "Premium")

    Layout(
        onBack = onBack,
        enablePaddingH = false
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.weight(1f)) {
                HorizontalPager(
                    state = pagerState
                ) { page ->
                    when(page) {
                        0 -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = 0.8f)
                                    .fillMaxHeight()
                                    .background(Color.Yellow)
                            ) {
                                Text("Free")
                            }
                        }
                        1 -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = 0.8f)
                                    .fillMaxHeight()
                                    .background(Color.Red)
                            ) {
                                Text("Standard")
                            }
                        }
                        2 -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = 0.8f)
                                    .fillMaxHeight()
                                    .background(Color.Blue)
                            ) {
                                Text("Premium")
                            }
                        }
                    }
                }
            }

            MainButton(
                title = "Alege",
                onClick = {}
            )
        }
    }
}