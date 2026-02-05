package com.example.scrollbooker.ui.search.sheets.services.steps
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.ui.search.components.SearchBusinessDomainLabel
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.search.sheets.services.SearchServicesFiltersSheetState
import com.example.scrollbooker.ui.search.sheets.services.components.MainFiltersFooter
import com.example.scrollbooker.ui.search.sheets.services.components.PopularServicesList
import com.example.scrollbooker.ui.search.sheets.services.components.ServiceDomainsList
import com.example.scrollbooker.ui.search.sheets.services.dateTimeSummary
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.headlineLarge
import kotlinx.coroutines.launch

@Composable
fun MainFiltersStep(
    state: SearchServicesFiltersSheetState,
    businessDomains: FeatureState<List<BusinessDomain>>,
    selectedBusinessDomainId: Int?,
    selectedService: Service?,
    onSetSelectedBusinessDomainId: (Int?) -> Unit,
    onSetServiceDomainId: (Int) -> Unit,

    onOpenDate: () -> Unit,
    onFilter: (SearchServicesFiltersSheetState) -> Unit,
    onClose: () -> Unit,
    onClear: () -> Unit,
    onClearServiceId: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val buttonSummary = state.dateTimeSummary()
    val isActive = buttonSummary != null

//    val hasDomainChanged = selectedBusinessDomainId != requestBusinessDomainId
//    val hasOtherFiltersChanged = state.hasChangesComparedTo(requestState.filters)

//    val isClearEnabled = selectedBusinessDomainId != null || state.hasActiveFilters()
//    val isConfirmEnabled = hasDomainChanged || hasOtherFiltersChanged

    Column(Modifier.fillMaxSize()) {
        SearchSheetsHeader(
            title = "",
            onClose = onClose
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.padding(horizontal = BasePadding),
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(R.string.services)
            )

            Spacer(Modifier.height(BasePadding))

            Column(modifier = Modifier.fillMaxSize()) {
                when(val bDomains = businessDomains) {
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Loading -> Unit
                    is FeatureState.Success -> {
                        val list = listOf<BusinessDomain>(
                            BusinessDomain(
                                id = 0,
                                name = stringResource(R.string.all),
                                shortName = stringResource(R.string.all),
                                serviceDomains = emptyList()
                            )
                        ) + bDomains.data

                        val initialIndex = selectedBusinessDomainId
                            ?.let { id ->
                                list.indexOfFirst { it.id == id }
                                    .takeIf { it != -1 }
                            } ?: 0

                        val pagerState = rememberPagerState(initialPage = initialIndex) { list.size }

                        LazyRow(
                            modifier = Modifier.padding(bottom = SpacingS),
                            contentPadding = PaddingValues(horizontal = BasePadding)
                        ) {
                            itemsIndexed(list) { index, bd ->
                                val isSelected = selectedBusinessDomainId == bd.id ||
                                        (selectedBusinessDomainId == null && bd.id == 0)

                                SearchBusinessDomainLabel(
                                    onClick = {
                                        onSetSelectedBusinessDomainId(if(bd.id == 0) null else bd.id)
                                        scope.launch { pagerState.animateScrollToPage(index) }
                                    },
                                    isSelected = isSelected,
                                    name = bd.shortName,
                                    shadowElevation = 0.dp,
                                    inactiveContainerColor = SurfaceBG,
                                    inactiveContentColor = OnSurfaceBG,
                                    paddingValues = PaddingValues(
                                        horizontal = 22.dp,
                                        vertical = 10.dp
                                    ),
                                    shape = ShapeDefaults.Medium
                                )
                            }
                        }

                        HorizontalPager(
                            state = pagerState,
                            overscrollEffect = null,
                            userScrollEnabled = false
                        ) { index ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                if(index == 0) PopularServicesList()
                                else {
                                    ServiceDomainsList(
                                        serviceDomains = list[index].serviceDomains,
                                        onSetServiceDomainId = onSetServiceDomainId
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        MainFiltersFooter(
            isClearEnabled = true,
            isConfirmEnabled = true,
            selectedService = selectedService,
            onConfirm = { onFilter(state) },
            onClear = onClear,
            onClearServiceId = onClearServiceId,
            onOpenDate = onOpenDate,
            summary = buttonSummary,
            isActive = isActive
        )
    }
}