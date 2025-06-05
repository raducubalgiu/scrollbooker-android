package com.example.scrollbooker.core.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun MessageScreen(
    icon: ImageVector,
    message: String,
    iconSize: Dp = 64.dp,
    contentPadding: PaddingValues = PaddingValues(32.dp),
    textStyle: TextStyle = bodyMedium
) {
    Column(modifier = Modifier.fillMaxSize().padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.padding(BasePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                modifier = Modifier.size(iconSize),
                tint = Divider,
                contentDescription = null,
            )
            Spacer(Modifier.height(BasePadding))
            Text(
                text = message,
                style = textStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}