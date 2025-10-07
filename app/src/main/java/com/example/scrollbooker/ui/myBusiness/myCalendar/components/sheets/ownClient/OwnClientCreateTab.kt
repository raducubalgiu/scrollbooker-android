package com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.ownClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service

@Composable
fun OwnClientCreateTab(
    servicesState: FeatureState<List<Service>>,
    currenciesState: FeatureState<List<Currency>>,
    buttonHeightDp: Dp,
    ownClientState: OwnClientFormStateState,
    onEvent: (OwnClientFormEvent) -> Unit,
    validation: OwnClientValidationResult
) {
    val servicesOptionList = when(val state = servicesState) {
        is FeatureState.Success -> state.data.map { service ->
            Option(
                value = service.id.toString(),
                name = service.name
            )
        }
        else -> emptyList()
    }

    val currenciesOptionList = when(val state = currenciesState) {
        is FeatureState.Success -> state.data.map { service ->
            Option(
                value = service.id.toString(),
                name = service.name
            )
        }
        else -> emptyList()
    }

    val productsOptionsList = listOf(
        Option(value = "1", "Tuns Special"),
        Option(value = "2", "Tuns Scurt"),
        Option(value = "3", "Tuns bros")
    )

    val isServicesEnabled = servicesState is FeatureState.Success
    val isCurrencyEnabled = currenciesState is FeatureState.Success

    val isClientNameValid = validation.customerNameError.isNullOrBlank()
    val isServiceNameValid = validation.serviceNameError.isNullOrBlank()
    val isProductNameValid = validation.productNameError.isNullOrBlank()
    val isPriceWithDiscountValid = validation.priceWithDiscountError.isNullOrBlank()
    val isDiscountValid = validation.discountError.isNullOrBlank()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = buttonHeightDp + BasePadding)

    ) {
        Input(
            label = "${stringResource(R.string.clientName)}*",
            value = ownClientState.customerName,
            onValueChange = {
                onEvent(OwnClientFormEvent.CustomerNameChanged(it))
            },
            isError = !isClientNameValid,
            errorMessage = validation.customerNameError,
            maxLength = validation.customerNameMaxLength
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        OwnClientService(
            options = servicesOptionList,
            isServicesSelectEnabled = isServicesEnabled,
            selectedServiceId = ownClientState.selectedServiceId.toString(),
            onSelectedServiceId = { newValue ->
                val service = newValue?.let {
                    servicesOptionList.first { it.value == newValue }
                }

                onEvent(OwnClientFormEvent.ServiceSelected(newValue))
                onEvent(OwnClientFormEvent.ServiceNameChanged(service?.name!!))
            },
            serviceName = ownClientState.serviceName,
            onServiceChange = { onEvent(OwnClientFormEvent.ServiceSelected(it)) },
            isServiceNameValid = isServiceNameValid,
            serviceNameError = validation.serviceNameError,
            serviceNameMaxLength = validation.serviceNameMaxLength
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        OwnClientProduct(
            options = productsOptionsList,
            isProductsSelectEnabled = true,
            selectedProductId = ownClientState.selectedProductId.toString(),
            onSelectedProductId = { newValue ->
                val product = newValue?.let {
                    productsOptionsList.first { it.value == newValue }
                }
                onEvent(OwnClientFormEvent.ProductSelected(newValue))
                onEvent(OwnClientFormEvent.ProductNameChanged(product?.name!!))
            },
            productName = ownClientState.productName,
            onProductChange = { onEvent(OwnClientFormEvent.ProductNameChanged(it)) },
            isProductNameValid = isProductNameValid,
            productNameError = validation.productNameError,
            productNameMaxLength = validation.productNameMaxLength
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        Input(
            label = "${stringResource(R.string.fullPrice)}*",
            value = ownClientState.price,
            onValueChange = { onEvent(OwnClientFormEvent.PriceChanged(it)) },
            isEnabled = false,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        Input(
            label = "${stringResource(R.string.finalPrice)}*",
            value = ownClientState.priceWithDiscount,
            isError = !isPriceWithDiscountValid,
            errorMessage = validation.priceWithDiscountError,
            onValueChange = { onEvent(OwnClientFormEvent.PriceWithDiscountChanged(it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        Input(
            label = "${stringResource(R.string.discount)}*",
            value = ownClientState.discount,
            onValueChange = { onEvent(OwnClientFormEvent.DiscountChanged(it)) },
            isError = !isDiscountValid,
            errorMessage = validation.discountError,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        InputSelect(
            label = "${stringResource(R.string.currency)}*",
            placeholder = "${stringResource(R.string.currency)}*",
            options = currenciesOptionList,
            isEnabled = isCurrencyEnabled,
            isRequired = true,
            selectedOption = ownClientState.selectedCurrencyId.toString(),
            onValueChange = { onEvent(OwnClientFormEvent.CurrencySelected(it)) }
        )
    }
}