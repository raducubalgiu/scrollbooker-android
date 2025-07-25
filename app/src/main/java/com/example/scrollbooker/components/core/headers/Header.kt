package com.example.scrollbooker.components.core.headers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun Header(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    title: String = "",
    enableBack: Boolean = true,
    customTitle: (@Composable () -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clickable(
                    onClick = {
                        if(onBack != null) {
                            onBack()
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            if(enableBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
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
                    text = title
                )
            }
            if(customTitle != null) {
                customTitle()
            }
        }
        Box(Modifier.size(50.dp)) {
            actions
        }
    }
}