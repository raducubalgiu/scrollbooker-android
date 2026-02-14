package com.example.scrollbooker.ui.search.sheets.services
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.sheets.services.components.MainFiltersFooter
import com.example.scrollbooker.ui.search.sheets.services.steps.DateTimeStep
import com.example.scrollbooker.ui.search.sheets.services.steps.MainFiltersStep
import com.example.scrollbooker.ui.search.sheets.services.steps.ServiceStep
import com.example.scrollbooker.ui.theme.Background

@Composable
fun SearchServicesSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit,
    onFilter: (SearchServicesFiltersSheetState) -> Unit
) {
    val requestState by viewModel.request.collectAsState()
    val businessDomains by viewModel.businessDomains.collectAsState()
    val selectedServiceDomain by viewModel.selectedServiceDomain.collectAsState()

    var state by rememberSaveable {
        mutableStateOf(SearchServicesFiltersSheetState(
            businessDomainId = requestState.filters.businessDomainId,
            serviceDomainId = requestState.filters.serviceDomainId,
            serviceId = requestState.filters.serviceId,
            selectedFilters = requestState.filters.selectedFilters,
            startDate = requestState.filters.startDate,
            endDate = requestState.filters.endDate,
            startTime = requestState.filters.startTime,
            endTime = requestState.filters.endTime
        ))
    }

    var step by remember {
        mutableStateOf(
            if(state.serviceDomainId != null && selectedServiceDomain != null) ServicesSheetStep.SERVICE
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
                                    viewModel = viewModel,
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
                                        state = state.copy(
                                            serviceId = it?.toIntOrNull(),
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
                    onConfirm = { onFilter(state) },
                    onClear = { state = SearchServicesFiltersSheetState() },
                    onOpenDate = { if (step != ServicesSheetStep.DATE_TIME) step = ServicesSheetStep.DATE_TIME },
                    summary = state.dateTimeSummary(),
                    isActive = state.dateTimeSummary() != null
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
                            step = step,
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

private fun servicesSheetTransitionSpec(
    mainStep: ServicesSheetStep = ServicesSheetStep.MAIN_FILTERS,
    clip: Boolean = false,
): AnimatedContentTransitionScope<ServicesSheetStep>.() -> ContentTransform = {
    val isForward = targetState != mainStep

    val enter = if (isForward) {
        slideInHorizontally { fullWidth -> fullWidth }
    } else {
        slideInHorizontally { fullWidth -> -fullWidth }
    } + fadeIn()

    val exit = if (isForward) {
        slideOutHorizontally { fullWidth -> -fullWidth }
    } else {
        slideOutHorizontally { fullWidth -> fullWidth }
    } + fadeOut()

    enter togetherWith exit using SizeTransform(clip = clip)
}



