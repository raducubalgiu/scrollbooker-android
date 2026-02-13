package com.example.scrollbooker.ui.search.sheets.services.steps
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithFilters
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.components.SearchAdvancedFilters
import com.example.scrollbooker.ui.search.sheets.SearchSheetActions
import com.example.scrollbooker.ui.search.sheets.services.components.ServiceStepHeader

@Composable
fun ServiceStep(
    viewModel: SearchViewModel,
    services: FeatureState<List<ServiceWithFilters>>,
    selectedFilters: Map<Int, Int>,
    selectedServiceDomain: ServiceDomain?,
    selectedService: ServiceWithFilters?,
    onSetSelectedFilter: (Int, Int) -> Unit,
    onBack: () -> Unit,
    onConfirm: (Int?) -> Unit
) {
    var localSelectedServiceId by rememberSaveable {
        mutableStateOf(if(selectedService?.id != null) selectedServiceDomain?.id else 0)
    }

    LaunchedEffect(localSelectedServiceId) {
        if(localSelectedServiceId.toString().isNotEmpty()) {
            viewModel.setServiceId(localSelectedServiceId)
        }
    }

    val servicesOptions = when(val state = services) {
        is FeatureState.Success -> buildList {
            add(
                Option(
                    value = "0",
                    name = "Toate Serviciile"
                )
            )
            state.data.map { s ->
                add(
                    Option(
                        value = s.id.toString(),
                        name = s.shortName
                    )
                )
            }
        }
        else -> emptyList()
    }

    Column(modifier = Modifier.padding(vertical = SpacingS)) {
        ServiceStepHeader(
            onBack = onBack,
            serviceDomainName = selectedServiceDomain?.name,
            serviceDomainUrl = selectedServiceDomain?.thumbnailUrl
        )

        Spacer(Modifier.height(BasePadding))

        Column(modifier = Modifier
            .weight(1f)
            .padding(horizontal = BasePadding)
        ) {
            InputSelect(
                options = servicesOptions,
                selectedOption = localSelectedServiceId.toString(),
                placeholder = stringResource(R.string.chooseService),
                label = stringResource(R.string.service),
                onValueChange = { localSelectedServiceId = it?.toInt() },
                isLoading = services is FeatureState.Loading,
            )

            Spacer(Modifier.height(BasePadding))

            AnimatedVisibility(
                visible = selectedService != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                selectedService?.let {
                    SearchAdvancedFilters(
                        selectedFilters = selectedFilters,
                        onSetSelectedFilter = { filterId, subFilterId ->
                            onSetSelectedFilter(filterId, subFilterId)
                        },
                        filters = it.filters.map {
                            Filter(
                                id = it.id,
                                name = it.name,
                                singleSelect = false,
                                type = FilterTypeEnum.OPTIONS,
                                subFilters = it.subFilters.map { SubFilter(id = it.id, name = it.name) },
                                unit = ""
                            )
                        }
                    )
                }
            }
        }

        SearchSheetActions(
            onClear = { viewModel.clearServiceId() },
            onConfirm = {
                val selectedServiceId = if(localSelectedServiceId == 0) null else localSelectedServiceId
                onConfirm(selectedServiceId)
            },
            isConfirmEnabled = true,
            isClearEnabled = true
        )
    }
}