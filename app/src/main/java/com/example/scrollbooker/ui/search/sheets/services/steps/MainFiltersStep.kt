package com.example.scrollbooker.ui.search.sheets.services.steps
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
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.entity.search.domain.model.RecentSearch
import com.example.scrollbooker.ui.search.components.SearchBusinessDomainLabel
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.search.sheets.services.components.ServiceDomainsList
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.headlineLarge
import kotlinx.coroutines.launch

@Composable
fun MainFiltersStep(
    recentSearches: FeatureState<List<RecentSearch>>,
    businessDomains: FeatureState<List<BusinessDomain>>,
    selectedBusinessDomainId: Int?,
    onSetSelectedBusinessDomainId: (Int?) -> Unit,
    onSetServiceDomain: (ServiceDomain) -> Unit,
    onClose: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val recentSearchesList = (recentSearches as? FeatureState.Success)?.data ?: emptyList()

    Column(Modifier.fillMaxSize()) {
        SearchSheetsHeader(onClose = onClose)

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
                                serviceDomains = emptyList(),
                                businessTypes = emptyList()
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
                            Column(modifier = Modifier.fillMaxSize()) {
                                if(index == 0) {
                                    RecentSearchesList(recentSearchesList)
                                } else {
                                    ServiceDomainsList(
                                        serviceDomains = list[index].serviceDomains,
                                        onSetServiceDomain = onSetServiceDomain
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}