package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.core.util.FeatureState
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.headers.HeaderEdit
import com.example.scrollbooker.components.customized.ProductCard.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.shared.products.components.EmployeesList
import com.example.scrollbooker.ui.shared.products.components.ServiceTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.headlineSmall

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
                    if(serviceDomains.size > 1) {
                        ScrollableTabRow(
                            selectedTabIndex = tabsState.selectedDomainIndex,
                            edgePadding = BasePadding,
                            containerColor = Background,
                            contentColor = OnSurfaceBG,
                            indicator = {},
                            divider = {
                                HorizontalDivider(
                                    modifier = Modifier.padding(top = 5.dp),
                                    color = Divider,
                                    thickness = 0.55.dp
                                )
                            }
                        ) {
                            serviceDomains.forEachIndexed { index, domain ->
                                val isSelected = tabsState.selectedDomainIndex == index

                                Button(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    onClick = {
                                        scope.launch { domainPagerState.animateScrollToPage(index) }
                                        viewModel.selectDomain(index)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if(isSelected) Primary else Color.Transparent,
                                        contentColor = if (isSelected) OnPrimary else Color.Gray
                                    ),
                                    shape = ShapeDefaults.ExtraLarge,
                                ) {
                                    Text(
                                        text = domain.name,
                                        style = bodyLarge,
                                        fontSize = 16.sp,
                                        fontWeight = if(isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    )
                                }
                            }
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
                                edgePadding = BasePadding,
                                containerColor = Background,
                                contentColor = OnSurfaceBG,
                                indicator = {},
                                divider = {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(top = 5.dp),
                                        color = Divider,
                                        thickness = 0.55.dp
                                    )
                                }
                            ) {
                                services.forEachIndexed { index, service ->
                                    val isSelected = servicePagerState.currentPage == index

                                    ServiceTab(
                                        isSelected = isSelected,
                                        serviceName = service.shortName,
                                        onClick = {
                                            scope.launch { servicePagerState.animateScrollToPage(index) }
                                            viewModel.selectService(domainIndex, index)
                                        }
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
                                        if(prod.data.isEmpty()) {
                                            MessageScreen(
                                                message = stringResource(R.string.noProductsFound),
                                                icon = painterResource(R.drawable.ic_list_bullet_outline)
                                            )
                                        } else {
                                            LazyColumn(
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                item {
                                                    val employees = serviceDomains[domainIndex]
                                                        .services[serviceIndex]
                                                        .employees

                                                    if(employees.isNotEmpty()) {
                                                        EmployeesList(
                                                            employees = employees,
                                                            onSetEmployee = { employeeId ->
                                                                viewModel.selectEmployee(domainIndex, serviceIndex, employeeId)
                                                            }
                                                        )
                                                    }
                                                }

                                                items(prod.data) { section ->
                                                    Column {
                                                        if(section.subFilter != null) {
                                                            Text(
                                                                text = section.subFilter.name,
                                                                style = headlineSmall,
                                                                fontSize = 22.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                modifier = Modifier.padding(
                                                                    top = BasePadding,
                                                                    start = BasePadding,
                                                                    end = BasePadding,
                                                                )
                                                            )

                                                            section.subFilter.description?.let {
                                                                Text(
                                                                    text = it,
                                                                    style = bodyMedium,
                                                                    color = Color.Gray,
                                                                    modifier = Modifier.padding(
                                                                        start = BasePadding,
                                                                        end = BasePadding,
                                                                        bottom = 8.dp
                                                                    )
                                                                )
                                                            }
                                                        }

                                                        section.products.forEachIndexed { index, product ->
                                                            ProductCard(
                                                                product = product,
                                                                isSelected = false,
                                                                onSelect = {}
                                                            )

                                                            if(index < section.products.size - 1) {
                                                                HorizontalDivider(
                                                                    color = Divider,
                                                                    thickness = 0.6.dp
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
}