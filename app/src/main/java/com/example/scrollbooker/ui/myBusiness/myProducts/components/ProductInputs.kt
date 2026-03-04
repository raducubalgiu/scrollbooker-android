package com.example.scrollbooker.ui.myBusiness.myProducts.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServicesResponse
import com.example.scrollbooker.ui.myBusiness.myProducts.SelectedFilters
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.labelLarge

@Immutable
data class ProductInputsState(
    val showErrors: Boolean,
    val validation: AddProductValidation,
    val filtersActions: FiltersActions,
    val productState: AddProductState,
    val serviceDomains: FeatureState<ServiceDomainWithEmployeeServicesResponse>,
    val selectedFilters: SelectedFilters,
)

@Immutable
data class ProductInputsActions(
    val onSetServiceDomainId: (String) -> Unit,
    val onSetServiceId: (String) -> Unit,
    val onSetProductType: (ProductTypeEnum) -> Unit,
    val onSetSessionsCount: (String) -> Unit,
    val onSetValidityDays: (String) -> Unit,
    val onSetName: (String) -> Unit,
    val onSetDescription: (String) -> Unit,
    val onSetDuration: (String) -> Unit,
    val onSetPrice: (String) -> Unit,
    val onSetDiscount: (String) -> Unit,
    val onSetCanBeBooked: (Boolean) -> Unit,
)

