package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.myBusiness.myProducts.components.CanBeBookedSection
import com.example.scrollbooker.ui.myBusiness.myProducts.components.FiltersActions
import com.example.scrollbooker.ui.myBusiness.myProducts.components.FiltersSection
import com.example.scrollbooker.ui.myBusiness.myProducts.components.FiltersSkeleton
import com.example.scrollbooker.ui.myBusiness.myProducts.components.calculatePriceWithDiscount
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.labelLarge

data class ProductValidationResult(
    val isValid: Boolean,
    val isNameValid: Boolean,
    val nameError: String?,
    val isDescriptionValid: Boolean,
    val descriptionError: String?,
    val isPriceValid: Boolean,
    val priceError: String?,
    val isDiscountValid: Boolean,
    val discountError: String?
)

@Composable
fun AddProductScreen(
    viewModel: AddProductsViewModel,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val actions = rememberFiltersSectionActions(viewModel)

    val servicesState by viewModel.servicesState.collectAsState()
    val currenciesState by viewModel.currenciesState.collectAsState()
    val filtersState by viewModel.filtersState.collectAsState()

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

    val servicesOptionList = when(val state = servicesState) {
        is FeatureState.Success -> state.data.map { service ->
            Option(
                value = service.id.toString(),
                name = service.displayName
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

    val isLoadingServices = servicesState is FeatureState.Loading
    val isErrorServices = servicesState is FeatureState.Error

    val isLoadingCurrencies = currenciesState is FeatureState.Loading
    val isErrorCurrencies = currenciesState is FeatureState.Error

    val isLoadingFilters = filtersState is FeatureState.Loading
    val isErrorFilters = filtersState is FeatureState.Error

    Layout(
        onBack = onBack,
        headerTitle = stringResource(R.string.addNewProduct),
        enablePaddingH = false
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = BasePadding)
                ) {
                    InputSelect(
                        label = stringResource(R.string.service),
                        placeholder = stringResource(R.string.pickService),
                        options = servicesOptionList,
                        selectedOption = productState.serviceId,
                        onValueChange = { serviceId ->
                            serviceId?.let {
                                viewModel.resetFilters()
                                viewModel.setServiceId(it)
                                viewModel.loadFilters(it.toInt())
                            }
                        },
                        isLoading = isLoadingServices,
                        isEnabled = !isErrorServices && !isLoadingServices
                    )

                    Spacer(Modifier.height(BasePadding))

                    when(val filters = filtersState) {
                        is FeatureState.Loading -> FiltersSkeleton()
                        is FeatureState.Success -> {
                            FiltersSection(
                                isVisible = productState.serviceId.isNotEmpty(),
                                filters = filters.data,
                                selectedFilters = selectedFilters,
                                isLoadingFilters = isLoadingFilters,
                                actions = actions
                            )
                        }
                        else -> Unit
                    }



                    Input(
                        value = productState.name,
                        onValueChange = { viewModel.setName(it) },
                        label = stringResource(R.string.name),
                        //isError = !validation.isNameValid,
                        //errorMessage = validation.nameError.toString(),
                        singleLine = false,
                        maxLines = 3
                    )

                    Spacer(Modifier.height(BasePadding))

                    Input(
                        value = productState.description,
                        onValueChange = { viewModel.setDescription(it) },
                        label = stringResource(R.string.description),
                        //isError = !validation.isDescriptionValid,
                        //errorMessage = validation.descriptionError.toString(),
                        singleLine = false,
                        maxLines = 5

                    )

                    Spacer(Modifier.height(BasePadding))

                    Input(
                        value = productState.duration,
                        onValueChange = { viewModel.setDuration(it) },
                        label = stringResource(R.string.duration)
                    )

                    Spacer(Modifier.height(BasePadding))

                    InputSelect(
                        label = stringResource(R.string.currency),
                        placeholder = stringResource(R.string.pickCurrency),
                        options = currenciesOptionList,
                        selectedOption = productState.currencyId,
                        onValueChange = { currencyId ->
                            focusManager.clearFocus()

                            currencyId?.let {
                                viewModel.setCurrencyId(it)
                            }
                        },
                        isLoading = isLoadingCurrencies,
                        isEnabled = !isErrorCurrencies && !isLoadingCurrencies
                    )

                    Spacer(Modifier.height(BasePadding))

                    Input(
                        value = productState.price,
                        onValueChange = { viewModel.setPrice(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType =  KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        label = stringResource(R.string.price),
                        //isError = !validation.isPriceValid,
                        //errorMessage = validation.priceError.toString()
                    )
                    Spacer(Modifier.height(BasePadding))

                    Input(
                        value = productState.discount,
                        onValueChange = { viewModel.setDiscount(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType =  KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        label = stringResource(R.string.discount),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Percent,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        //isError = !validation.isDiscountValid,
                        //errorMessage = validation.discountError.toString()
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
                        onClick = { viewModel.createProduct() },
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