package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.components.core.headers.HeaderEdit
import com.example.scrollbooker.components.customized.ProductCard.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun MyProductsScreen(
    viewModel: MyProductsViewModel,
    onBack: () -> Unit,
    onAddProduct: () -> Unit
) {
    val serviceDomains by viewModel.serviceDomains.collectAsState()
    val products by viewModel.productSections.collectAsState()

    val scope = rememberCoroutineScope()

    Layout(
        headerTitle = stringResource(R.string.myProducts),
        header = {
            HeaderEdit(
                title = stringResource(R.string.myProducts),
                onAction = onAddProduct,
                actionTitle = stringResource(R.string.add),
                onBack = onBack
            )
        },
        onBack = onBack,
        enablePaddingH = false
    ) {
        when(val sDomains = serviceDomains) {
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                val serviceDomains = sDomains.data

                val tabsState by viewModel.tabsState.collectAsState()

                val domainPagerState = rememberPagerState(
                    initialPage = tabsState.selectedDomainIndex,
                    pageCount = { serviceDomains.size }
                )

                LaunchedEffect(domainPagerState.currentPage) {
                    if (tabsState.selectedDomainIndex != domainPagerState.currentPage) {
                        viewModel.selectDomain(domainPagerState.currentPage)
                    }
                }

                Column {
                    ScrollableTabRow(
                        selectedTabIndex = tabsState.selectedDomainIndex,
                        edgePadding = BasePadding
                    ) {
                        serviceDomains.forEachIndexed { index, domain ->
                            Tab(
                                selected = tabsState.selectedDomainIndex == index,
                                onClick = {
                                    scope.launch { domainPagerState.animateScrollToPage(index) }
                                    viewModel.selectDomain(index)
                                },
                                text = { Text(domain.name) }
                            )
                        }
                    }

                    HorizontalPager(
                        state = domainPagerState,
                        modifier = Modifier.weight(1f)
                    ) { domainIndex ->

                        val services = serviceDomains[domainIndex].services

                        val selectedServiceIndex =
                            viewModel.getSelectedService(domainIndex)

                        val servicePagerState = rememberPagerState(
                            initialPage = selectedServiceIndex,
                            pageCount = { services.size }
                        )

                        LaunchedEffect(servicePagerState.currentPage) {
                            viewModel.selectService(
                                domainIndex,
                                servicePagerState.currentPage
                            )
                        }

                        Column {
                            ScrollableTabRow(
                                selectedTabIndex = servicePagerState.currentPage,
                                edgePadding = BasePadding
                            ) {
                                services.forEachIndexed { index, service ->
                                    Tab(
                                        selected = servicePagerState.currentPage == index,
                                        onClick = {
                                            scope.launch { servicePagerState.animateScrollToPage(index) }
                                            viewModel.selectService(domainIndex, index)
                                        },
                                        text = { Text(service.name) }
                                    )
                                }
                            }

                    HorizontalPager(
                        state = servicePagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { serviceIndex ->
                        when(val prod = products) {
                            is FeatureState.Error -> ErrorScreen()
                            is FeatureState.Loading -> LoadingScreen()
                            is FeatureState.Success -> {
                                if (prod.data.isEmpty()) {
                                    ErrorScreen()
                                } else {
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = BasePadding)
                                    ) {
                                        items(prod.data) { section ->
                                            Column(
                                                modifier = Modifier.padding(vertical = BasePadding)
                                            ) {
                                                Text(
                                                    text = section.subFilter?.name ?: "",
                                                    style = titleMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    modifier = Modifier.padding(bottom = BasePadding)
                                                )

                                                section.products.forEach { product ->
                                                    ProductCard(
                                                        product = product,
                                                        isSelected = false,
                                                        onSelect = {}
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
                    }
                }
            }
        }
    }
}