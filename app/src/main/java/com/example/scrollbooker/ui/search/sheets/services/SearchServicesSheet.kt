package com.example.scrollbooker.ui.search.sheets.services
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.sheets.services.steps.DateTimeSelection
import com.example.scrollbooker.ui.search.sheets.services.steps.ServicesDateTimeFilters
import com.example.scrollbooker.ui.search.sheets.services.steps.ServicesMainFilters

@Composable
fun SearchServicesSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit,
    onFilter: (SearchServicesFiltersSheetState) -> Unit
) {
    val requestState by viewModel.request.collectAsState()
    val filters = requestState.filters

    LaunchedEffect(Unit) {
        if(filters.businessDomainId != null) {
            viewModel.syncBusinessDomain(filters.businessDomainId)
        }
    }

    val state by viewModel.servicesSheetFilters.collectAsState()
    var step by remember { mutableStateOf(ServicesSheetFiltersStepEnum.MAIN_FILTERS) }

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                if (initialState == ServicesSheetFiltersStepEnum.MAIN_FILTERS &&
                    targetState == ServicesSheetFiltersStepEnum.DATE_TIME
                ) {
                    slideInHorizontally(animationSpec = tween(250)) {
                        fullWidth -> fullWidth
                    } + fadeIn() togetherWith
                    slideOutHorizontally(animationSpec = tween(250)) {
                        fullWidth -> -fullWidth
                    } + fadeOut()
                } else {
                    slideInHorizontally(animationSpec = tween(250)) {
                        fullWidth -> -fullWidth
                    } + fadeIn() togetherWith
                    slideOutHorizontally(animationSpec = tween(250)) {
                        fullWidth -> fullWidth
                    } + fadeOut()
                }.using(
                    SizeTransform(clip = false)
                )
            },
            label = "servicesSheetStep"
        ) { currentStep ->
            when (currentStep) {
                ServicesSheetFiltersStepEnum.MAIN_FILTERS -> {
                    ServicesMainFilters(
                        viewModel = viewModel,
                        state = state,
                        onOpenDate = {
                            if(step == ServicesSheetFiltersStepEnum.MAIN_FILTERS) {
                                step = ServicesSheetFiltersStepEnum.DATE_TIME
                            } else {
                                step = ServicesSheetFiltersStepEnum.MAIN_FILTERS
                            }
                        },
                        onFilter = onFilter,
                        onClear = {}
                    )
                }

                ServicesSheetFiltersStepEnum.DATE_TIME -> {
                    val initialDateTime = DateTimeSelection()

                    ServicesDateTimeFilters(
                        initialSelection = initialDateTime,
                        onCancel = { step = ServicesSheetFiltersStepEnum.MAIN_FILTERS },
                        onConfirm = {}
                    )
                }
            }
        }
    }
}


