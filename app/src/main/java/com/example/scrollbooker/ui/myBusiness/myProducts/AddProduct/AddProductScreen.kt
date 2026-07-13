package com.example.scrollbooker.ui.myBusiness.myProducts.AddProduct
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.accordion.Accordion
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.inputs.BasicInput
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.myBusiness.myProducts.MyProductsViewModel
import com.example.scrollbooker.ui.myBusiness.myProducts.components.FiltersActions
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.titleLarge

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
    val onSetCanBeBooked: (Boolean) -> Unit,
)

@Composable
fun AddProductScreen(
    myProductsViewModel: MyProductsViewModel,
    viewModel: AddProductsViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val employeesState by viewModel.employees.collectAsStateWithLifecycle()
    val serviceDomains by myProductsViewModel.selectedServices.collectAsStateWithLifecycle()

    val productState by viewModel.productState.collectAsStateWithLifecycle()
    val selectedFilters by viewModel.selectedFilters.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    var showErrors by rememberSaveable { mutableStateOf(false) }
    val validation by remember(productState, showErrors, context) {
        derivedStateOf {
            if(!showErrors) AddProductValidation(isValid = true)
            else productState.validate(context)
        }
    }

    val isLoadingServiceDomains by remember(serviceDomains) {
        derivedStateOf { serviceDomains is FeatureState.Loading }
    }

    val serviceDomainsOptionsList = when(val s = serviceDomains) {
        is FeatureState.Success -> s.data.map { domain ->
            Option(
                value = domain.id.toString(),
                name = domain.name
            )
        }
        else -> emptyList()
    }

    val servicesOptionList = when(val s = serviceDomains) {
        is FeatureState.Success -> {
            val services = s.data.find {
                it.id.toString() == productState.serviceDomainId }?.services ?: emptyList()
            services.map { service ->
                Option(
                    value = service.id.toString(),
                    name = service.name
                )
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
                onBack=onBack,
                title = stringResource(R.string.addNewProduct)
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier.fillMaxWidth().padding(BasePadding),
                onClick = {}
            ) {
                Text(
                    text = "Salveaza"
                )
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
                onValueChange = { domainId -> domainId?.let { viewModel.setServiceDomainId(it) } },
                isLoading = isLoadingServiceDomains,
                isEnabled = !isLoadingServiceDomains,
                //isError = state.showErrors && !isValidServiceDomainId,
                //errorMessage = state.validation.serviceDomainIdError.toString()
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
                //isError = state.showErrors && !isValidServiceId,
                //errorMessage = state.validation.serviceIdError.toString()
            )

            Spacer(Modifier.height(BasePadding))

//            if(state.serviceId.isNotEmpty() && filters.data.isNotEmpty()) {
//                FiltersSection(
//                    isVisible = state.serviceId.isNotEmpty(),
//                    filters = filters.data,
//                    selectedFilters = state.selectedFilters,
//                    isLoadingFilters = false,
//                    actions = state.filtersActions
//                )
//            }
//
//            Spacer(Modifier.height(BasePadding))

            Input(
                label = stringResource(R.string.name),
                value = productState.name,
                onValueChange = { viewModel.setName(it) },
                //isError = state.showErrors && !isNameValid,
                //errorMessage = state.validation.nameError.toString(),
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
                // ROW NOU PENTRU DURATĂ
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Spațiu între texte și input
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.duration),
                            fontWeight = FontWeight.SemiBold,
                            style = labelLarge
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Reprezintă durata efectivă a acestui serviciu.",
                            style = bodySmall,
                            color = Color.Gray // Culoare secundară pentru a nu încărca vizual
                        )
                    }

                    // În dreapta: Inputul de durată (îi dăm o lățime fixă sau maximă ca să nu ocupe tot ecranul)
                    BasicInput(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.width(100.dp), // Ajustează lățimea în funcție de design (ex: pentru numere)
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Background,
                            unfocusedContainerColor = Background,
                            focusedTextColor = OnBackground,
                            unfocusedTextColor = OnBackground,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )
                }

                Spacer(Modifier.height(BasePadding))

                Text(
                    text = "Preturi per angajat",
                    fontWeight = FontWeight.SemiBold,
                    style = labelLarge
                )

                Spacer(Modifier.height(BasePadding))

                when(employeesState) {
                    is FeatureState.Loading -> {
                        Text(
                            text = "Loading employees...",
                            style = labelLarge,
                            color = Color.Gray
                        )
                    }
                    is FeatureState.Error -> {
                        Text(
                            text = "Error loading employees",
                            style = labelLarge,
                            color = Color.Red
                        )
                    }
                    is FeatureState.Success -> {
                        val employees = (employeesState as FeatureState.Success).data
                        if (employees.isEmpty()) {
                            Text(
                                text = "No employees found",
                                style = labelLarge,
                                color = Color.Gray
                            )
                        } else {
                            employees.forEachIndexed { index, employee ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if(index == employees.size - 1) Error else Color.Green)
                                        )

                                        Avatar(
                                            url = employee.avatar ?: "",
                                            size = 30.dp
                                        )

                                        Text(
                                            text = employee.fullName,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Text(text = "200 RON")
                                }

                                Spacer(Modifier.height(SpacingM))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberProductInputsActions(
    viewModel: AddProductsViewModel
): ProductInputsActions {
    return remember(viewModel) {
        ProductInputsActions(
            onSetServiceDomainId = viewModel::setServiceDomainId,
            onSetServiceId = viewModel::setServiceId,
            onSetProductType = viewModel::setType,
            onSetSessionsCount = viewModel::setSessionsCount,
            onSetValidityDays = viewModel::setValidityDays,
            onSetName = viewModel::setName,
            onSetDescription = viewModel::setDescription,
            onSetDuration = viewModel::setDuration,
//            onSetPrice = viewModel::setPrice,
//            onSetDiscount = viewModel::setDiscount,
            onSetCanBeBooked = viewModel::setCanBeBooked
        )
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