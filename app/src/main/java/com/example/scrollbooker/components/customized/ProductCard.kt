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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.customized.Protected.Protected
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.navigators.NavigateCalendarParam
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun ProductCard(
    product: Product,
    isSelected: Boolean = true,
    displayActions: Boolean = false,
    isLoadingDelete: Boolean = false,
    onNavigateToEdit: ((Int) -> Unit)? = null,
    onDeleteProduct: ((productId: Int) -> Unit)? = null,
    onSelect: (Product) -> Unit
    //onNavigateToCalendar: (NavigateCalendarParam) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(BasePadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = titleMedium,
                        fontSize = 18.sp,
                        color = OnBackground
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Femei",
                            color = Color.Gray
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = "\u2022",
                            color = Color.Gray
                        )
                        Text(
                            text = "${product.duration}min",
                            style = bodyLarge,
                            color = Color.Gray
                        )
                    }

                    Spacer(Modifier.height(SpacingS))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${product.priceWithDiscount} RON",
                                    style = titleMedium,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold
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
                        }
                    }
                }

                Protected(permission = PermissionEnum.BOOK_BUTTON_VIEW) {
                    MainButtonOutlined(
                        title = if(isSelected) stringResource(R.string.added)
                                else stringResource(R.string.add),
                        onClick = {
                            onSelect(product)
//                            onNavigateToCalendar(
//                                NavigateCalendarParam(
//                                    userId = product.userId,
//                                    slotDuration = product.duration,
//                                    productId = product.id,
//                                    productName = product.name
//                                )
//                            )
                        },
                        trailingIcon = Icons.Default.Check,
                        trailingIconTint = Color.Green,
                        showTrailingIcon = isSelected
                    )
                }
            }

            Spacer(Modifier.height(BasePadding))

            if(displayActions) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainButtonOutlined(
                        modifier = Modifier
                            .weight(0.5f)
                            .clip(shape = ShapeDefaults.ExtraLarge),
                        title = stringResource(R.string.edit),
                        onClick = { onNavigateToEdit?.invoke(product.id) },
                        icon = painterResource(R.drawable.ic_edit_outline),
                        iconColor = Color.Gray
                    )

                    Spacer(Modifier.width(SpacingS))

                    MainButtonOutlined(
                        modifier = Modifier
                            .weight(0.5f)
                            .clip(shape = ShapeDefaults.ExtraLarge),
                        title = stringResource(R.string.delete),
                        isLoading = isLoadingDelete,
                        isEnabled = !isLoadingDelete,
                        onClick = { onDeleteProduct?.invoke(product.id) },
                        icon = painterResource(R.drawable.ic_delete_outline),
                        iconColor = Error
                    )
                }
            }
        }
    }
}