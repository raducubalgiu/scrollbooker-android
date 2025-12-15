package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.generateTimeSlots
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineMedium

@Composable
fun OwnClientCreateTab(
    buttonHeightDp: Dp,
    ownClientState: OwnClientFormStateState,
    onEvent: (OwnClientFormEvent) -> Unit,
    validation: OwnClientValidationResult,
    showErrors: Boolean,
    hasSelectedSlot: Boolean
) {
    val isClientNameValid = validation.customerNameError.isNullOrBlank()
    val isProductNameValid = validation.productNameError.isNullOrBlank()
    val isPriceValid = validation.priceError.isNullOrBlank()

    val slots = remember { generateTimeSlots() }

    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = buttonHeightDp + BasePadding)

    ) {
        Spacer(Modifier.height(BasePadding))

        Text(
            modifier = Modifier.padding(bottom = BasePadding),
            style = headlineMedium,
            color = OnBackground,
            fontWeight = FontWeight.ExtraBold,
            text = stringResource(R.string.addAppointment),
        )

        Spacer(Modifier.height(BasePadding))

        if(!hasSelectedSlot) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(0.5f)) {
                    InputSelect(
                        options = slots,
                        onValueChange = { startTime = it.toString() },
                        selectedOption = startTime,
                        placeholder = "De la"
                    )
                }

                Spacer(Modifier.width(SpacingS))

                Column(Modifier.weight(0.5f)) {
                    InputSelect(
                        options = slots,
                        onValueChange = { endTime = it.toString() },
                        selectedOption = endTime,
                        placeholder = "Pana la"
                    )
                }
            }

            Spacer(Modifier.height(BasePadding))

        }

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