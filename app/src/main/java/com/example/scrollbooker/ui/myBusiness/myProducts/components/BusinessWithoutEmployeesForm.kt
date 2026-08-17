package com.example.scrollbooker.ui.myBusiness.myProducts.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.ProductOfferingState
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.ProductOfferingValidation
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun BusinessWithoutEmployeesForm(
    showErrors: Boolean,
    offering: ProductOfferingState?,
    offeringValidation: ProductOfferingValidation?,
    onPriceChange: (String) -> Unit,
    onDiscountChange: (String) -> Unit
) {
    Column {
        if (offering != null) {
            Input(
                label = stringResource(R.string.price),
                value = offering.price,
                onValueChange = onPriceChange,
                isError = showErrors && offeringValidation?.priceError != null,
                errorMessage = offeringValidation?.priceError.orEmpty(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(Modifier.height(BasePadding))

            Input(
                label = stringResource(R.string.discount),
                value = offering.discount,
                onValueChange = onDiscountChange,
                isError = showErrors && offeringValidation?.discountError != null,
                errorMessage = offeringValidation?.discountError.orEmpty(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Percent,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
            )

            Spacer(Modifier.height(BasePadding))

            Input(
                label = stringResource(R.string.fullPrice),
                value = offering.priceWithDiscount,
                onValueChange = { },
                readOnly = true,
                isEnabled = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Calculate,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = SurfaceBG,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = OnSurfaceBG,
                    disabledLabelColor = OnSurfaceBG.copy(0.5f)
                )
            )
        }
    }
}