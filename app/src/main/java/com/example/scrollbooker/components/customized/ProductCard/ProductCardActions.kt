package com.example.scrollbooker.components.customized.ProductCard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun ProductCardActions(
    product: Product,
    isEditable: Boolean,
    isSelected: Boolean,
    isSelectable: Boolean,
    displayEditableActions: Boolean,
    isLoadingDelete: Boolean,
    onSelect: ((Product) -> Unit)?,
    onNavigateToEdit: ((Int) -> Unit)? = null,
    onDeleteProduct: ((Int) -> Unit)? = null,
) {
    val showAddSingleButtonSelectable =
        !displayEditableActions &&
                product.canBeBooked &&
                product.type == ProductTypeEnum.SINGLE &&
                isSelectable

    val showAddSingleButtonNotSelectable =
        !displayEditableActions &&
                product.canBeBooked &&
                product.type == ProductTypeEnum.SINGLE &&
                !isSelectable

    val showBuyPackButton =
        !displayEditableActions &&
                product.canBeBooked &&
                product.type != ProductTypeEnum.SINGLE

    val showEditableMenu = isEditable && displayEditableActions

    when {
        showAddSingleButtonSelectable -> {
            val containerColor = if (isSelected) Background.copy(alpha = 0.6f) else Background
            val contentColor = OnBackground
            val shadowElevation = if (isSelected) 1.dp else 4.dp

            Protected(permission = PermissionEnum.BOOK_BUTTON_VIEW) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(
                            elevation = shadowElevation,
                            shape = RoundedCornerShape(12.dp),
                            clip = false
                        )
                ) {
                    FilledIconButton(
                        onClick = { onSelect?.invoke(product) },
                        shape = RoundedCornerShape(12.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = containerColor,
                            contentColor = contentColor
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.Remove else Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        showAddSingleButtonNotSelectable -> {
            Protected(permission = PermissionEnum.BOOK_BUTTON_VIEW) {
                MainButtonOutlined(
                    contentPadding = PaddingValues(
                        vertical = SpacingS,
                        horizontal = BasePadding
                ),
                    title = stringResource(R.string.book),
                    onClick = { onSelect?.invoke(product) }
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