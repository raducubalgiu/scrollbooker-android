package com.example.scrollbooker.ui.shared.posts.sheets.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun BookingStepper(
    totalSteps: Int,
    currentStep: Int,
    onChangeStep: (Int) -> Unit
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSteps) { index ->
            val isActive = index == currentStep
            val isEnabled = index < currentStep

            IconButton(
                modifier = Modifier.size(30.dp),
                onClick = { if(isEnabled) onChangeStep(index) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = when {
                        isActive -> Primary
                        isEnabled -> Primary.copy(alpha = 0.2f)
                        else -> SurfaceBG
                    },
                )
            ) {
                when {
                    isEnabled -> {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = OnSurfaceBG,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    isActive -> {
                        Text(
                            text = "${index + 1}",
                            color = OnPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                    else -> {
                        Text(
                            text = "${index + 1}",
                            color = OnSurfaceBG,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            if (index < totalSteps - 1) {
                Box(
                    Modifier
                        .width(30.dp)
                        .height(1.dp)
                        .background(Color.LightGray)
                )
            }
        }
    }
}