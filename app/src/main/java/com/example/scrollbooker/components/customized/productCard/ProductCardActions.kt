package com.example.scrollbooker.components.customized.productCard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.menu.Menu
import com.example.scrollbooker.components.core.menu.MenuItemData
import com.example.scrollbooker.components.customized.protected.Protected
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
    onNavigateToBooking: (product: Product) -> Unit
) {
    val (showAddSingleButtonSelectable, showAddSingleButtonNotSelectable, showBuyPackButton, showEditableMenu) = remember(
        product.type, product.canBeBooked, isSelectable, isEditable, displayEditableActions
    ) {
        val isSingle = product.type == ProductTypeEnum.SINGLE
        val canBook = product.canBeBooked
        val isEditMenu = isEditable && displayEditableActions

        val btnSelectable = !displayEditableActions && canBook && isSingle && isSelectable
        val btnNotSelectable = !displayEditableActions && canBook && isSingle && !isSelectable
        val btnPack = !displayEditableActions && canBook && !isSingle

        Quadruple(btnSelectable, btnNotSelectable, btnPack, isEditMenu)
    }

    val handleSelect = remember(product, onSelect) {
        { onSelect?.invoke(product) ?: Unit }
    }

    val handleBooking = remember(product, onNavigateToBooking) {
        { onNavigateToBooking(product) }
    }

    val handleEdit = remember(product.id, onNavigateToEdit) {
        { onNavigateToEdit?.invoke(product.id) ?: Unit }
    }

    val handleDelete = remember(product.id, onDeleteProduct) {
        { onDeleteProduct?.invoke(product.id) ?: Unit }
    }

    when {
        showAddSingleButtonSelectable -> {
            val containerColor = if (isSelected) Primary else Background
            val contentColor = if (isSelected) OnPrimary else OnBackground
            val shadowElevation = if (isSelected) 1.dp else 4.dp

            Protected(permission = PermissionEnum.BOOK_BUTTON_VIEW) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(
                            elevation = shadowElevation,
                            shape = ShapeDefaults.ExtraLarge,
                            clip = false
                        )
                ) {
                    FilledIconButton(
                        onClick = handleSelect,
                        shape = ShapeDefaults.ExtraLarge,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = containerColor,
                            contentColor = contentColor
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Add,
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
                    onClick = handleBooking
                )
            }
        }

        showBuyPackButton -> {
            Protected(permission = PermissionEnum.BOOK_BUTTON_VIEW) {
                MainButtonOutlined(
                    title = stringResource(R.string.buy),
                    onClick = handleSelect,
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
            val editIcon = painterResource(R.drawable.ic_edit_outline)
            val deleteIcon = painterResource(R.drawable.ic_delete_outline)

            Menu(
                items = listOf(
                    MenuItemData(
                        text = stringResource(R.string.edit),
                        leadingIcon = editIcon,
                        onClick = handleEdit,
                    ),
                    MenuItemData(
                        text = stringResource(R.string.delete),
                        color = Error,
                        leadingIcon = deleteIcon,
                        enabled = !isLoadingDelete,
                        isLoading = isLoadingDelete,
                        onClick = handleDelete,
                    )
                )
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)