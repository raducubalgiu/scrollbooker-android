package com.example.scrollbooker.ui.myBusiness.myProducts.AddProduct
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.customized.placeholderActionBox.PlaceholderActionBox
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.myBusiness.myProducts.MyProductsViewModel
import com.example.scrollbooker.ui.myBusiness.myProducts.components.FiltersSection
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun AddProductScreen(
    myProductsViewModel: MyProductsViewModel,
    viewModel: AddProductsViewModel,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val productState by viewModel.productState.collectAsStateWithLifecycle()
    val productValidation by viewModel.productValidation.collectAsStateWithLifecycle()

    val serviceDomains by myProductsViewModel.selectedServices.collectAsStateWithLifecycle()
    val filters by viewModel.filters.collectAsStateWithLifecycle()
    val selectedFilters by viewModel.selectedFilters.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    var showErrors by rememberSaveable { mutableStateOf(false) }

    val isLoadingServiceDomains by remember(serviceDomains) {
        derivedStateOf { serviceDomains is FeatureState.Loading }
    }

    val serviceDomainsOptionsList = when(val s = serviceDomains) {
        is FeatureState.Success -> s.data.map { domain ->
            Option(value = domain.id.toString(), name = domain.name)
        }
        else -> emptyList()
    }

    val servicesOptionList = when(val s = serviceDomains) {
        is FeatureState.Success -> {
            val services = s.data.find { it.id.toString() == productState.serviceDomainId }?.services ?: emptyList()
            services.map { service ->
                Option(value = service.id.toString(), name = service.name)
            }
        }
        else -> emptyList()
    }

    LaunchedEffect(Unit) {
        viewModel.createSuccessEvent.collect {
            myProductsViewModel.refreshProducts()
            onBack()
        }
    }

    Scaffold(
        topBar = {
            Header(
                onBack = onBack,
                title = stringResource(R.string.addNewProduct)
            )
        },
        bottomBar = {
            MainButton(
                modifier = Modifier
                    .padding(horizontal = BasePadding)
                    .navigationBarsPadding(),
                title = stringResource(R.string.save),
                isLoading = isSaving,
                enabled = !isSaving,
                onClick = {
                    showErrors = true
                    viewModel.createProduct()
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = BasePadding)
            .verticalScroll(scrollState)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
        ) {
            Text(
                text = stringResource(R.string.baseInfo),
                style = titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(BasePadding))

            InputSelect(
                label = stringResource(R.string.categories),
                placeholder = stringResource(R.string.pickCategory),
                options = serviceDomainsOptionsList,
                selectedOption = productState.serviceDomainId,
                onValueChange = { domainId ->
                    domainId?.let {
                        viewModel.setServiceDomainId(it)
                        viewModel.setCurrencyId("1")
                    }
                },
                isLoading = isLoadingServiceDomains,
                isEnabled = !isLoadingServiceDomains,
                isError = showErrors && productValidation.serviceDomainIdError != null,
                errorMessage = productValidation.serviceDomainIdError.orEmpty()
            )

            Spacer(Modifier.height(BasePadding))

            InputSelect(
                label = stringResource(R.string.service),
                placeholder = stringResource(R.string.pickService),
                options = servicesOptionList,
                selectedOption = productState.serviceId,
                onValueChange = { serviceId -> serviceId?.let { viewModel.setServiceId(it) } },
                isError = showErrors && productValidation.serviceIdError != null,
                errorMessage = productValidation.serviceIdError.orEmpty()
            )

            Spacer(Modifier.height(BasePadding))

            Input(
                label = stringResource(R.string.serviceName),
                value = productState.name,
                onValueChange = { viewModel.setName(it) },
                isError = showErrors && productValidation.nameError != null,
                errorMessage = productValidation.nameError.orEmpty(),
                singleLine = false,
                maxLines = 3
            )

            Spacer(Modifier.height(BasePadding))

            Input(
                label = stringResource(R.string.description),
                value = productState.description,
                onValueChange = { viewModel.setDescription(it) },
                singleLine = false,
                maxLines = 5
            )

            Spacer(Modifier.height(BasePadding))

            when (val state = filters) {
                is FeatureState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(Modifier.height(BasePadding))
                }
                is FeatureState.Error -> {
                    Text(text = "Eroare la încărcarea filtrelor", color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(BasePadding))
                }
                is FeatureState.Success -> {
                    val filtersList = state.data

                    FiltersSection(
                        isVisible = productState.serviceId.isNotEmpty() && filtersList.isNotEmpty(),
                        filters = filtersList,
                        selectedFilters = selectedFilters,
                        isLoadingFilters = false,
                        onToggleOption = { filterId, subFilterId, isSingleSelect ->
                            viewModel.toggleFilterOption(filterId, subFilterId, isSingleSelect)
                        }
                    )
                }
            }

            Text(
                text = stringResource(R.string.variantAndPrices),
                style = titleLarge,
                fontWeight = FontWeight.Bold
            )

            PlaceholderActionBox(
                description = stringResource(R.string.addVariantAndPricesDescription),
                icon = Icons.Default.Add,
                onClick = {}
            )
        }
    }
}