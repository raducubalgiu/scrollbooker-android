package com.example.scrollbooker.components.customized.ProductCard
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.getDurationText
import com.example.scrollbooker.entity.booking.products.domain.model.getFiltersSummary
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.labelSmall
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    displayEditableActions: Boolean = false,
    displayDescription: Boolean = true,
    isEditable: Boolean = false,
    isSelected: Boolean = false,
    isSelectable: Boolean = false,
    isLoadingDelete: Boolean = false,
    onOpenProductDetail: (Product) -> Unit,
    onSelect: ((Product) -> Unit)? = null,
    onNavigateToEdit: ((Int) -> Unit)? = null,
    onNavigateToBooking: (product: Product) -> Unit,
    onDeleteProduct: ((productId: Int) -> Unit)? = null,
) {
    val productSummaryText = "${product.getDurationText(product.startingOffering.duration)} • ${product.getFiltersSummary()}"

    Column(
        modifier = modifier
            .clickable { onOpenProductDetail(product) }
            .padding(vertical = BasePadding)
    ) {
        Column {
            if (
                product.filters.isNotEmpty() &&
                product.type == ProductTypeEnum.PACK &&
                product.sessionsCount != null
            ) {
                ProductPackageBadge(sessionsCount = product.sessionsCount)
                Spacer(Modifier.height(BasePadding))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = titleMedium,
                        fontSize = 18.sp,
                        color = OnBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(SpacingXXS))

                    Text(
                        text = productSummaryText,
                        style = bodyLarge,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(SpacingS))

                    ProductCardRowPrice(
                        price = product.startingOffering.price,
                        priceWithDiscount = product.startingOffering.priceWithDiscount,
                        discount = product.startingOffering.discount
                    )
                }

                Spacer(Modifier.width(SpacingS))

                ProductCardActions(
                    product = product,
                    displayEditableActions = displayEditableActions,
                    isEditable = isEditable,
                    isSelected = isSelected,
                    isSelectable = isSelectable,
                    isLoadingDelete = isLoadingDelete,
                    onSelect = onSelect,
                    onNavigateToEdit = onNavigateToEdit,
                    onDeleteProduct = onDeleteProduct,
                    onNavigateToBooking = onNavigateToBooking
                )
            }

            if(product.description != null &&
                product.description.isNotEmpty() &&
                displayDescription
            ) {
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

@Composable
private fun ProductPackageBadge(sessionsCount: Int?) {
    if (sessionsCount == null) return

    Box(
        modifier = Modifier
            .background(
                color = Primary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Pachet - $sessionsCount ședințe",
            style = labelSmall,
            color = Primary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
