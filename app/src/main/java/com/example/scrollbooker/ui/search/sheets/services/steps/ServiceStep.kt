package com.example.scrollbooker.ui.search.sheets.services.steps
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.components.SearchAdvancedFilters
import com.example.scrollbooker.ui.search.sheets.services.components.ServiceStepHeader

@Composable
fun ServiceStep(
    viewModel: SearchViewModel,
    selectedFilters: Map<Int, Int>,
    selectedServiceId: Int?,
    onChangeFilter: (Int, Int) -> Unit,
    onChangeService: (String?) -> Unit,
    onBack: () -> Unit
) {
    val selectedServiceDomain by viewModel.selectedServiceDomain.collectAsState()
    val servicesState by viewModel.services.collectAsState()

    val services = (servicesState as? FeatureState.Success)?.data
    val isLoadingServices = servicesState is FeatureState.Loading

    val selectedService = services?.firstOrNull() { it.id == selectedServiceId }

    val servicesOptions = when(val state = servicesState) {
        is FeatureState.Success -> state.data.map { s ->
            Option(
                value = s.id.toString(),
                name = s.shortName
            )
        }
        else -> emptyList()
    }

    Column(modifier = Modifier.padding(vertical = SpacingS)) {
        ServiceStepHeader(
            onBack = onBack,
            serviceDomainName = selectedServiceDomain?.name,
            serviceDomainUrl = selectedServiceDomain?.url
        )

        Spacer(Modifier.height(BasePadding))

        Column(modifier = Modifier
            .weight(1f)
            .padding(horizontal = BasePadding)
        ) {
            InputSelect(
                options = servicesOptions,
                selectedOption = selectedServiceId.toString(),
                placeholder = stringResource(R.string.chooseService),
                label = stringResource(R.string.service),
                onValueChange = { onChangeService(it) },
                isLoading = isLoadingServices,
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
                        onSetSelectedFilter = onChangeFilter,
                        filters = it.filters.filter { it.type != FilterTypeEnum.RANGE }
                    )
                }
            }
        }
    }
}