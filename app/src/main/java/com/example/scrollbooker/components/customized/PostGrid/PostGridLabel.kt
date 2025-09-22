package com.example.scrollbooker.components.customized.PostGrid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.entity.social.post.domain.model.LastMinute
import com.example.scrollbooker.entity.social.post.domain.model.PostProduct
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.bodyMedium
import java.math.BigDecimal

@Composable
fun PostGridLabel(
    lastMinute: LastMinute,
    product: PostProduct?
) {
    val color: Color = when {
        lastMinute.isLastMinute -> LastMinute
        product?.discount != null && product.discount > BigDecimal.ZERO -> Error
        else -> Color.Transparent
    }

    val text: String = when {
        lastMinute.isLastMinute -> stringResource(R.string.lastMinute)
        product?.discount != null && product.discount > BigDecimal.ZERO -> stringResource(R.string.sale)
        else -> ""
    }

    Box(modifier = Modifier
        .padding(5.dp)
        .background(
            color = color,
            shape = RoundedCornerShape(4.dp)
        )
        .padding(5.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            style = bodyMedium
        )
    }
}