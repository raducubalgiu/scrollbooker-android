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
import com.example.scrollbooker.ui.search.sheets.services.steps.DateTimeStep
import com.example.scrollbooker.ui.search.sheets.services.steps.MainFiltersStep
import com.example.scrollbooker.ui.search.sheets.services.steps.ServiceStep
import com.example.scrollbooker.ui.search.sheets.services.steps.ServiceStepState

@Composable
fun SearchServicesSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit,
    onFilter: (SearchServicesFiltersSheetState) -> Unit
) {
    val requestState by viewModel.request.collectAsState()
    val businessDomains by viewModel.businessDomains.collectAsState()

    var state by rememberSaveable {
        mutableStateOf(SearchServicesFiltersSheetState(
            businessDomainId = requestState.filters.businessDomainId,
            serviceDomainId = requestState.filters.serviceDomainId,
            serviceId = requestState.filters.serviceId,
            startDate = requestState.filters.startDate,
            endDate = requestState.filters.endDate,
            startTime = requestState.filters.startTime,
            endTime = requestState.filters.endTime
        ))
    }

    var step by remember { mutableStateOf(ServicesSheetStep.MAIN_FILTERS) }
    val transitionSpec = remember { servicesSheetTransitionSpec() }

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        AnimatedContent(
            targetState = step,
            transitionSpec = transitionSpec,
            label = "servicesSheetStep"
        ) { currentStep ->
            when (currentStep) {
                ServicesSheetStep.MAIN_FILTERS -> {
                    MainFiltersStep(
                        state = state,
                        onOpenDate = {
                            if (step == ServicesSheetStep.MAIN_FILTERS) step = ServicesSheetStep.DATE_TIME
                            else step = ServicesSheetStep.MAIN_FILTERS
                        },
                        onFilter = { onFilter(state) },
                        onClear = {},
                        onClearServiceId = {},
                        onClose = onClose,
                        businessDomains = businessDomains,
                        selectedBusinessDomainId = state.businessDomainId,
                        onSetSelectedBusinessDomainId = { state = state.copy(businessDomainId = it) },
                        onSetServiceDomain = {
                            state = state.copy(serviceDomainId = it.id)
                            viewModel.setSelectedServiceDomain(it)

                            step = ServicesSheetStep.SERVICE
                        }
                    )
                }

                ServicesSheetStep.DATE_TIME -> {
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

                ServicesSheetStep.SERVICE -> {
                    ServiceStep(
                        viewModel = viewModel,
                        serviceStepState = ServiceStepState(
                            serviceId = state.serviceId,
                            selectedFilters = state.selectedFilters
                        ),
                        onBack = { step = ServicesSheetStep.MAIN_FILTERS },
                        onConfirm = {
                            state = state.copy(
                                serviceId = it.serviceId,
                                selectedFilters = it.selectedFilters
                            )

                            step = ServicesSheetStep.MAIN_FILTERS
                        }
                    )
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



