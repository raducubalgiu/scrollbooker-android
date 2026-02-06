package com.example.scrollbooker.ui.search.sheets.services.steps
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.ui.search.components.SearchBusinessDomainLabel
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.search.sheets.services.SearchServicesFiltersSheetState
import com.example.scrollbooker.ui.search.sheets.services.components.MainFiltersFooter
import com.example.scrollbooker.ui.search.sheets.services.components.PopularServicesList
import com.example.scrollbooker.ui.search.sheets.services.components.ServiceDomainsList
import com.example.scrollbooker.ui.search.sheets.services.components.fakeServices
import com.example.scrollbooker.ui.search.sheets.services.dateTimeSummary
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.headlineLarge
import com.example.scrollbooker.ui.theme.headlineSmall
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

data class SearchRecent(
    val id: Int,
    val name: String,
    val description: String
)

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

    val recentSearches = listOf(
        SearchRecent(
            id = 1,
            name = "Tuns",
            description = "Barbati"
        ),
        SearchRecent(
            id = 2,
            name = "Vopsit",
            description = "Barbati"
        ),
        SearchRecent(
            id = 3,
            name = "ITP",
            description = "Autoturisme"
        )
    )

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
                            Column(modifier = Modifier.fillMaxSize()) {
                                if(index == 0) {
                                    LazyVerticalGrid(
                                        contentPadding = PaddingValues(BasePadding),
                                        columns = GridCells.Fixed(3),
                                        verticalArrangement = Arrangement.spacedBy(BasePadding),
                                        horizontalArrangement = Arrangement.spacedBy(BasePadding),
                                    ) {
                                        item(span = { GridItemSpan(maxLineSpan) }) {
                                            Text(
                                                style = headlineSmall,
                                                color = OnBackground,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                text = "Cautari recente"
                                            )
                                        }

                                        items(
                                            recentSearches,
                                            span = { GridItemSpan(maxLineSpan) }
                                        ) { search ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.ic_search),
                                                    contentDescription = null
                                                )

                                                Spacer(Modifier.width(BasePadding))

                                                Column {
                                                    Text(
                                                        text = search.name,
                                                        fontWeight = FontWeight.SemiBold
                                                    )

                                                    Text(
                                                        text = search.description,
                                                        color = Color.Gray,
                                                        style = bodySmall
                                                    )
                                                }
                                            }

                                            Spacer(Modifier.height(BasePadding))
                                        }

                                        item(span = { GridItemSpan(maxLineSpan) }) {
                                            Text(
                                                style = headlineSmall,
                                                color = OnBackground,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                text = "Servicii populare"
                                            )
                                        }

                                        items(fakeServices) {
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Box(modifier = Modifier
                                                    .size(76.dp)
                                                    .clip(CircleShape)
                                                ) {
                                                    AsyncImage(
                                                        modifier = Modifier.matchParentSize(),
                                                        model = "",
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Crop,
                                                    )

                                                    Box(modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(SurfaceBG)
                                                    )
                                                }

                                                Spacer(Modifier.height(SpacingS))

                                                Text(
                                                    text = it.name,
                                                    style = titleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                }
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