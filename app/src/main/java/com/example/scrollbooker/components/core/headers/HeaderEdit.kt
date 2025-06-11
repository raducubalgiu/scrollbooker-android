package com.example.scrollbooker.components.core.headers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun HeaderEdit(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    title: String,
    enableBack: Boolean = true,
    onAction: () -> Unit,
    actionTitle: String,
    isEnabled: Boolean = true,
    isLoading: Boolean = false
) {

    Row(
        modifier = Modifier.fillMaxWidth().then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(50.dp)
                .clickable(
                    onClick = {
                        if(onBack != null) {
                            onBack()
                        }
                    },
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            if(enableBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_prev),
                    tint = OnBackground,
                    contentDescription = null
                )
            }
        }
        Box {
            if (title.isNotEmpty()) {
                Text(
                    style = titleMedium,
                    color = OnBackground,
                    fontWeight = FontWeight.Bold,
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Box (modifier = Modifier
            .width(100.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(
                onClick = onAction,
                enabled = isEnabled && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Primary,
                    disabledContentColor = Divider,
                    disabledContainerColor = Color.Transparent
                ),
                interactionSource = remember { MutableInteractionSource() },
            ) {
                if(isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        style = titleMedium,
                        fontWeight = FontWeight.Bold,
                        text = actionTitle
                    )
                }
            }
        }
    }
}