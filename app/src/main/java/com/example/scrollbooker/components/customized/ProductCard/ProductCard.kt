package com.example.scrollbooker.components.customized.ProductCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.customized.Protected.Protected
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodySmall

@Composable
fun ProductCard(
    product: Product,
    displayActions: Boolean = false,
    isLoadingDelete: Boolean = false,
    onNavigateToEdit: ((Int) -> Unit)? = null,
    onDeleteProduct: ((productId: Int) -> Unit)? = null,
    isSelected: Boolean = true,
    onSelect: (Product) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(BasePadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProductDetails(
                    modifier = Modifier.weight(1f),
                    name = product.name,
                    duration = product.duration,
                    price = product.price,
                    priceWithDiscount = product.priceWithDiscount,
                    discount = product.discount,
                    filters = product.filters
                )

                Spacer(Modifier.width(SpacingS))

                if(!displayActions && product.canBeBooked) {
                    Protected(permission = PermissionEnum.BOOK_BUTTON_VIEW) {
                        MainButtonOutlined(
                            title = if(isSelected) stringResource(R.string.added) else stringResource(R.string.add),
                            onClick = { onSelect(product) },
                            trailingIcon = if(isSelected) Icons.Default.Check else Icons.Default.Add,
                            trailingIconTint = Primary,
                            showTrailingIcon = true
                        )
                    }
                }
            }

            Spacer(Modifier.height(BasePadding))

            if(!product.canBeBooked) {
                Text(
                    text = "Acest serviciu poate fi rezervat doar in urma unei discutii telefonice",
                    color = Error,
                    style = bodySmall
                )

                Spacer(Modifier.height(BasePadding))
            }

            if(displayActions) {
                ProductCardActions(
                    isLoadingDelete = isLoadingDelete,
                    onNavigateToEdit = { onNavigateToEdit?.invoke(product.id) },
                    onDeleteProduct = { onDeleteProduct?.invoke(product.id) }
                )
            }
        }
    }
}