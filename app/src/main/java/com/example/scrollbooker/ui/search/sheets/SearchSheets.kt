package com.example.scrollbooker.ui.search.sheets

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithFilters
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.ui.search.SearchRequestState
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.sheets.filters.SearchFiltersSheet
import com.example.scrollbooker.ui.search.sheets.services.SearchServicesSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSheets(
    viewModel: SearchViewModel,
    sheetState: SheetState,
    requestState: SearchRequestState,
    businessDomains: FeatureState<List<BusinessDomain>>,
    selectedServiceDomain: ServiceDomain?,
    services: List<ServiceWithFilters>?,
    isLoadingServices: Boolean,
    sheetAction: SearchSheetActionEnum,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Sheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onClose = onClose,
    ) {
        when(sheetAction) {
            SearchSheetActionEnum.OPEN_SERVICES -> {
                SearchServicesSheet(
                    viewModel = viewModel,
                    requestState = requestState,
                    businessDomains = businessDomains,
                    selectedServiceDomain = selectedServiceDomain,
                    services = services,
                    isLoadingServices = isLoadingServices,
                    onClose = {
                        scope.launch {
                            sheetState.hide()
                            onClose()
                        }
                    },
                    onFilter = {
                        scope.launch {
                            sheetState.hide()

                            if(!sheetState.isVisible) {
                                viewModel.setFiltersFromServicesSheet(it)
                                onClose()
                            }
                        }
                    }
                )
            }
            SearchSheetActionEnum.OPEN_FILTERS -> {
                SearchFiltersSheet(
                    viewModel = viewModel,
                    onClose = {
                        scope.launch {
                            sheetState.hide()
                            onClose()
                        }
                    },
                    onFilter = {
                        scope.launch {
                            sheetState.hide()

                            if(!sheetState.isVisible) {
                                viewModel.setFiltersFromFiltersSheet(it)
                                onClose()
                            }
                        }
                    },
                )
            }
            SearchSheetActionEnum.NONE -> Unit
        }
    }
}