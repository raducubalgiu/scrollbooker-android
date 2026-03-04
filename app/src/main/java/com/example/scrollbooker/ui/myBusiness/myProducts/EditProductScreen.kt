package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.myBusiness.myProducts.components.AddProductValidation
import com.example.scrollbooker.ui.myBusiness.myProducts.components.FiltersActions
import com.example.scrollbooker.ui.myBusiness.myProducts.components.ProductInputs
import com.example.scrollbooker.ui.myBusiness.myProducts.components.ProductInputsActions
import com.example.scrollbooker.ui.myBusiness.myProducts.components.ProductInputsState
import com.example.scrollbooker.ui.theme.Divider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    myProductsViewModel: MyProductsViewModel,
    viewModel: EditProductsViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val serviceDomains by myProductsViewModel.serviceDomains.collectAsState()
    val productState by viewModel.productState.collectAsState()
    val selectedFilters by viewModel.selectedFilters.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()

    val filtersActions = rememberFiltersSectionActions(viewModel)
    val productInputsActions = rememberProductInputsActions(viewModel)

    var showErrors by rememberSaveable { mutableStateOf(false) }
    val validation by remember(productState, showErrors, context) {
        derivedStateOf {
            if(!showErrors) AddProductValidation(isValid = true)
            else productState.validate(context)
        }
    }

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
            when(loadingState) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                            .padding(horizontal = BasePadding)
                        ) {
                            ProductInputs(
                                state = ProductInputsState(
                                    showErrors = showErrors,
                                    validation = validation,
                                    filtersActions = filtersActions,
                                    productState = productState,
                                    serviceDomains = serviceDomains,
                                    selectedFilters = selectedFilters
                                ),
                                actions = productInputsActions,
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
                                        viewModel.editProduct()
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberProductInputsActions(
    viewModel: EditProductsViewModel
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
            onSetPrice = viewModel::setPrice,
            onSetDiscount = viewModel::setDiscount,
            onSetCanBeBooked = viewModel::setCanBeBooked
        )
    }
}

@Composable
private fun rememberFiltersSectionActions(
    editProductsViewModel: EditProductsViewModel
): FiltersActions {
    return remember(editProductsViewModel) {
        FiltersActions(
            singleOption = editProductsViewModel::setSingleOption,
            toggleMultiOption = editProductsViewModel::toggleMultiOption,
            setRange = editProductsViewModel::setRange,
            toggleApplicable = editProductsViewModel::setApplicable
        )
    }
}
