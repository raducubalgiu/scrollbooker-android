package com.example.scrollbooker.components.core.layout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    arrangement: Arrangement.Vertical = Arrangement.Center,
    icon: Painter,
    message: String,
    iconSize: Dp = 64.dp,
) {
    Column(
        modifier = Modifier.fillMaxSize().then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = arrangement
    ) {
        Icon(
            painter = icon,
            modifier = Modifier.size(iconSize),
            tint = Divider,
            contentDescription = null,
        )
        Spacer(Modifier.height(BasePadding))
        Text(
            text = message,
            style = bodyLarge,
            color = OnBackground,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal
        )
    }
}