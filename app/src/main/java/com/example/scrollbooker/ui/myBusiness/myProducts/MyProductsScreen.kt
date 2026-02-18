package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.components.core.headers.HeaderEdit
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.headlineMedium

@Composable
fun MyProductsScreen(
    viewModel: MyProductsViewModel,
    onBack: () -> Unit,
    onAddProduct: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val servicesState by viewModel.servicesState.collectAsState()

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
        when(servicesState) {
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                val services = (servicesState as FeatureState.Success).data

                val state by viewModel.state.collectAsState()

                val coroutineScope = rememberCoroutineScope()

                val domainPagerState = rememberPagerState(
                    initialPage = state.selectedDomainIndex,
                    pageCount = { services.size }
                )

                LaunchedEffect(domainPagerState.currentPage) {
                    if (state.selectedDomainIndex != domainPagerState.currentPage) {
                        viewModel.selectDomain(domainPagerState.currentPage)
                    }
                }

                Column {
                    ScrollableTabRow(
                        selectedTabIndex = state.selectedDomainIndex,
                        edgePadding = BasePadding
                    ) {
                        services.forEachIndexed { index, domain ->
                            Tab(
                                selected = state.selectedDomainIndex == index,
                                onClick = {
                                    coroutineScope.launch {
                                        domainPagerState.animateScrollToPage(index)
                                    }
                                    viewModel.selectDomain(index)
                                },
                                text = { Text(domain.serviceDomain.name) }
                            )
                        }
                    }

                    HorizontalPager(
                        state = domainPagerState,
                        modifier = Modifier.weight(1f)
                    ) { domainIndex ->

                        val services = services[domainIndex].services

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
                                            coroutineScope.launch {
                                                servicePagerState.animateScrollToPage(index)
                                            }
                                            viewModel.selectService(domainIndex, index)
                                        },
                                        text = { Text(service.service.name) }
                                    )
                                }
                            }
                            HorizontalPager(
                                state = servicePagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { serviceIndex ->

                                val service = services[serviceIndex]

                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = service.service.name,
                                        style = headlineMedium,
                                        fontWeight = FontWeight.SemiBold
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