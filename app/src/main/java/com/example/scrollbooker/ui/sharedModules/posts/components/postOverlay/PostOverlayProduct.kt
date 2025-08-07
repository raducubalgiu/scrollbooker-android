package com.example.scrollbooker.ui.sharedModules.posts.components.postOverlay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostProduct
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun PostOverlayProduct(product: PostProduct) {
    HorizontalDivider(color = Divider, thickness = 0.55.dp)
    Spacer(Modifier.height(SpacingM))

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = product.name,
                style = titleMedium,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color.White
            )

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
                    product.discount.compareTo(BigDecimal.ZERO).let {
                        if(it > 0 == true) {
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

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Divider
        )
    }

    Spacer(Modifier.height(BasePadding))
}