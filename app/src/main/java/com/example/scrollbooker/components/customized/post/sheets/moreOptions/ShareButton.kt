package com.example.scrollbooker.components.customized.post.sheets.moreOptions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun ShareButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String
) {
    Column(modifier = modifier
        .clip(shape = ShapeDefaults.Large)
        .background(SurfaceBG)
        .padding(vertical = BasePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )

        Spacer(Modifier.height(BasePadding))

        Text(
            text = text,
            fontWeight = FontWeight.SemiBold
        )
    }
}