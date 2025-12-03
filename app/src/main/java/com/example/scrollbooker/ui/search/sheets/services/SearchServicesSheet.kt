package com.example.scrollbooker.ui.search.sheets.services
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.getIcon
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.sheets.services.components.SearchBusinessDomainCard
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.search.sheets.services.components.SearchBusinessDomainsSheetList
import com.example.scrollbooker.ui.search.sheets.services.components.SearchServicesSheetFooter
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineLarge

data class SearchServicesSheetFilters(
    val businessDomainId: Int? = null,
    val serviceId: Int? = null
)

@Composable
fun SearchServicesSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit,
    onClear: () -> Unit,
    onFilter: (SearchServicesSheetFilters) -> Unit
) {
    val verticalScroll = rememberScrollState()

    val businessDomains by viewModel.businessDomains.collectAsState()
    val businessTypes by viewModel.businessTypes.collectAsState()

    val requestState by viewModel.request.collectAsState()

    var state by remember(requestState.filters) { mutableStateOf<SearchServicesSheetFilters>(
        SearchServicesSheetFilters(
            businessDomainId = requestState.filters.businessDomainId,
            serviceId = requestState.filters.serviceId
        )
    )}

    val businessTypesOptions = when(val state = businessTypes) {
        is FeatureState.Success -> state.data.map { bt ->
            Option(
                value = bt.id.toString(),
                name = bt.name
            )
        }
        else -> emptyList()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        SearchSheetsHeader(
            title = "",
            onClose = onClose
        )

        Column(modifier = Modifier
            .weight(1f)
            .padding(top = BasePadding)
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
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScroll),
            ) {
                SearchBusinessDomainsSheetList(
                    businessDomains = businessDomains,
                    selectedBusinessDomainId = state.businessDomainId,
                    onClick = {
                        state = state.copy(businessDomainId = it)
                    }
                )

                Spacer(Modifier.height(SpacingXXL))

                Column(Modifier.padding(horizontal = BasePadding)) {
                    InputSelect(
                        options = businessTypesOptions,
                        selectedOption = state.serviceId.toString(),
                        placeholder = "Alege Business-ul",
                        onValueChange = { state = state.copy(serviceId = it?.toInt()) },
                        isRequired = false,
                        isLoading = businessTypes is FeatureState.Loading,
                        isEnabled = businessTypes is FeatureState.Loading || businessTypes is FeatureState.Error
                    )

                    Spacer(Modifier.height(BasePadding))

                    InputSelect(
                        options = listOf(
                            Option(value = "95", name = "Tuns"),
                            Option(value = "96", name = "Coafat"),
                            Option(value = "99", name = "Spalat")
                        ),
                        selectedOption = state.serviceId.toString(),
                        placeholder = stringResource(R.string.chooseServices),
                        onValueChange = {
                            state = state.copy(serviceId = it?.toInt())
                        },
                        isRequired = false,
                        isLoading = false,
                       //isEnabled = false
                    )

                    Spacer(Modifier.height(BasePadding))

//                    SearchAdvancedFilters(
//                        isLoading = false,
//                        filters = listOf<String>("Tip Vehicul", "Model", "Fabricatie", "Putere (KW)"),
//                        serviceName = "Tuns"
//                    )
                }
            }
        }

        SearchServicesSheetFooter(
            onFilter = { onFilter(state) },
            onClear = onClear
        )
    }
}