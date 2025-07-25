package com.example.scrollbooker.ui.sharedModules.posts.components.postOverlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostProduct
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun PostOverlayProduct(product: PostProduct) {
    Column(
        modifier = Modifier.padding(vertical = BasePadding)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = product.name,
                style = titleMedium,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color.White
            )

            Spacer(Modifier.width(SpacingS))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(Modifier.height(SpacingS))

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${product.priceWithDiscount} ${product.currency}",
                    style = titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(Modifier.width(SpacingS))
                if(product.discount.compareTo(BigDecimal.ZERO) > 0) {
                    Text(
                        text = "${product.price}",
                        style = bodyLarge,
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.White
                    )
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        text = "(-${product.discount}%)",
                        color = Error,
                        style = bodyLarge
                    )
                }
            }
        }
    }
}