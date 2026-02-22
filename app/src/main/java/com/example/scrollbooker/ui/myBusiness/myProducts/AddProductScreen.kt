package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.myBusiness.myProducts.components.AddProductValidation
import com.example.scrollbooker.ui.myBusiness.myProducts.components.CanBeBookedSection
import com.example.scrollbooker.ui.myBusiness.myProducts.components.FiltersActions
import com.example.scrollbooker.ui.myBusiness.myProducts.components.FiltersSection
import com.example.scrollbooker.ui.myBusiness.myProducts.components.calculatePriceWithDiscount
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.labelLarge

@Composable
fun AddProductScreen(
    myProductsViewModel: MyProductsViewModel,
    viewModel: AddProductsViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val actions = rememberFiltersSectionActions(viewModel)

    val serviceDomains by myProductsViewModel.serviceDomains.collectAsState()
    val productState by viewModel.productState.collectAsState()
    val selectedFilters by viewModel.selectedFilters.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val priceWithDiscount by remember(productState.price, productState.discount) {
        derivedStateOf {
            calculatePriceWithDiscount(
                price = productState.price,
                discountPercent = productState.discount
            ).toPlainString()
        }
    }

    val serviceDomainsOptionsList = when(val state = serviceDomains) {
        is FeatureState.Success -> state.data.map { domain ->
            Option(
                value = domain.id.toString(),
                name = domain.name
            )
        }
        else -> emptyList()
    }

    val servicesOptionList = when(val state = serviceDomains) {
        is FeatureState.Success -> {
            val services = state.data.find { it.id.toString() == productState.serviceDomainId }?.services ?: emptyList()
            services.map { service ->
                Option(
                    value = service.id.toString(),
                    name = service.name
                )
            }
        }
        else -> emptyList()
    }

    val filters = when(val state = serviceDomains) {
        is FeatureState.Success -> {
            val services = state.data.find { it.id.toString() == productState.serviceDomainId }?.services ?: emptyList()
            val filters = services.find { it.id.toString() == productState.serviceId }?.filters ?: emptyList()
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

    var showErrors by rememberSaveable { mutableStateOf(false) }
    val validation by remember(productState, showErrors, context) {
        derivedStateOf {
            if(!showErrors) AddProductValidation(isValid = true)
            else productState.validate(context)
        }
    }

    val isLoadingServiceDomains = serviceDomains is FeatureState.Loading
    val isErrorServiceDomains = serviceDomains is FeatureState.Error

    val isNameValid = validation.nameError.isNullOrBlank()
    val isDurationValid = validation.durationError.isNullOrBlank()
    val isPriceValid = validation.priceError.isNullOrBlank()
    val isDiscountValid = validation.discountError.isNullOrBlank()
    val isValidServiceDomainId = validation.serviceDomainIdError.isNullOrBlank()
    val isValidServiceId = validation.serviceIdError.isNullOrBlank()

    Scaffold(
        topBar = {
            Header(
                onBack=onBack,
                title = stringResource(R.string.addNewProduct)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = BasePadding)
                ) {
                    InputSelect(
                        label = stringResource(R.string.categories),
                        placeholder = stringResource(R.string.pickCategory),
                        options = serviceDomainsOptionsList,
                        selectedOption = productState.serviceDomainId,
                        onValueChange = { domainId ->
                            domainId?.let { viewModel.setServiceDomainId(it) }
                        },
                        isLoading = isLoadingServiceDomains,
                        isEnabled = !isErrorServiceDomains && !isLoadingServiceDomains,
                        isError = showErrors && !isValidServiceDomainId,
                        errorMessage = validation.serviceDomainIdError.toString()
                    )

                    Spacer(Modifier.height(BasePadding))

                    InputSelect(
                        label = stringResource(R.string.service),
                        placeholder = stringResource(R.string.pickService),
                        options = servicesOptionList,
                        selectedOption = productState.serviceId,
                        onValueChange = { serviceId ->
                            serviceId?.let {
                                viewModel.resetFilters()
                                viewModel.setServiceId(it)
                            }
                        },
                        isError = showErrors && !isValidServiceId,
                        errorMessage = validation.serviceIdError.toString()
                    )

                    Spacer(Modifier.height(BasePadding))

                    if(productState.serviceId.isNotEmpty()) {
                        FiltersSection(
                            isVisible = productState.serviceId.isNotEmpty(),
                            filters = filters.data,
                            selectedFilters = selectedFilters,
                            isLoadingFilters = false,
                            actions = actions
                        )
                    }

                    InputSelect(
                        label = stringResource(R.string.productType),
                        placeholder = stringResource(R.string.pickProductType),
                        options = productTypes,
                        selectedOption = productState.type?.key.toString(),
                        onValueChange = { key ->
                            val type = ProductTypeEnum.entries.find { it.key == key }
                            type?.let { viewModel.setType(it) }
                        },
                    )

                    Spacer(Modifier.height(BasePadding))

                    when(productState.type) {
                        ProductTypeEnum.PACK -> {
                            Input(
                                label = stringResource(R.string.sessionsCount),
                                value = productState.sessionsCount,
                                onValueChange = viewModel::setSessionsCount,
                            )

                            Spacer(Modifier.height(BasePadding))
                        }
                        ProductTypeEnum.MEMBERSHIP -> {
                            Input(
                                label = stringResource(R.string.validityDays),
                                value = productState.validityDays,
                                onValueChange = viewModel::setValidityDays,
                            )

                            Spacer(Modifier.height(BasePadding))
                        }
                        else -> Unit
                    }

                    Input(
                        label = stringResource(R.string.name),
                        value = productState.name,
                        onValueChange = viewModel::setName,
                        isError = showErrors && !isNameValid,
                        errorMessage = validation.nameError.toString(),
                        singleLine = false,
                        maxLines = 3
                    )

                    Spacer(Modifier.height(BasePadding))

                    Input(
                        label = stringResource(R.string.description),
                        value = productState.description,
                        onValueChange = viewModel::setDescription,
                        singleLine = false,
                        maxLines = 5
                    )

                    Spacer(Modifier.height(BasePadding))

                    Input(
                        label = stringResource(R.string.duration),
                        value = productState.duration,
                        onValueChange = viewModel::setDuration,
                        isError = showErrors && !isDurationValid,
                        errorMessage = validation.durationError.toString(),
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
                        value = productState.price,
                        onValueChange = { viewModel.setPrice(it) },
                        isError = showErrors && !isPriceValid,
                        errorMessage = validation.priceError.toString(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType =  KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                    )
                    Spacer(Modifier.height(BasePadding))

                    Input(
                        label = stringResource(R.string.discount),
                        value = productState.discount,
                        onValueChange = { viewModel.setDiscount(it) },
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
                        isError = showErrors && !isDiscountValid,
                        errorMessage = validation.discountError.toString()
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
                        canBeBooked = productState.canBeBooked,
                        onCheckChange = { viewModel.setCanBeBooked(it) }
                    )
                }

                Column {
                    HorizontalDivider(color = Divider, thickness = 0.5.dp)

                    MainButton(
                        modifier = Modifier.padding(BasePadding),
                        isLoading = isSaving,
                        enabled = !isSaving,
                        title = stringResource(R.string.save),
                        onClick = {
                            showErrors = true

                            if(validation.isValid) {
                                viewModel.createProduct()
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberFiltersSectionActions(
    addProductsViewModel: AddProductsViewModel
): FiltersActions {
    return remember(addProductsViewModel) {
        FiltersActions(
            singleOption = addProductsViewModel::setSingleOption,
            toggleMultiOption = addProductsViewModel::toggleMultiOption,
            setRange = addProductsViewModel::setRange,
            toggleApplicable = addProductsViewModel::setApplicable
        )
    }
}