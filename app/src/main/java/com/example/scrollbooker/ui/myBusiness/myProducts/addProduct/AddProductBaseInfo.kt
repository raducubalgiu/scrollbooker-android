package com.example.scrollbooker.ui.myBusiness.myProducts.addProduct

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.ui.myBusiness.myProducts.components.FiltersSection
import com.example.scrollbooker.ui.myBusiness.myProducts.components.FiltersSkeleton
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun AddProductBaseInfo(
    productState: ProductState,
    productValidation: AddProductValidation,
    showErrors: Boolean,

    isLoadingServiceDomains: Boolean,
    serviceDomainsOptionsList: List<Option>,
    servicesOptionList: List<Option>,
    filters: FeatureState<List<Filter>>,
    selectedFilters:  Map<Int, Set<Int>>,

    onServiceDomainChanged: (String?) -> Unit,
    onServiceChanged: (String?) -> Unit,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onToggleFilterOption: (filterId: Int, subFilterId: Int, isSingleSelect: Boolean) -> Unit
) {
    Text(
        text = stringResource(R.string.baseInfo),
        style = titleMedium,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(BasePadding))

    InputSelect(
        label = stringResource(R.string.categories),
        placeholder = stringResource(R.string.pickCategory),
        options = serviceDomainsOptionsList,
        selectedOption = productState.serviceDomainId,
        onValueChange = onServiceDomainChanged,
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
        onValueChange = onServiceChanged,
        isError = showErrors && productValidation.serviceIdError != null,
        errorMessage = productValidation.serviceIdError.orEmpty()
    )

    Spacer(Modifier.height(BasePadding))

    Input(
        label = stringResource(R.string.serviceName),
        value = productState.name,
        onValueChange = onNameChanged,
        isError = showErrors && productValidation.nameError != null,
        errorMessage = productValidation.nameError.orEmpty(),
        singleLine = false,
        maxLines = 3
    )

    Spacer(Modifier.height(BasePadding))

    Input(
        label = stringResource(R.string.description),
        value = productState.description,
        onValueChange = onDescriptionChanged,
        singleLine = false,
        maxLines = 5
    )

    Spacer(Modifier.height(BasePadding))

    when (val state = filters) {
        is FeatureState.Loading -> FiltersSkeleton()
        is FeatureState.Error -> Text(text = "Eroare la încărcarea filtrelor", color = Error)
        is FeatureState.Success -> {
            val filtersList = state.data

            FiltersSection(
                isVisible = productState.serviceId.isNotEmpty() && filtersList.isNotEmpty(),
                filters = filtersList,
                selectedFilters = selectedFilters,
                isLoadingFilters = false,
                onToggleOption = onToggleFilterOption
            )
        }
    }
}