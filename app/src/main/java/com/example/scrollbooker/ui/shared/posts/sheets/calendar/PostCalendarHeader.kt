package com.example.scrollbooker.ui.shared.posts.sheets.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun PostCalendarHeader(targetState: PostCalendarState) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(150)) },
        label = "HeaderTransition"
    ) { target ->
        when(target) {
            PostCalendarState.CALENDAR -> {
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box {
                        Box(modifier = Modifier
                            .padding(BasePadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.Transparent
                            )
                        }
                    }

                    Text(
                        text = "Calendar",
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(modifier = Modifier.clickable { }) {
                        Box(modifier = Modifier
                            .padding(BasePadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = OnBackground
                            )
                        }
                    }
                }
            }
            PostCalendarState.CONFIRM -> {
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(modifier = Modifier.clickable { }) {
                        Box(modifier = Modifier
                            .padding(BasePadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_chevron_left_outline),
                                contentDescription = null,
                            )
                        }
                    }

                    Text(
                        text = "Confirma rezervarea",
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(modifier = Modifier.clickable {  }) {
                        Box(modifier = Modifier
                            .padding(BasePadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_chevron_left_outline),
                                contentDescription = null,
                                tint = Color.Transparent
                            )
                        }
                    }
                }
            }
        }
    }
}