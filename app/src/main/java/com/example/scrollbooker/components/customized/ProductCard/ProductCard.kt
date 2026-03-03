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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.menu.Menu
import com.example.scrollbooker.components.core.menu.MenuItemData
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodySmall

@Composable
fun ProductCard(
    product: Product,
    displayEditableActions: Boolean = false,
    isEditable: Boolean = false,
    isSelected: Boolean = false,
    isLoadingDelete: Boolean = false,
    onSelect: ((Product) -> Unit)? = null,
    onNavigateToEdit: ((Int) -> Unit)? = null,
    onDeleteProduct: ((productId: Int) -> Unit)? = null,
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
                    filters = product.filters,
                    type = product.type,
                    sessionsCount = product.sessionsCount
                )

                Spacer(Modifier.width(SpacingS))

                when {
                    !displayEditableActions && product.canBeBooked -> {
                        MainButtonOutlined(
                            title = if(isSelected) stringResource(R.string.added) else stringResource(R.string.add),
                            onClick = { onSelect?.invoke(product) },
                            trailingIcon = if(isSelected) Icons.Default.Check else Icons.Default.Add,
                            trailingIconTint = Primary,
                            showTrailingIcon = true,
                        )
                    }

                    isEditable && displayEditableActions -> {
                        Menu(
                            items = listOf(
                                MenuItemData(
                                    text = stringResource(R.string.edit),
                                    leadingIcon = painterResource(R.drawable.ic_edit_outline),
                                    onClick = { onNavigateToEdit?.invoke(product.id) },
                                ),
                                MenuItemData(
                                    text = stringResource(R.string.delete),
                                    color = Error,
                                    leadingIcon = painterResource(R.drawable.ic_delete_outline),
                                    enabled = !isLoadingDelete,
                                    isLoading = isLoadingDelete,
                                    onClick = { onDeleteProduct?.invoke(product.id) },
                                )
                            )
                        )
                    }
                }
            }

            if(product.description != null && product.description.isNotEmpty()) {
                Spacer(Modifier.height(BasePadding))

                Text(
                    text = product.description,
                    color = Color.Gray,
                    style = bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if(!product.canBeBooked) {
                Spacer(Modifier.height(BasePadding))

                Text(
                    text = "Acest serviciu poate fi rezervat doar in urma unei discutii telefonice",
                    color = Error,
                    style = bodySmall
                )
            }
        }
    }
}