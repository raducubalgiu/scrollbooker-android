package com.example.scrollbooker.ui.myBusiness.myProducts.productState

import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.customized.placeholderActionBox.PlaceholderActionBox
import com.example.scrollbooker.components.customized.productCard.ProductVariantCard
import com.example.scrollbooker.core.extensions.formatDuration
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.FormMode
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.SelectedServiceDomainsWithServices
import com.example.scrollbooker.ui.myBusiness.myProducts.ProductViewModel
import com.example.scrollbooker.ui.myBusiness.myProducts.components.AddProductBaseInfo
import com.example.scrollbooker.ui.myBusiness.myProducts.components.AddVariantSheet
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormContent(
    formMode: FormMode,
    viewModel: ProductViewModel,
    serviceDomains: FeatureState<List<SelectedServiceDomainsWithServices>>,
    showErrors: Boolean,
    scrollState: ScrollState,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    val hasEmployees by viewModel.hasEmployees.collectAsStateWithLifecycle()
    val ownerId by viewModel.ownerId.collectAsStateWithLifecycle()
    val productState by viewModel.productState.collectAsStateWithLifecycle()
    val productValidation by viewModel.productValidation.collectAsStateWithLifecycle()

    val filters by viewModel.filters.collectAsStateWithLifecycle()
    val selectedFilters by viewModel.selectedFilters.collectAsStateWithLifecycle()
    val employees by viewModel.employees.collectAsStateWithLifecycle()

    val isLoadingServiceDomains by remember(serviceDomains) {
        derivedStateOf { serviceDomains is FeatureState.Loading }
    }

    val serviceDomainsOptionsList = when (val s = serviceDomains) {
        is FeatureState.Success -> s.data
            .filter { domain -> domain.services.any { it.isSelected } }
            .map { domain -> Option(value = domain.id.toString(), name = domain.name) }
        else -> emptyList()
    }

    val servicesOptionList = when (val s = serviceDomains) {
        is FeatureState.Success -> {
            val services = s.data.find { it.id.toString() == productState.serviceDomainId }?.services ?: emptyList()
            services.map { service -> Option(value = service.id.toString(), name = service.name) }
        }
        else -> emptyList()
    }

    if (sheetState.isVisible) {
        AddVariantSheet(
            formMode = formMode,
            sheetState = sheetState,
            hasEmployees = hasEmployees,
            employees = employees,
            ownerId = ownerId,
            defaultName = productState.name,
            onSave = { variant -> viewModel.addVariant(variant) },
            onClose = { scope.launch { sheetState.hide() } },
        )
    }

    val isVariantsEnabled = productState.name.isNotEmpty() &&
            productState.serviceDomainId.isNotEmpty() &&
            productState.serviceId.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = BasePadding)
            .verticalScroll(scrollState)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
    ) {
        AddProductBaseInfo(
            productState = productState,
            productValidation = productValidation,
            showErrors = showErrors,
            isLoadingServiceDomains = isLoadingServiceDomains,
            serviceDomainsOptionsList = serviceDomainsOptionsList,
            servicesOptionList = servicesOptionList,
            filters = filters,
            selectedFilters = selectedFilters,
            onServiceDomainChanged = { domainId -> domainId?.let { viewModel.setServiceDomainId(it) } },
            onServiceChanged = { serviceId -> serviceId?.let { viewModel.setServiceId(it) } },
            onNameChanged = { viewModel.setName(it) },
            onDescriptionChanged = { viewModel.setDescription(it) },
            onFilterOptionsChanged = { filterId, subFilterIds ->
                viewModel.setFilterOptions(filterId, subFilterIds)
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.optionsAndPrices),
                style = titleMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                enabled = isVariantsEnabled,
                onClick = {
                    focusManager.clearFocus()
                    scope.launch { sheetState.show() }
                }
            ) {
                Text(
                    text = stringResource(R.string.add),
                    style = titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(BasePadding))

        if (productState.variants.isEmpty()) {
            PlaceholderActionBox(
                description = stringResource(R.string.addOptionsAndPricesDescription),
                icon = Icons.Default.Add,
                enabled = isVariantsEnabled,
                isError = showErrors && productValidation.variantsError != null,
                errorMessage = productValidation.variantsError.orEmpty(),
                onClick = {
                    focusManager.clearFocus()
                    scope.launch { sheetState.show() }
                }
            )
        } else {
            productState.variants.forEachIndexed { index, v ->
                val cheapest = v.cheapestOffering

                ProductVariantCard(
                    name = v.name,
                    durationText = (v.duration.toIntOrNull() ?: 0).formatDuration(),
                    hasDifferentPrices = v.hasDifferentOfferings,
                    price = cheapest?.price?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    discount = cheapest?.discount?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    priceWithDiscount = cheapest?.priceWithDiscount?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    isSelected = false,
                    isSelectable = false,
                    isEditable = true,
                    isDeletable = true,
                    onEdit = { },
                    onDelete = { viewModel.removeVariant(index) }
                )

                if (index < productState.variants.lastIndex) {
                    Spacer(Modifier.height(SpacingS))
                }
            }
        }
    }
}