package com.example.scrollbooker.components.customized.ProductCard

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.menu.Menu
import com.example.scrollbooker.components.core.menu.MenuItemData
import com.example.scrollbooker.components.customized.Protected.Protected
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun ProductCardActions(
    product: Product,
    displayEditableActions: Boolean,
    isEditable: Boolean,
    isSelected: Boolean,
    isLoadingDelete: Boolean,
    onSelect: ((Product) -> Unit)?,
    onNavigateToEdit: ((Int) -> Unit)? = null,
    onDeleteProduct: ((Int) -> Unit)? = null,
) {
    val showAddSingleButton =
        !displayEditableActions &&
                product.canBeBooked &&
                product.type == ProductTypeEnum.SINGLE

    val showBuyPackButton =
        !displayEditableActions &&
                product.canBeBooked &&
                product.type != ProductTypeEnum.SINGLE

    val showEditableMenu = isEditable && displayEditableActions

    when {
        showAddSingleButton -> {
            Protected(permission = PermissionEnum.BOOK_BUTTON_VIEW) {
                MainButtonOutlined(
                    title = if (isSelected) stringResource(R.string.added) else stringResource(R.string.add),
                    onClick = { onSelect?.invoke(product) },
                    trailingIcon = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                    trailingIconTint = Primary,
                    showTrailingIcon = true,
                )
            }
        }

        showBuyPackButton -> {
            Protected(permission = PermissionEnum.BOOK_BUTTON_VIEW) {
                MainButtonOutlined(
                    title = stringResource(R.string.buy),
                    onClick = { onSelect?.invoke(product) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Primary,
                        contentColor = OnPrimary
                    ),
                    border = BorderStroke(1.dp, Primary),
                    showTrailingIcon = false,
                )
            }
        }

        showEditableMenu -> {
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