package com.example.scrollbooker.ui.search.sheets.services.steps
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithFilters
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.ui.search.components.SearchAdvancedFilters
import com.example.scrollbooker.ui.search.sheets.services.components.ServiceStepHeader

@Composable
fun ServiceStep(
    selectedServiceDomain: ServiceDomain?,
    selectedFilters: Map<Int, Int>,
    selectedServiceId: Int?,
    services: List<ServiceWithFilters>?,
    isLoadingServices: Boolean,
    onChangeFilter: (Int, Int) -> Unit,
    onChangeService: (String?) -> Unit,
    onBack: () -> Unit
) {
    val selectedService = services?.firstOrNull() { it.id == selectedServiceId }

    val options = buildList {
        add(
            Option(
                value = "0",
                name = "Toate serviciile"
            )
        )
        services?.map {
            add(
                Option(
                    value = it.id.toString(),
                    name = it.name
                )
            )
        }
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
                options = if(services != null) options else emptyList(),
                selectedOption = selectedServiceId?.toString() ?: "0",
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