package com.example.scrollbooker.feature.profile.presentation.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CounterItem(
    counter: Int,
    label: String,
    onNavigate: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier
        .clickable(
            onClick = onNavigate,
            interactionSource = interactionSource,
            indication = null
        ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = titleMedium,
            text = "${counter}",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = OnBackground
        )
        Spacer(Modifier.height(SpacingS))
        Text(
            style = bodyMedium,
            text = label,
            color = OnSurfaceBG
        )
    }
}