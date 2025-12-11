package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.headlineMedium

@Composable
fun OwnClientCreateTab(
    buttonHeightDp: Dp,
    ownClientState: OwnClientFormStateState,
    onEvent: (OwnClientFormEvent) -> Unit,
    validation: OwnClientValidationResult,
    showErrors: Boolean
) {
    val isClientNameValid = validation.customerNameError.isNullOrBlank()
    val isProductNameValid = validation.productNameError.isNullOrBlank()
    val isPriceValid = validation.priceError.isNullOrBlank()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = buttonHeightDp + BasePadding)

    ) {
        Spacer(Modifier.height(BasePadding))

        Text(
            modifier = Modifier.padding(bottom = BasePadding),
            text = stringResource(R.string.addAppointment),
            style = headlineMedium,
            fontWeight = FontWeight.SemiBold,
            fontSize = 26.sp
        )

        Spacer(Modifier.height(BasePadding))

        Input(
            label = "${stringResource(R.string.clientName)}*",
            value = ownClientState.customerName,
            onValueChange = { onEvent(OwnClientFormEvent.CustomerNameChanged(it)) },
            isError = showErrors && !isClientNameValid,
            errorMessage = validation.customerNameError,
            maxLength = validation.customerNameMaxLength,
        )

        Spacer(Modifier.height(BasePadding))

        Input(
            label = "${stringResource(R.string.product)}*",
            value = ownClientState.productName,
            onValueChange = { onEvent(OwnClientFormEvent.ProductNameChanged(it)) },
            isError = showErrors && !isProductNameValid,
            errorMessage = validation.productNameError,
            maxLength = validation.productNameMaxLength
        )

        Spacer(Modifier.height(BasePadding))

        Input(
            label = "${stringResource(R.string.price)}*",
            value = ownClientState.price,
            isError = showErrors && !isPriceValid,
            errorMessage = validation.priceError,
            onValueChange = { onEvent(OwnClientFormEvent.PriceChanged(it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.height(BasePadding))

        Input(
            label = "${stringResource(R.string.currency)}*",
            value = "RON",
            isEnabled = false,
            readOnly = true,
            onValueChange = { },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            )
        )
    }
}