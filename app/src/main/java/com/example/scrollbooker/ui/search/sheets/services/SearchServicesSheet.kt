package com.example.scrollbooker.ui.search.sheets.services
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
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
import kotlin.toString

@Composable
fun SearchServicesSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit,
    onFilter: (SearchServicesFiltersSheetState) -> Unit
) {
    val requestState by viewModel.request.collectAsState()
    val state by viewModel.servicesSheetFilters.collectAsState()

    val businessDomains by viewModel.businessDomains.collectAsState()
    val serviceDomains by viewModel.serviceDomains.collectAsState()
    val services by viewModel.services.collectAsState()
    val serviceFilters by viewModel.filters.collectAsState()
    val selectedFilters by viewModel.selectedFilters.collectAsState()

    val requestBusinessDomainId = requestState.filters.businessDomainId

    var selectedBusinessDomainId by rememberSaveable(requestBusinessDomainId) {
        mutableStateOf<Int?>(requestBusinessDomainId)
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
                        onFilter = onFilter,
                        onClear = {
                            selectedBusinessDomainId = null
                            viewModel.clearServicesFiltersSheet()
                        },
                        onClose = onClose,
                        businessDomains = businessDomains,
                        serviceDomains = serviceDomains,
                        selectedBusinessDomainId = selectedBusinessDomainId,
                        onSetSelectedBusinessDomainId = {
                            selectedBusinessDomainId = it
                            viewModel.onSheetBusinessDomainSelected(it)
                        },
                        onSetServiceDomainId = {
                            viewModel.setServiceDomainId(it)
                            step = ServicesSheetStep.SERVICE
                        }
                    )
                }

                ServicesSheetStep.DATE_TIME -> {
                    DateTimeStep(
                        state = state,
                        onBack = { step = ServicesSheetStep.MAIN_FILTERS },
                        onConfirm = {
                            viewModel.setDateTime(
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
                        services = services,
                        serviceFilters = serviceFilters,
                        selectedFilters = selectedFilters,
                        selectedOption = state.serviceId.toString(),
                        onServiceChanged = { viewModel.setServiceId(it) },
                        onSetSelectedFilter = { filterId, subFilterId ->
                            viewModel.setSelectedFilter(filterId, subFilterId)
                        },
                        onBack = { step = ServicesSheetStep.MAIN_FILTERS },
                        onConfirm = { step = ServicesSheetStep.MAIN_FILTERS }
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
        slideInHorizontally(animationSpec = tween(250)) { fullWidth -> fullWidth }
    } else {
        slideInHorizontally(animationSpec = tween(250)) { fullWidth -> -fullWidth }
    } + fadeIn(animationSpec = tween(250))

    val exit = if (isForward) {
        slideOutHorizontally(animationSpec = tween(250)) { fullWidth -> -fullWidth }
    } else {
        slideOutHorizontally(animationSpec = tween(250)) { fullWidth -> fullWidth }
    } + fadeOut(animationSpec = tween(250))

    enter togetherWith exit using SizeTransform(clip = clip)
}



