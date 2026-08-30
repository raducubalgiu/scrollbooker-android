package com.example.scrollbooker.ui.myBusiness.myProducts.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.RoundCheckbox
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.ProductVariantState
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.ProductVariantValidation
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BusinessWithEmployeesForm(
    employeesState: FeatureState<List<Employee>>,
    variantState: ProductVariantState,
    showErrors: Boolean,
    validation: ProductVariantValidation,
    onToggleSelected: (index: Int) -> Unit,
    onPriceChange: (index: Int, value: String) -> Unit,
    onDiscountChange: (index: Int, value: String) -> Unit
) {
    when (val employees = employeesState) {
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Success -> {
            Column {
                variantState.offerings.forEachIndexed { index, offering ->
                    val employee = employees.data.find { it.id.toString() == offering.userId }
                    val offeringValidation = validation.offeringValidations.getOrNull(index)

                    val rowErrors = remember(offeringValidation, showErrors) {
                        if (showErrors) {
                            listOfNotNull(
                                offeringValidation?.priceError,
                                offeringValidation?.discountError
                            ).filter { it.isNotBlank() }
                        } else {
                            emptyList()
                        }
                    }

                    val fieldColor = if (offering.isSelected) Color.White else SurfaceBG

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(BasePadding))
                                .background(
                                    if (offering.isSelected) Primary.copy(alpha = 0.08f) else SurfaceBG
                                )
                                .padding(SpacingM)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(SpacingS)
                            ) {
                                Avatar(
                                    url = employee?.avatar ?: "",
                                    size = 36.dp
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = employee?.fullName.orEmpty(),
                                        style = titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Text(
                                        text = employee?.job.orEmpty(),
                                        style = bodyMedium,
                                        color = Color.Gray
                                    )
                                }

                                RoundCheckbox(
                                    checked = offering.isSelected,
                                    onCheckedChange = { onToggleSelected(index) }
                                )
                            }

                            Spacer(Modifier.height(SpacingM))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(SpacingXS)
                            ) {
                                Input(
                                    modifier = Modifier.weight(1f),
                                    value = offering.price,
                                    label = stringResource(R.string.price),
                                    onValueChange = { onPriceChange(index, it) },
                                    isError = showErrors && offeringValidation?.priceError != null,
                                    isEnabled = offering.isSelected,
                                    inputColor = fieldColor,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next,
                                        keyboardType = KeyboardType.Number
                                    )
                                )

                                Input(
                                    modifier = Modifier.weight(1f),
                                    value = offering.discount,
                                    label = stringResource(R.string.discount),
                                    onValueChange = { onDiscountChange(index, it) },
                                    isError = showErrors && offeringValidation?.discountError != null,
                                    isEnabled = offering.isSelected,
                                    inputColor = fieldColor,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Next,
                                        keyboardType = KeyboardType.Number
                                    )
                                )

                                Input(
                                    modifier = Modifier.weight(1f),
                                    value = offering.priceWithDiscount,
                                    label = stringResource(R.string.priceWithDiscount),
                                    onValueChange = {},
                                    isEnabled = false,
                                    inputColor = SurfaceBG,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done,
                                        keyboardType = KeyboardType.Number
                                    )
                                )
                            }

                            AnimatedVisibility(visible = rowErrors.isNotEmpty()) {
                                Column(modifier = Modifier.padding(top = SpacingXS)) {
                                    rowErrors.forEach { message ->
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Outlined.Warning,
                                                contentDescription = null,
                                                tint = Error,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(Modifier.width(SpacingS))
                                            Text(
                                                text = message,
                                                color = Error,
                                                style = bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (index < variantState.offerings.lastIndex) {
                            Spacer(Modifier.height(SpacingM))
                        }
                    }
                }
            }
        }
    }
}