@Composable
fun ProductInputs(
    state: ProductInputsState,
    actions: ProductInputsActions,
) {
    val context = LocalContext.current

    val priceWithDiscount by remember(state.productState.price, state.productState.discount) {
        derivedStateOf {
            calculatePriceWithDiscount(
                price = state.productState.price,
                discountPercent = state.productState.discount
            ).toPlainString()
        }
    }

    val serviceDomainsOptionsList = when(val s = state.serviceDomains) {
        is FeatureState.Success -> s.data.serviceDomains.map { domain ->
            Option(
                value = domain.id.toString(),
                name = domain.name
            )
        }
        else -> emptyList()
    }

    val servicesOptionList = when(val s = state.serviceDomains) {
        is FeatureState.Success -> {
            val services = s.data.serviceDomains.find {
                it.id.toString() == state.productState.serviceDomainId }?.services ?: emptyList()
            services.map { service ->
                Option(
                    value = service.id.toString(),
                    name = service.name
                )
            }
        }
        else -> emptyList()
    }

    val filters = when(val s = state.serviceDomains) {
        is FeatureState.Success -> {
            val services = s.data.serviceDomains.find { it.id.toString() == state.productState.serviceDomainId }?.services ?: emptyList()
            val filters = services.find { it.id.toString() == state.productState.serviceId }?.filters ?: emptyList()
            FeatureState.Success(filters)
        }
        else -> FeatureState.Success(emptyList())
    }

    val productTypes: List<Option> = ProductTypeEnum.entries.map {
        Option(
            name = when(it) {
                ProductTypeEnum.SINGLE -> context.getString(R.string.single)
                ProductTypeEnum.PACK -> context.getString(R.string.pack)
                ProductTypeEnum.MEMBERSHIP -> context.getString(R.string.membership)
            },
            value = it.key
        )
    }

    val isLoadingServiceDomains = state.serviceDomains is FeatureState.Loading

    val isNameValid = state.validation.nameError.isNullOrBlank()
    val isDurationValid = state.validation.durationError.isNullOrBlank()
    val isPriceValid = state.validation.priceError.isNullOrBlank()
    val isDiscountValid = state.validation.discountError.isNullOrBlank()
    val isValidServiceDomainId = state.validation.serviceDomainIdError.isNullOrBlank()
    val isValidServiceId = state.validation.serviceIdError.isNullOrBlank()
    val isValidProductType = state.validation.productTypeError.isNullOrBlank()

    if(serviceDomainsOptionsList.size > 1) {
        InputSelect(
            label = stringResource(R.string.categories),
            placeholder = stringResource(R.string.pickCategory),
            options = serviceDomainsOptionsList,
            selectedOption = state.productState.serviceDomainId,
            onValueChange = { domainId -> domainId?.let { actions.onSetServiceDomainId(it) } },
            isLoading = isLoadingServiceDomains,
            isEnabled = !isLoadingServiceDomains,
            isError = state.showErrors && !isValidServiceDomainId,
            errorMessage = state.validation.serviceDomainIdError.toString()
        )

        Spacer(Modifier.height(BasePadding))
    }

    InputSelect(
        label = stringResource(R.string.service),
        placeholder = stringResource(R.string.pickService),
        options = servicesOptionList,
        selectedOption = state.productState.serviceId,
        onValueChange = { serviceId ->
            serviceId?.let { actions.onSetServiceId(it) }
        },
        isError = state.showErrors && !isValidServiceId,
        errorMessage = state.validation.serviceIdError.toString()
    )

    Spacer(Modifier.height(BasePadding))

    if(state.productState.serviceId.isNotEmpty() && filters.data.isNotEmpty()) {
        FiltersSection(
            isVisible = state.productState.serviceId.isNotEmpty(),
            filters = filters.data,
            selectedFilters = state.selectedFilters,
            isLoadingFilters = false,
            actions = state.filtersActions
        )
    }

    InputSelect(
        label = stringResource(R.string.productType),
        placeholder = stringResource(R.string.pickProductType),
        options = productTypes,
        selectedOption = state.productState.type?.key.toString(),
        onValueChange = { key ->
            val type = ProductTypeEnum.entries.find { it.key == key }
            type?.let { actions.onSetProductType(it) }
        },
        isError = state.showErrors && !isValidProductType,
        errorMessage = state.validation.productTypeError.toString()
    )

    Spacer(Modifier.height(BasePadding))

    when(state.productState.type) {
        ProductTypeEnum.PACK -> {
            Input(
                label = stringResource(R.string.sessionsCount),
                value = state.productState.sessionsCount,
                onValueChange = actions.onSetSessionsCount
            )

            Spacer(Modifier.height(BasePadding))
        }
        ProductTypeEnum.MEMBERSHIP -> {
            Input(
                label = stringResource(R.string.validityDays),
                value = state.productState.validityDays,
                onValueChange = actions.onSetValidityDays
            )

            Spacer(Modifier.height(BasePadding))
        }
        else -> Unit
    }

    Input(
        label = stringResource(R.string.name),
        value = state.productState.name,
        onValueChange = actions.onSetName,
        isError = state.showErrors && !isNameValid,
        errorMessage = state.validation.nameError.toString(),
        singleLine = false,
        maxLines = 3
    )

    Spacer(Modifier.height(BasePadding))

    Input(
        label = stringResource(R.string.description),
        value = state.productState.description,
        onValueChange = actions.onSetDescription,
        singleLine = false,
        maxLines = 5
    )

    Spacer(Modifier.height(BasePadding))

    Input(
        label = stringResource(R.string.duration),
        value = state.productState.duration,
        onValueChange = actions.onSetDuration,
        isError = state.showErrors && !isDurationValid,
        errorMessage = state.validation.durationError.toString(),
        keyboardOptions = KeyboardOptions(
            keyboardType =  KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
    )

    Spacer(Modifier.height(BasePadding))

    InputSelect(
        label = stringResource(R.string.currency),
        placeholder = stringResource(R.string.pickCurrency),
        options = listOf(Option(name = "RON", value = "1")),
        selectedOption = "1",
        onValueChange = {},
        isEnabled = false
    )

    Spacer(Modifier.height(BasePadding))

    Input(
        label = stringResource(R.string.price),
        value = state.productState.price,
        onValueChange = actions.onSetPrice,
        isError = state.showErrors && !isPriceValid,
        errorMessage = state.validation.priceError.toString(),
        keyboardOptions = KeyboardOptions(
            keyboardType =  KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
    )
    Spacer(Modifier.height(BasePadding))

    Input(
        label = stringResource(R.string.discount),
        value = state.productState.discount,
        onValueChange = actions.onSetDiscount,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Percent,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType =  KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        isError = state.showErrors && !isDiscountValid,
        errorMessage = state.validation.discountError.toString()
    )

    Spacer(Modifier.height(BasePadding))

    TextField(
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(fontWeight = FontWeight.Bold),
        value = priceWithDiscount.toString(),
        onValueChange = {},
        shape = ShapeDefaults.Medium,
        readOnly = true,
        keyboardOptions = KeyboardOptions(
            keyboardType =  KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        label = {
            Text(
                text = stringResource(R.string.priceWithDiscount),
                style = labelLarge
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SurfaceBG,
            unfocusedContainerColor = SurfaceBG,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedPlaceholderColor = Primary,
            unfocusedLabelColor = Primary
        )
    )

    Spacer(Modifier.height(BasePadding))

    CanBeBookedSection(
        canBeBooked = state.productState.canBeBooked,
        onCheckChange = actions.onSetCanBeBooked
    )
}