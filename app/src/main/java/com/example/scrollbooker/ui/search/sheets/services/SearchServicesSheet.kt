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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.MedicalServices
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
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.sheets.SearchBusinessDomainCard
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.search.sheets.services.components.SearchServicesSheetFooter
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineLarge
import timber.log.Timber

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
    val requestState by viewModel.request.collectAsState()

    var state by remember(requestState.filters) { mutableStateOf<SearchServicesSheetFilters>(
        SearchServicesSheetFilters(
            businessDomainId = requestState.filters.businessDomainId,
            serviceId = requestState.filters.serviceId
        )
    )}

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
                    text = "Poti filtra serviciile in functie de categorie si filtrele acestora",
                )
            }

            Spacer(Modifier.height(SpacingXXL))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(verticalScroll),
            ) {
                val domains = listOf(
                    Triple(1, "Beauty", Icons.Outlined.FavoriteBorder),
                    Triple(2, "Medical", Icons.Outlined.MedicalServices),
                    Triple(3, "Auto", Icons.Outlined.Build),
                    Triple(4, "Fitness & Wellness", Icons.Outlined.FitnessCenter)
                )

                LazyRow(contentPadding = PaddingValues(horizontal = BasePadding)) {
                    itemsIndexed(domains) { index, domain ->
                        SearchBusinessDomainCard(
                            name = domain.second,
                            icon = domain.third,
                            isSelected = state.businessDomainId == domain.first,
                            onClick = {
                                state = state.copy(
                                    businessDomainId = domain.first
                                )
                            }
                        )

                        if(index <= domains.size) {
                            Spacer(Modifier.width(BasePadding))
                        }
                    }
                }

                Spacer(Modifier.height(SpacingXXL))

                Column(Modifier.padding(horizontal = BasePadding)) {
                    InputSelect(
                        options = listOf(
                            Option(value = "95", name = "Tuns"),
                            Option(value = "96", name = "Coafat"),
                            Option(value = "99", name = "Spalat")
                        ),
                        selectedOption = state.serviceId.toString(),
                        placeholder = stringResource(R.string.chooseServices),
                        onValueChange = {
                            Timber.tag("STATE").e("TRY TO ON-CHANGE $it")
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