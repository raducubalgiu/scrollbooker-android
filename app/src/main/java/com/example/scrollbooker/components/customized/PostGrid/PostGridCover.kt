package com.example.scrollbooker.components.customized.PostGrid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.social.post.domain.model.PostProduct
import com.example.scrollbooker.ui.theme.Secondary
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun PostGridCover(product: PostProduct) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = BasePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier
            .background(
                color = Secondary.copy(alpha = 0.7f),
                shape = RoundedCornerShape(1.dp)
            )
            .padding(
                vertical = 12.dp,
                horizontal = 10.dp
            )
        ) {
            Text(
                text = product.name,
                style = titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}