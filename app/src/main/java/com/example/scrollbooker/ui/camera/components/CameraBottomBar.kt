package com.example.scrollbooker.ui.camera.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CameraBottomBar() {
    Row(modifier = Modifier.fillMaxWidth().height(90.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(90.dp)
                .padding(
                    top = SpacingXL,
                    start = BasePadding,
                    end = BasePadding
                )
                .clickable {},
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "15s",
                style = titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Box(
            modifier = Modifier
                .height(90.dp)
                .padding(
                    top = SpacingXL,
                    start = BasePadding,
                    end = BasePadding
                )
                .clickable {},
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "30s",
                style = titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Divider,
                fontSize = 18.sp
            )
        }

        Box(
            modifier = Modifier
                .height(90.dp)
                .padding(
                    top = SpacingXL,
                    start = BasePadding,
                    end = BasePadding
                )
                .clickable {},
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "60s",
                style = titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Divider,
                fontSize = 18.sp
            )
        }
    }
}