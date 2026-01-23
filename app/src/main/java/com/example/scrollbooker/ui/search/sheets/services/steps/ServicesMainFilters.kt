package com.example.scrollbooker.ui.search.sheets.services.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.search.SearchRequestState
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.components.SearchAdvancedFilters
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.search.sheets.services.SearchServicesFiltersSheetState
import com.example.scrollbooker.ui.search.sheets.services.components.SearchBusinessDomainsSheetList
import com.example.scrollbooker.ui.search.sheets.services.components.SearchServicesSheetFooter
import com.example.scrollbooker.ui.search.sheets.services.dateTimeSummary
import com.example.scrollbooker.ui.search.sheets.services.hasActiveFilters
import com.example.scrollbooker.ui.search.sheets.services.hasChangesComparedTo
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun ServicesMainFilters(
    viewModel: SearchViewModel,
    requestState: SearchRequestState,
    state: SearchServicesFiltersSheetState,
    onOpenDate: () -> Unit,
    onFilter: (SearchServicesFiltersSheetState) -> Unit,
    onClose: () -> Unit,
    onClear: () -> Unit
) {
    val verticalScroll = rememberScrollState()

    val businessDomains by viewModel.businessDomains.collectAsState()
    val serviceDomains by viewModel.serviceDomains.collectAsState()
    val services by viewModel.services.collectAsState()
    val serviceFilters by viewModel.filters.collectAsState()

    val selectedFilters by viewModel.selectedFilters.collectAsState()

    val serviceDomainsOptions = when(val state = serviceDomains) {
        is FeatureState.Success -> state.data.map { bt ->
            Option(
                value = bt.id.toString(),
                name = bt.name
            )
        }
        else -> emptyList()
    }

    val servicesOptions = when(val state = services) {
        is FeatureState.Success -> state.data.map { s ->
            Option(
                value = s.id.toString(),
                name = s.displayName,
                description = s.description
            )
        }
        else -> emptyList()
    }

    val buttonSummary = state.dateTimeSummary()
    val isActive = buttonSummary != null

    val requestBusinessDomainId = requestState.filters.businessDomainId

    var selectedBusinessDomainId by rememberSaveable(requestBusinessDomainId) {
        mutableStateOf<Int?>(requestBusinessDomainId)
    }

    val hasDomainChanged = selectedBusinessDomainId != requestBusinessDomainId
    val hasOtherFiltersChanged = state.hasChangesComparedTo(requestState.filters)

    val isClearEnabled = selectedBusinessDomainId != null || state.hasActiveFilters()
    val isConfirmEnabled = hasDomainChanged || hasOtherFiltersChanged

    Column(Modifier.fillMaxSize()) {
        SearchSheetsHeader(
            title = "",
            onClose = onClose
        )

        Column(modifier = Modifier
            .weight(1f)
            .padding(top = BasePadding)
            .verticalScroll(verticalScroll)
        ) {
            Column(Modifier.padding(horizontal = BasePadding)) {
                Text(
                    style = headlineLarge,
                    color = OnBackground,
                    fontWeight = FontWeight.ExtraBold,
                    text = stringResource(R.string.services)
                )
                Spacer(Modifier.height(SpacingXXS))
                Text(
                    style = bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    text = stringResource(R.string.youCanFilterServicesBasedOnCategoryTypeAndFilter),
                )
            }

            Spacer(Modifier.height(SpacingXXL))

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SearchBusinessDomainsSheetList(
                    businessDomains = businessDomains,
                    selectedBusinessDomainId = selectedBusinessDomainId,
                    onClick = {
                        selectedBusinessDomainId = it
                        viewModel.onSheetBusinessDomainSelected(it)
                    }
                )

                Spacer(Modifier.height(SpacingXL))

                AnimatedVisibility(
                    visible = selectedBusinessDomainId != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(Modifier.padding(horizontal = BasePadding)) {
                        InputSelect(
                            options = serviceDomainsOptions,
                            selectedOption = state.serviceDomainId.toString(),
                            placeholder = "Alege Categoria",
                            isEnabled = selectedBusinessDomainId != null,
                            onValueChange = {
                                viewModel.setServiceDomainId(it?.toInt())
                            },
                            isLoading = serviceDomains is FeatureState.Loading,
                        )

                        Spacer(Modifier.height(BasePadding))

                        InputSelect(
                            options = servicesOptions,
                            selectedOption = state.serviceId.toString(),
                            placeholder = stringResource(R.string.chooseServices),
                            isEnabled = state.serviceDomainId != null,
                            onValueChange = { viewModel.setServiceId(it?.toInt()) },
                            isLoading = services is FeatureState.Loading,
                        )

                        Spacer(Modifier.height(BasePadding))

                        when(val filters = serviceFilters) {
                            is FeatureState.Loading -> {
                                repeat(3) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(rememberShimmerBrush())
                                    )
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                            is FeatureState.Success -> {
                                SearchAdvancedFilters(
                                    selectedFilters = selectedFilters,
                                    onSetSelectedFilter = { filterId, subFilterId ->
                                        viewModel.setSelectedFilter(filterId, subFilterId)
                                    },
                                    filters = filters.data
                                )
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }

        SearchServicesSheetFooter(
            isClearEnabled = isClearEnabled,
            isConfirmEnabled = true,
            onConfirm = { onFilter(state) },
            onClear = {
                selectedBusinessDomainId = null
                onClear()
            },
            onOpenDate = onOpenDate,
            summary = buttonSummary,
            isActive = isActive
        )
    }
}