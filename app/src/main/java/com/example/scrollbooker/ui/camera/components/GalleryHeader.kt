package com.example.scrollbooker.ui.camera.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun GalleryHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomIconButton(
            imageVector = Icons.Default.Close,
            tint = OnBackground,
            boxSize = 60.dp,
            onClick = onBack
        )

        Text(
            text = stringResource(R.string.selectVideo),
            style = titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = OnBackground
        )

        CustomIconButton(
            imageVector = Icons.Default.Close,
            onClick = {},
            tint = Color.Transparent
        )
    }
}