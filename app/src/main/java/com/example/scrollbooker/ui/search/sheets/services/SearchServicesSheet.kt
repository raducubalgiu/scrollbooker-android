package com.example.scrollbooker.ui.search.sheets.services
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithFilters
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.ui.search.SearchRequestState
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.sheets.services.components.MainFiltersFooter
import com.example.scrollbooker.ui.search.sheets.services.steps.DateTimeStep
import com.example.scrollbooker.ui.search.sheets.services.steps.MainFiltersStep
import com.example.scrollbooker.ui.search.sheets.services.steps.ServiceStep
import com.example.scrollbooker.ui.search.sheets.services.steps.servicesSheetTransitionSpec
import com.example.scrollbooker.ui.theme.Background

@Composable
fun SearchServicesSheet(
    viewModel: SearchViewModel,
    requestState: SearchRequestState,
    businessDomains: FeatureState<List<BusinessDomain>>,
    selectedServiceDomain: ServiceDomain?,
    services: List<ServiceWithFilters>?,
    isLoadingServices: Boolean,

    onClose: () -> Unit,
    onFilter: (SearchServicesFiltersSheetState) -> Unit
) {
    var state by rememberSaveable {
        mutableStateOf(
            SearchServicesFiltersSheetState(
                businessDomainId = requestState.filters.businessDomainId,
                serviceDomainId = requestState.filters.serviceDomainId,
                serviceId = requestState.filters.serviceId,
                selectedFilters = requestState.filters.selectedFilters,
                startDate = requestState.filters.startDate,
                endDate = requestState.filters.endDate,
                startTime = requestState.filters.startTime,
                endTime = requestState.filters.endTime
            )
        )
    }

    var step by remember {
        mutableStateOf(
            if(
                state.serviceDomainId != null &&
                state.serviceDomainId == selectedServiceDomain?.id
            ) ServicesSheetStep.SERVICE
            else ServicesSheetStep.MAIN_FILTERS
        )
    }

    val transitionSpec = remember { servicesSheetTransitionSpec() }

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                Column(Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = step,
                        transitionSpec = transitionSpec,
                        label = "servicesSheetStep"
                    ) { currentStep ->
                        when (currentStep) {
                            ServicesSheetStep.MAIN_FILTERS -> {
                                MainFiltersStep(
                                    onClose = onClose,
                                    businessDomains = businessDomains,
                                    selectedBusinessDomainId = state.businessDomainId,
                                    onSetSelectedBusinessDomainId = { state = state.copy(businessDomainId = it) },
                                    onSetServiceDomain = {
                                        state = state.copy(
                                            serviceDomainId = it.id,
                                            serviceId = null,
                                            selectedFilters = emptyMap()
                                        )
                                        viewModel.setSelectedServiceDomain(it)

                                        step = ServicesSheetStep.SERVICE
                                    }
                                )
                            }

                            ServicesSheetStep.SERVICE -> {
                                ServiceStep(
                                    selectedServiceDomain = selectedServiceDomain,
                                    services = services,
                                    isLoadingServices = isLoadingServices,
                                    onBack = {
                                        state = state.copy(
                                            serviceDomainId = null,
                                            serviceId = null,
                                            selectedFilters = emptyMap()
                                        )
                                        step = ServicesSheetStep.MAIN_FILTERS
                                    },
                                    selectedFilters = state.selectedFilters,
                                    selectedServiceId = state.serviceId,
                                    onChangeService = {
                                        val newServiceId = if(it?.toInt() == 0) null else it?.toIntOrNull()

                                        state = state.copy(
                                            serviceId = newServiceId,
                                            selectedFilters = emptyMap()
                                        )
                                    },
                                    onChangeFilter = { filterId, subFilterId ->
                                        state = state.copy(
                                            selectedFilters = state.selectedFilters + (filterId to subFilterId)
                                        )
                                    },
                                )
                            }

                            else -> Unit
                        }
                    }
                }

                MainFiltersFooter(
                    isClearEnabled = state.isClearAllEnabled(),
                    isConfirmEnabled = state.isConfirmEnabled(requestState),
                    onConfirm = { onFilter(state) },
                    onClear = {
                        state = state.copy(
                            serviceDomainId = null,
                            serviceId = null,
                            selectedFilters = emptyMap(),
                            startDate = null,
                            endDate = null,
                            startTime = null,
                            endTime = null
                        )
                        step = ServicesSheetStep.MAIN_FILTERS
                    },
                    onOpenDate = { if (step != ServicesSheetStep.DATE_TIME) step = ServicesSheetStep.DATE_TIME },
                    summary = state.dateTimeSummary(),
                    isActive = state.isDateActive()
                )
            }

            AnimatedContent(
                targetState = step,
                transitionSpec = transitionSpec,
                label = "servicesSheetStep"
            ) { currentStep ->
                if(currentStep == ServicesSheetStep.DATE_TIME) {
                    Box(Modifier.fillMaxSize().background(Background)) {
                        DateTimeStep(
                            state = state,
                            onBack = { step = ServicesSheetStep.MAIN_FILTERS },
                            onConfirm = {
                                state = state.copy(
                                    startDate = it.startDate,
                                    endDate = it.endDate,
                                    startTime = it.startTime,
                                    endTime = it.endTime
                                )
                                step = ServicesSheetStep.MAIN_FILTERS
                            },
                        )
                    }
                }
            }
        }
    }
}



