package com.example.scrollbooker.ui.profile.components.userInfo.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import androidx.core.net.toUri

@Composable
fun ProfileIntentActionsList(
    intentList: List<IntentAction>
) {
    val context = LocalContext.current

    intentList.isNotEmpty().let {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingXXL),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            intentList.forEachIndexed { index, intent ->
                ProfileIntentActionButton(
                    icon = intent.icon,
                    title = intent.title,
                    onClick = {
                        when(intent.actionType) {
                            IntentActionTypeEnum.PHONE -> {
                                // Handle phone action
                            }
                            IntentActionTypeEnum.ADDRESS -> {
                                // Handle address action
                            }
                            IntentActionTypeEnum.EMAIL -> {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = "mailto:${intent.value}".toUri()
                                }
                                context.startActivity(intent)
                            }
                            IntentActionTypeEnum.WEBSITE -> {
                                // Handle website action
                            }
                            IntentActionTypeEnum.INSTAGRAM -> {
                                // Handle Instagram action
                            }
                            IntentActionTypeEnum.YOUTUBE -> {
                                // Handle YouTube action
                            }
                            IntentActionTypeEnum.TIKTOK -> {
                                // Handle TikTok action
                            }
                        }
                    }
                )

                if(index < intentList.size - 1) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = BasePadding)
                            .width(1.dp)
                            .height(10.dp)
                            .background(Color.Gray, shape = RectangleShape)
                    )
                }
            }
        }
    }
}