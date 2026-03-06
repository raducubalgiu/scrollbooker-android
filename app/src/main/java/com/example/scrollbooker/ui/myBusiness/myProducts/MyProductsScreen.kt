package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.key
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
import com.example.scrollbooker.entity.booking.products.domain.model.ProductSection
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServices
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServicesResponse
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceWithEmployees
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.accordion.Accordion
import com.example.scrollbooker.components.core.headers.HeaderEdit
import com.example.scrollbooker.components.customized.ProductCard.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.shared.products.components.EmployeesList
import com.example.scrollbooker.ui.shared.products.components.ServiceTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodySmall

@Composable
fun MyProductsScreen(
    viewModel: MyProductsViewModel,
    onBack: () -> Unit,
    onNavigateAddProduct: () -> Unit,
    onNavigateEditProduct: (Int, Int) -> Unit
) {
    val serviceDomains by viewModel.serviceDomains.collectAsState()
    val products by viewModel.productSections.collectAsState()
    val tabsState by viewModel.tabsState.collectAsState()

    val isEditable by remember {
        derivedStateOf {
            (serviceDomains as? FeatureState.Success)?.data?.isEditable == true
        }
    }

    Layout(
        headerTitle = stringResource(R.string.myProducts),
        header = {
            HeaderEdit(
                title = stringResource(R.string.myProducts),
                onAction = onNavigateAddProduct,
                actionTitle = stringResource(R.string.add),
                onBack = onBack,
                isEnabled = isEditable
            )
        },
        onBack = onBack,
        enablePaddingH = false
    ) {
        when(val sDomains = serviceDomains) {
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                MyProductsContent(
                    serviceDomains = sDomains.data,
                    products = products,
                    tabsState = tabsState,
                    isEditable = isEditable,
                    viewModel = viewModel,
                    onNavigateEditProduct = onNavigateEditProduct
                )
            }
        }
    }
}

