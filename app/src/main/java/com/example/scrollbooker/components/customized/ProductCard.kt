package com.example.scrollbooker.components.customized

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.MainButton
import com.example.scrollbooker.feature.products.domain.model.ProductCardEnum
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.feature.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun ProductCard(
    product: Product,
    mode: ProductCardEnum,
    onNavigate: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(vertical = SpacingXL)) {
        Text(
            text = product.name,
            fontWeight = FontWeight.Bold,
            style = titleMedium,
            fontSize = 20.sp,
            color = OnBackground
        )
        Spacer(Modifier.height(SpacingXXS))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Femei",
                style = bodyMedium
            )
            Text(text = "\u2022", Modifier.padding(horizontal = 5.dp))
            Text(
                text = "${product.duration}min",
                style = bodyMedium
            )
        }
        Text(
            style = bodyMedium,
            text = product.description,
            color = OnSurfaceBG,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(SpacingS))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${product.priceWithDiscount} RON",
                        style = titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(SpacingS))
                    if(product.discount.compareTo(BigDecimal.ZERO) > 0) {
                        Text(
                            text = "${product.price}",
                            style = bodyMedium,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Spacer(Modifier.width(SpacingS))
                        Text(
                            text = "(-${product.discount}%)",
                            color = Error
                        )
                    }
                }
                if(mode == ProductCardEnum.CLIENT) {
                    Button(onClick = {}) {
                        Text(
                            style = bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            text = stringResource(R.string.book)
                        )
                    }
                }
            }
        }

        if(mode == ProductCardEnum.OWNER) {
            Spacer(Modifier.height(BasePadding))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MainButton(
                    modifier = Modifier.weight(0.5f),
                    title = stringResource(R.string.edit),
                    onClick = { onNavigate("${MainRoute.EditProduct.route}/${product.id}/${product.name}") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceBG,
                        contentColor = OnSurfaceBG
                    )
                )
                Spacer(Modifier.width(BasePadding))
                MainButton(
                    modifier = Modifier.weight(0.5f),
                    title = stringResource(R.string.delete),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Error
                    )
                )
            }
        }
    }

    HorizontalDivider(color = Divider)
}