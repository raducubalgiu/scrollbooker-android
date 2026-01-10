package com.example.scrollbooker.components.core.layout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    arrangement: Arrangement.Vertical = Arrangement.Center,
    message: String,
    icon: Painter,
    color: Color = OnSurfaceBG
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = arrangement
    ) {
        Column(modifier = Modifier.padding(BasePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(50.dp),
                tint = Divider,
                painter = icon,
                contentDescription = null,
            )
            Spacer(Modifier.height(BasePadding))
            Text(
                style = bodyLarge,
                text = message,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}