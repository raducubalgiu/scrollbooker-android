package com.example.scrollbooker.ui.myBusiness.myProducts.AddProduct
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.accordion.Accordion
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.inputs.BasicInput
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.myBusiness.myProducts.MyProductsViewModel
import com.example.scrollbooker.ui.myBusiness.myProducts.components.FiltersSection
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun AddProductScreen(
    myProductsViewModel: MyProductsViewModel,
    viewModel: AddProductsViewModel,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val serviceDomains by myProductsViewModel.selectedServices.collectAsStateWithLifecycle()
    val productState by viewModel.productState.collectAsStateWithLifecycle()
    val filters by viewModel.filters.collectAsStateWithLifecycle()
    val selectedFilters by viewModel.selectedFilters.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    val productValidation by viewModel.productValidation.collectAsStateWithLifecycle()
    val variantsValidation by viewModel.variantsValidation.collectAsStateWithLifecycle()

    var showErrors by rememberSaveable { mutableStateOf(false) }

    val currentVariantIndex = 0
    val currentOfferingIndex = 0

    val currentVariant = productState.variants.getOrNull(currentVariantIndex)
    val variantValidation = variantsValidation.getOrNull(currentVariantIndex)
    val offeringValidation = variantValidation?.offerings?.getOrNull(currentOfferingIndex)

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
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(BasePadding),
                enabled = !isSaving,
                onClick = {
                    showErrors = true
                    viewModel.createProduct()
                }
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "Salvează")
                }
            }
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
                text = "Informatii de baza",
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
                onValueChange = { serviceId ->
                    serviceId?.let { viewModel.setServiceId(it) }
                },
                isError = showErrors && productValidation.serviceIdError != null,
                errorMessage = productValidation.serviceIdError.orEmpty()
            )

            Spacer(Modifier.height(BasePadding))

            Input(
                label = stringResource(R.string.name),
                value = productState.name,
                onValueChange = {
                    viewModel.setName(it)
                    if (currentVariant != null) {
                        viewModel.updateVariant(
                            currentVariantIndex,
                            name = it,
                            duration = currentVariant.duration
                        )
                    }
                },
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

            Spacer(Modifier.height(BasePadding))


            Text(
                text = "Variante si preturi",
                style = titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(BasePadding))

            Accordion(
                title = productState.name.ifEmpty { "--" },
                isExpanded = true,
                onSetExpanded = {},
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = stringResource(R.string.duration), fontWeight = FontWeight.SemiBold, style = labelLarge)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Reprezintă durata efectivă a acestui serviciu.", style = bodySmall, color = Color.Gray)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        BasicInput(
                            value = currentVariant?.duration.orEmpty(),
                            onValueChange = { viewModel.updateVariantDuration(it) },
                            isError = showErrors && variantValidation?.durationError != null,
                            modifier = Modifier.width(100.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Background, unfocusedContainerColor = Background,
                                focusedTextColor = OnBackground, unfocusedTextColor = OnBackground,
                                focusedIndicatorColor = if (showErrors && variantValidation?.durationError != null) MaterialTheme.colorScheme.error else Color.Transparent,
                                unfocusedIndicatorColor = if (showErrors && variantValidation?.durationError != null) MaterialTheme.colorScheme.error else Color.Transparent,
                            )
                        )
                        if (showErrors && variantValidation?.durationError != null) {
                            Text(text = variantValidation.durationError!!, color = MaterialTheme.colorScheme.error, style = bodySmall)
                        }
                    }
                }

                Spacer(Modifier.height(BasePadding))

                Text(
                    text = "Prețuri per angajat",
                    fontWeight = FontWeight.SemiBold,
                    style = labelLarge
                )

                Spacer(Modifier.height(BasePadding))

                currentVariant?.offerings?.forEachIndexed { index, offeringState ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                                //.padding(vertical = 4.bindDp()),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Comutator (Checkbox) în stânga: Activează prețul pentru acest angajat
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Checkbox(
                                    checked = offeringState.isSelected,
                                    onCheckedChange = { isChecked ->
                                        viewModel.toggleOfferingSelection(index, isChecked)
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = offeringState.fullName, // Afișăm numele complet al angajatului
                                    style = bodyMedium,
                                    fontWeight = if (offeringState.isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }

                            // Input de preț în dreapta (Este activat doar dacă angajatul e bifat)
                            Column(horizontalAlignment = Alignment.End) {
                                BasicInput(
                                    value = offeringState.price,
                                    //enabled = offeringState.isSelected, // Blocat dacă nu e bifat angajatul
                                    onValueChange = { newPrice ->
                                        viewModel.updateOfferingPrices(
                                            offeringIndex = index,
                                            price = newPrice,
                                            discount = offeringState.discount,
                                            priceWithDiscount = newPrice
                                        )
                                    },
                                    isError = showErrors && offeringValidation?.priceError != null,
                                    modifier = Modifier.width(100.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Background,
                                        unfocusedContainerColor = if (offeringState.isSelected) Background else Background.copy(alpha = 0.5f),
                                        focusedTextColor = OnBackground,
                                        unfocusedTextColor = if (offeringState.isSelected) OnBackground else Color.Gray,
                                        //focusedIndicatorColor = if (showErrors && offeringValidation?.priceError != null) MaterialTheme.colorScheme.error else Color.Transparent,
                                        //unfocusedIndicatorColor = if (showErrors && offeringValidation?.priceError != null) MaterialTheme.colorScheme.error else Color.Transparent,
                                    )
                                )
                            }
                        }

//                        // Dacă utilizatorul a bifat angajatul, dar a lăsat prețul gol la salvare, afișăm eroarea dedesubt
//                        if (showErrors && offeringValidation?.priceError != null && offeringState.isSelected) {
//                            Text(
//                                text = offeringValidation.priceError!!,
//                                color = MaterialTheme.colorScheme.error,
//                                style = bodySmall,
//                                modifier = Modifier.padding(start = 48.dp, bottom = 4.dp)
//                            )
//                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = Divider.copy(alpha = 0.3f),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}