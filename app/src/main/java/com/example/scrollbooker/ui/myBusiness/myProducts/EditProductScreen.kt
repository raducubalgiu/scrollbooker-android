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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.core.util.checkMinMax
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.labelLarge
import java.math.BigDecimal
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    viewModel: MyProductsViewModel,
    productId: Int,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val productState by viewModel.product.collectAsState()

    val servicesState by viewModel.servicesState.collectAsState()
    val currenciesState by viewModel.currenciesState.collectAsState()
    val filtersState by viewModel.filtersState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val selectedFilters by viewModel.selectedFilterOptions

    LaunchedEffect(Unit) {
        viewModel.loadProduct(productId)
        viewModel.loadCurrencies()
        viewModel.loadFilters()
    }

    when (val product = productState) {
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Success -> {
            var name by rememberSaveable { mutableStateOf(product.data.name.toString()) }
            var serviceId by rememberSaveable { mutableStateOf(product.data.serviceId.toString()) }
            var description by rememberSaveable { mutableStateOf(product.data.description.toString()) }
            var duration by rememberSaveable { mutableStateOf(product.data.duration.toString()) }
            var currencyId by rememberSaveable { mutableStateOf(product.data.currencyId.toString()) }
            var price by rememberSaveable { mutableStateOf(product.data.price.toString()) }
            var discount by rememberSaveable { mutableStateOf(product.data.discount.toString()) }

            val priceDecimal = price.toBigDecimalOrNull() ?: BigDecimal.ZERO
            val discountDecimal = discount.toBigDecimalOrNull() ?: BigDecimal.ZERO

            val priceWithDiscount = priceDecimal.multiply(
                BigDecimal.ONE.subtract(discountDecimal.divide(BigDecimal(100)))
            ).setScale(2, RoundingMode.HALF_UP).toString()

            val scrollState = rememberScrollState()

            val servicesOptionList = when (val state = servicesState) {
                is FeatureState.Success -> state.data.map { service ->
                    Option(
                        value = service.id.toString(),
                        name = service.name
                    )
                }

                else -> emptyList()
            }

            val currenciesOptionList = when (val state = currenciesState) {
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

            val validation by remember(name, description, price, discount, serviceId, currencyId) {
                derivedStateOf {
                    val checkName = checkLength(context, name, minLength = 3, maxLength = 100)
                    val isNameValid = checkName.isNullOrBlank()

                    val checkDescription =
                        checkLength(context, description, minLength = 3, maxLength = 255)
                    val isDescriptionValid = checkDescription.isNullOrBlank()

                    val checkPrice = checkMinMax(context, price, min = 10)
                    val isPriceValid = checkPrice.isNullOrBlank()

                    val checkDiscount = checkMinMax(context, discount, max = 90)
                    val isDiscountValid = checkDiscount.isNullOrBlank()

                    val isValid = serviceId.isNotEmpty() &&
                            currencyId.isNotEmpty() &&
                            isNameValid &&
                            isDescriptionValid &&
                            isPriceValid &&
                            isDiscountValid

                    ProductValidationResult(
                        isValid = isValid,
                        isNameValid = isNameValid,
                        nameError = checkName.toString(),
                        isDescriptionValid = isDescriptionValid,
                        descriptionError = checkDescription.toString(),
                        isPriceValid = isPriceValid,
                        priceError = checkPrice.toString(),
                        isDiscountValid = isDiscountValid,
                        discountError = checkDiscount.toString()
                    )
                }
            }

            Layout(
                onBack = {
                    viewModel.removeSelectedFilters()
                    onBack()
                },
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                bottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
                            ),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(scrollState)
                                .padding(horizontal = BasePadding)
                        ) {
                            InputSelect(
                                label = stringResource(R.string.service),
                                placeholder = stringResource(R.string.pickService),
                                options = servicesOptionList,
                                selectedOption = serviceId,
                                onValueChange = {
                                    focusManager.clearFocus()
                                    serviceId = it.toString()
                                },
                                isLoading = isLoadingServices,
                                isEnabled = !isErrorServices && !isLoadingServices
                            )
                            Spacer(Modifier.height(BasePadding))

                            Input(
                                value = name,
                                onValueChange = { name = it },
                                label = stringResource(R.string.name),
                                isError = !validation.isNameValid,
                                errorMessage = validation.nameError.toString()
                            )

                            Spacer(Modifier.height(BasePadding))

                            when (val filters = filtersState) {
                                is FeatureState.Success -> {
                                    filters.data.map { filter ->
                                        InputSelect(
                                            label = filter.name,
                                            placeholder = "Selecteaza filtrul",
                                            options = filter.subFilters.map {
                                                Option(value = it.id.toString(), name = it.name)
                                            },
                                            selectedOption = selectedFilters[filter.id.toString()]
                                                ?: "",
                                            onValueChange = { value ->
                                                focusManager.clearFocus()
                                                viewModel.updateSelectedFilter(
                                                    filter.id.toString(),
                                                    value = value.toString()
                                                )
                                            },
                                            isLoading = isLoadingFilters,
                                            isEnabled = !isErrorFilters && !isLoadingFilters
                                        )

                                        Spacer(Modifier.height(BasePadding))
                                    }
                                }

                                else -> Unit
                            }

                            Input(
                                value = description,
                                onValueChange = { description = it },
                                label = stringResource(R.string.description),
                                isError = !validation.isDescriptionValid,
                                errorMessage = validation.descriptionError.toString(),
                                singleLine = false
                            )
                            Spacer(Modifier.height(BasePadding))

                            Input(
                                value = duration,
                                onValueChange = { duration = it },
                                label = stringResource(R.string.duration)
                            )

                            Spacer(Modifier.height(BasePadding))

                            InputSelect(
                                label = stringResource(R.string.currency),
                                placeholder = stringResource(R.string.pickCurrency),
                                options = currenciesOptionList,
                                selectedOption = currencyId,
                                onValueChange = {
                                    focusManager.clearFocus()
                                    currencyId = it.toString()
                                },
                                isLoading = isLoadingCurrencies,
                                isEnabled = !isErrorCurrencies && !isLoadingCurrencies
                            )

                            Spacer(Modifier.height(BasePadding))

                            Input(
                                value = price.toString(),
                                onValueChange = { price = it },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                label = stringResource(R.string.price),
                                isError = !validation.isPriceValid,
                                errorMessage = validation.priceError.toString()
                            )
                            Spacer(Modifier.height(BasePadding))

                            Input(
                                value = discount.toString(),
                                onValueChange = { discount = it },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
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
                                isError = !validation.isDiscountValid,
                                errorMessage = validation.discountError.toString()
                            )
                            Spacer(Modifier.height(BasePadding))

                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(
                                    fontWeight = FontWeight.Bold
                                ),
                                value = priceWithDiscount.toString(),
                                onValueChange = {},
                                shape = ShapeDefaults.Medium,
                                readOnly = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
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
                        }

                        Spacer(Modifier.height(BasePadding))

                        Column {
                            HorizontalDivider(color = Divider, thickness = 0.5.dp)
                            MainButton(
                                modifier = Modifier
                                    .padding(top = BasePadding)
                                    .padding(horizontal = BasePadding),
                                isLoading = isSaving,
                                enabled = validation.isValid && !isSaving,
                                title = stringResource(R.string.save),
                                onClick = {
                                    viewModel.createProduct(
                                        name = name,
                                        description = description,
                                        price = price,
                                        priceWithDiscount = priceWithDiscount,
                                        discount = discount,
                                        duration = duration,
                                        serviceId = serviceId,
                                        currencyId = currencyId,
                                        canBeBooked = true
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
