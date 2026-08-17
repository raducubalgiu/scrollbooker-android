package com.example.scrollbooker.ui.myBusiness.myProducts.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.ProductVariantState
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.ProductVariantValidation
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
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

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(SpacingS)
                        ) {
                            Avatar(
                                url = employee?.avatar ?: "",
                                size = 30.dp
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

                            Checkbox(
                                checked = offering.isSelected,
                                onCheckedChange = { onToggleSelected(index) }
                            )
                        }

                        Spacer(Modifier.height(SpacingXS))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(SpacingS)
                        ) {
                            Input(
                                value = offering.price,
                                label = stringResource(R.string.price),
                                onValueChange = { onPriceChange(index, it) },
                                isError = showErrors && offeringValidation?.priceError != null,
                                modifier = Modifier.weight(1f),
                                isEnabled = offering.isSelected
                            )

                            Input(
                                value = offering.discount,
                                label = stringResource(R.string.discount),
                                onValueChange = { onDiscountChange(index, it) },
                                isError = showErrors && offeringValidation?.discountError != null,
                                modifier = Modifier.weight(1f),
                                isEnabled = offering.isSelected
                            )

                            Input(
                                value = offering.priceWithDiscount,
                                label = stringResource(R.string.priceWithDiscount),
                                onValueChange = {},
                                isEnabled = false,
                                modifier = Modifier.weight(1f),
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

                        if (index < variantState.offerings.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = SpacingM),
                                thickness = 0.55.dp,
                                color = Divider
                            )
                        }
                    }
                }
            }
        }
    }
}
