package com.example.scrollbooker.components.core.stepper

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.store.util.ThemePreferenceEnum
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.ScrollBookerTheme
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.labelLarge

@Composable
fun Stepper(
    titles: List<String>,
    currentStep: Int,
    modifier: Modifier = Modifier,
    circleSize: Dp = 28.dp,
    connectorHeight: Dp = 2.dp
) {
    require(titles.isNotEmpty())
    val total = titles.size

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            titles.forEachIndexed { i, _ ->
                val isCompleted = i < currentStep
                val isActive = i == currentStep

                val nodeColor by animateColorAsState(
                    if (isCompleted || isActive) Primary
                    else SurfaceBG
                )
                val contentColor by animateColorAsState(
                    if (isCompleted) OnPrimary
                    else if (isActive) OnPrimary
                    else OnSurfaceBG
                )

                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(nodeColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = contentColor)
                    } else {
                        Text(
                            text = (i + 1).toString(),
                            style = labelLarge,
                            color = contentColor
                        )
                    }
                }

                if (i < total - 1) {
                    val connectorColor by animateColorAsState(
                        if (i < currentStep) Primary
                        else SurfaceBG
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(connectorHeight)
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(connectorColor)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StepperPreview() {
    ScrollBookerTheme(
        themePreferenceEnum = ThemePreferenceEnum.LIGHT
    ) {
        Stepper(
            titles = listOf("Produse", "Calendar", "Confirma"),
            currentStep = 0
        )
    }
}