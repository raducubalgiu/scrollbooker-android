package com.example.scrollbooker.ui.search.sheets.services.steps

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
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.components.SearchAdvancedFilters
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.search.sheets.services.SearchServicesFiltersSheetState
import com.example.scrollbooker.ui.search.sheets.services.components.SearchBusinessDomainsSheetList
import com.example.scrollbooker.ui.search.sheets.services.components.SearchServicesSheetFooter
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun ServicesMainFilters(
    viewModel: SearchViewModel,
    state: SearchServicesFiltersSheetState,
    onOpenDate: () -> Unit,
    onFilter: (SearchServicesFiltersSheetState) -> Unit,
    onClose: () -> Unit,
    onClear: () -> Unit
) {
    val verticalScroll = rememberScrollState()
    val businessDomains by viewModel.businessDomains.collectAsState()
    val businessTypes by viewModel.businessTypes.collectAsState()
    val services by viewModel.services.collectAsState()
    val serviceFilters by viewModel.filters.collectAsState()

    val businessTypesOptions = when(val state = businessTypes) {
        is FeatureState.Success -> state.data.map { bt ->
            Option(
                value = bt.id.toString(),
                name = bt.name
            )
        }
        else -> emptyList()
    }

    val servicesOptions = when(val state = services) {
        is FeatureState.Success -> state.data.map { bt ->
            Option(
                value = bt.id.toString(),
                name = bt.name
            )
        }
        else -> emptyList()
    }

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
                    selectedBusinessDomainId = state.businessDomainId,
                    onClick = { viewModel.onSheetBusinessDomainSelected(it) }
                )

                Spacer(Modifier.height(SpacingXXL))

                Column(Modifier.padding(horizontal = BasePadding)) {
                    InputSelect(
                        options = businessTypesOptions,
                        selectedOption = state.businessTypeId.toString(),
                        placeholder = "Alege Business-ul",
                        onValueChange = {
                            viewModel.setBusinessTypeId(it?.toInt())
                        },
                        isLoading = businessTypes is FeatureState.Loading,
                    )

                    Spacer(Modifier.height(BasePadding))

                    InputSelect(
                        options = servicesOptions,
                        selectedOption = state.serviceId.toString(),
                        placeholder = stringResource(R.string.chooseServices),
                        onValueChange = {
                            viewModel.setServiceId(it?.toInt())
                        },
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
                                selectedSubFilterIds = state.subFilterIds,
                                onSubFilterAppend = {
                                    viewModel.setSubFilterId(it)
                                },
                                filters = filters.data
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }

        SearchServicesSheetFooter(
            onConfirm = { onFilter(state) },
            onClear = onClear,
            onOpenDate = onOpenDate
        )
    }
}