@Composable
private fun MyProductsContent(
    serviceDomains: ServiceDomainWithEmployeeServicesResponse,
    products: FeatureState<List<ProductSection>>,
    tabsState: ServicesTabsState,
    isEditable: Boolean,
    viewModel: MyProductsViewModel,
    onNavigateEditProduct: (Int, Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    val domains = serviceDomains.serviceDomains
    val hasMultipleDomains by remember { derivedStateOf { domains.size > 1 } }

    val domainPagerState = rememberPagerState(
        initialPage = tabsState.selectedDomainIndex,
        pageCount = { domains.size }
    )

    LaunchedEffect(domainPagerState.currentPage) {
        if (tabsState.selectedDomainIndex != domainPagerState.currentPage) {
            viewModel.selectDomain(domainPagerState.currentPage)
        }
    }

    Column {
        if (!isEditable) {
            PermissionWarningBanner()
        }

        if (hasMultipleDomains) {
            DomainTabRow(
                domains = domains,
                selectedDomainIndex = tabsState.selectedDomainIndex,
                scope = scope,
                domainPagerState = domainPagerState,
                onSelectDomain = viewModel::selectDomain
            )
        }

        HorizontalPager(
            state = domainPagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false,
            key = { it }
        ) { domainIndex ->
            DomainPage(
                domainIndex = domainIndex,
                domains = domains,
                products = products,
                tabsState = tabsState,
                isEditable = isEditable,
                scope = scope,
                viewModel = viewModel,
                onNavigateEditProduct = onNavigateEditProduct
            )
        }
    }
}

@Composable
private fun PermissionWarningBanner() {
    Text(
        modifier = Modifier.padding(horizontal = BasePadding),
        text = stringResource(R.string.youDoNotHavePermissionToEditProducts),
        color = Error,
        style = bodySmall
    )
    Spacer(Modifier.height(BasePadding))
}

@Composable
private fun DomainTabRow(
    domains: List<ServiceDomainWithEmployeeServices>,
    selectedDomainIndex: Int,
    scope: CoroutineScope,
    domainPagerState: PagerState,
    onSelectDomain: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedDomainIndex,
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
        domains.forEachIndexed { index, domain ->
            key(domain.id) {
                val isSelected = selectedDomainIndex == index

                Button(
                    modifier = Modifier.padding(vertical = 8.dp),
                    onClick = remember(index, scope, domainPagerState, onSelectDomain) {
                        {
                            scope.launch { domainPagerState.animateScrollToPage(index) }
                            onSelectDomain(index)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Primary else Color.Transparent,
                        contentColor = if (isSelected) OnPrimary else Color.Gray
                    ),
                    shape = ShapeDefaults.ExtraLarge,
                ) {
                    Text(
                        text = domain.name,
                        style = bodyLarge,
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
private fun DomainPage(
    domainIndex: Int,
    domains: List<ServiceDomainWithEmployeeServices>,
    products: FeatureState<List<ProductSection>>,
    tabsState: ServicesTabsState,
    isEditable: Boolean,
    scope: CoroutineScope,
    viewModel: MyProductsViewModel,
    onNavigateEditProduct: (Int, Int) -> Unit
) {
    val services = remember(domainIndex, domains) {
        domains.getOrNull(domainIndex)?.services ?: emptyList()
    }

    val selectedServiceIndex = remember(domainIndex, tabsState) {
        tabsState.selectedServicePerDomain[domainIndex] ?: 0
    }

    val servicePagerState = rememberPagerState(
        initialPage = selectedServiceIndex,
        pageCount = { services.size }
    )

    LaunchedEffect(servicePagerState.currentPage) {
        viewModel.selectService(domainIndex, servicePagerState.currentPage)
    }

    Column {
        ServiceTabRow(
            services = services,
            selectedServiceIndex = servicePagerState.currentPage,
            scope = scope,
            servicePagerState = servicePagerState,
            onSelectService = remember(domainIndex, viewModel) {
                { index -> viewModel.selectService(domainIndex, index) }
            }
        )

        HorizontalPager(
            state = servicePagerState,
            modifier = Modifier.fillMaxSize(),
            key = { it }
        ) { serviceIndex ->
            ServicePage(
                domainIndex = domainIndex,
                serviceIndex = serviceIndex,
                domains = domains,
                products = products,
                isEditable = isEditable,
                viewModel = viewModel,
                onNavigateEditProduct = onNavigateEditProduct
            )
        }
    }
}

@Composable
private fun ServiceTabRow(
    services: List<ServiceWithEmployees>,
    selectedServiceIndex: Int,
    scope: CoroutineScope,
    servicePagerState: PagerState,
    onSelectService: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedServiceIndex,
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
            key(service.id) {
                val isSelected = selectedServiceIndex == index

                ServiceTab(
                    isSelected = isSelected,
                    serviceName = service.shortName,
                    onClick = remember(index, scope, servicePagerState, onSelectService) {
                        {
                            scope.launch { servicePagerState.animateScrollToPage(index) }
                            onSelectService(index)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ServicePage(
    domainIndex: Int,
    serviceIndex: Int,
    domains: List<ServiceDomainWithEmployeeServices>,
    products: FeatureState<List<ProductSection>>,
    isEditable: Boolean,
    viewModel: MyProductsViewModel,
    onNavigateEditProduct: (Int, Int) -> Unit
) {
    when (products) {
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Success -> {
            if (products.data.isEmpty()) {
                MessageScreen(
                    message = stringResource(R.string.noProductsFound),
                    icon = painterResource(R.drawable.ic_list_bullet_outline)
                )
            } else {
                ProductsList(
                    domainIndex = domainIndex,
                    serviceIndex = serviceIndex,
                    domains = domains,
                    productSections = products.data,
                    isEditable = isEditable,
                    viewModel = viewModel,
                    onNavigateEditProduct = onNavigateEditProduct
                )
            }
        }
    }
}

@Composable
private fun ProductsList(
    domainIndex: Int,
    serviceIndex: Int,
    domains: List<ServiceDomainWithEmployeeServices>,
    productSections: List<ProductSection>,
    isEditable: Boolean,
    viewModel: MyProductsViewModel,
    onNavigateEditProduct: (Int, Int) -> Unit
) {
    val employees = remember(domainIndex, serviceIndex, domains) {
        domains.getOrNull(domainIndex)
            ?.services?.getOrNull(serviceIndex)
            ?.employees ?: emptyList()
    }

    val serviceDomainId = remember(domainIndex, domains) {
        domains.getOrNull(domainIndex)?.id ?: 0
    }

    val onSelectEmployee = remember(domainIndex, serviceIndex, viewModel) {
        { employeeId: Int ->
            viewModel.selectEmployee(domainIndex, serviceIndex, employeeId)
        }
    }

    val onNavigateToEditMemoized = remember(serviceDomainId, onNavigateEditProduct) {
        { productId: Int -> onNavigateEditProduct(serviceDomainId, productId) }
    }

    val onDeleteProduct = remember(viewModel) {
        { productId: Int -> viewModel.deleteProduct(productId) }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        if (employees.isNotEmpty()) {
            item(key = "employees_list") {
                EmployeesList(
                    employees = employees,
                    onSetEmployee = onSelectEmployee
                )
            }
        }

        items(
            items = productSections,
            key = { section -> section.subFilter?.id ?: "no_filter_${section.hashCode()}" }
        ) { section ->
            ProductSectionItem(
                section = section,
                isEditable = isEditable,
                onNavigateToEdit = onNavigateToEditMemoized,
                onDeleteProduct = onDeleteProduct
            )
        }
    }
}

@Composable
private fun LazyItemScope.ProductSectionItem(
    section: ProductSection,
    isEditable: Boolean,
    onNavigateToEdit: (Int) -> Unit,
    onDeleteProduct: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }

    val hasSubFilterName = remember(section.subFilter) {
        section.subFilter?.name?.isNotEmpty() == true
    }

    if (hasSubFilterName) {
        Accordion(
            title = section.subFilter?.name ?: "",
            description = section.subFilter?.description,
            isExpanded = isExpanded,
            onSetExpanded = { isExpanded = !isExpanded },
            containerColor = Color.Transparent,
            titleContainerColor = SurfaceBG,
            shape = ShapeDefaults.Large
        ) {
            ProductSectionContent(
                section = section,
                isEditable = isEditable,
                onNavigateToEdit = onNavigateToEdit,
                onDeleteProduct = onDeleteProduct
            )
        }
    } else {
        ProductSectionContent(
            section = section,
            isEditable = isEditable,
            onNavigateToEdit = onNavigateToEdit,
            onDeleteProduct = onDeleteProduct
        )
    }
}

@Composable
private fun ProductSectionContent(
    section: ProductSection,
    isEditable: Boolean,
    onNavigateToEdit: (Int) -> Unit,
    onDeleteProduct: (Int) -> Unit
) {
    Column {
        section.products.forEachIndexed { index, product ->
            key(product.id) {
                ProductCard(
                    product = product,
                    displayEditableActions = true,
                    isEditable = isEditable,
                    onNavigateToEdit = onNavigateToEdit,
                    onDeleteProduct = onDeleteProduct
                )

                if (index < section.products.size - 1) {
                    HorizontalDivider(
                        color = Divider,
                        thickness = 0.6.dp
                    )
                }
            }
        }
    }
}