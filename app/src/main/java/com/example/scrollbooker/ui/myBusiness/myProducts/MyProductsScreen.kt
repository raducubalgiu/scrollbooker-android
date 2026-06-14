package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.runtime.Composable

@Composable
fun MyProductsScreen(
    viewModel: MyProductsViewModel,
    onBack: () -> Unit,
    onNavigateAddProduct: () -> Unit,
    onNavigateEditProduct: (Int, Int) -> Unit
) {
//    val serviceDomains by viewModel.serviceDomains.collectAsState()
//    val products by viewModel.productSections.collectAsState()
//    val tabsState by viewModel.tabsState.collectAsState()
//
//    val isEditable by remember {
//        derivedStateOf {
//            (serviceDomains as? FeatureState.Success)?.data?.isEditable == true
//        }
//    }
//
//    Layout(
//        headerTitle = stringResource(R.string.myProducts),
//        header = {
//            HeaderEdit(
//                title = stringResource(R.string.myProducts),
//                onAction = onNavigateAddProduct,
//                actionTitle = stringResource(R.string.add),
//                onBack = onBack,
//                isEnabled = isEditable
//            )
//        },
//        onBack = onBack,
//        enablePaddingH = false
//    ) {
//        when(val sDomains = serviceDomains) {
//            is FeatureState.Loading -> LoadingScreen()
//            is FeatureState.Error -> ErrorScreen()
//            is FeatureState.Success -> {
//                MyProductsContent(
//                    serviceDomains = sDomains.data,
//                    products = products,
//                    tabsState = tabsState,
//                    isEditable = isEditable,
//                    onNavigateEditProduct = onNavigateEditProduct,
//                    onSelectDomain = viewModel::selectDomain,
//                    onSelectService = viewModel::selectService,
//                    onSelectEmployee = viewModel::selectEmployee,
//                    onDeleteProduct = viewModel::deleteProduct
//                )
//            }
//        }
//    }
}

//@Composable
//fun MyProductsContent(
//    serviceDomains: ServiceDomainWithEmployeeServicesResponse,
//    products: FeatureState<List<ProductSection>>,
//    tabsState: ServicesTabsState,
//    isEditable: Boolean = false,
//    onSelectDomain: (Int) -> Unit,
//    onSelectService: (Int, Int) -> Unit,
//    onSelectEmployee: (domainIndex: Int, serviceIndex: Int, employeeId: Int) -> Unit,
//    onNavigateEditProduct: ((Int, Int) -> Unit)? = null,
//    onDeleteProduct: ((productId: Int) -> Unit)? = null,
//) {
//    val scope = rememberCoroutineScope()
//
//    val domains = serviceDomains.serviceDomains
//    val hasMultipleDomains by remember { derivedStateOf { domains.size > 1 } }
//
//    val domainPagerState = rememberPagerState(
//        initialPage = tabsState.selectedDomainIndex,
//        pageCount = { domains.size }
//    )
//
//    LaunchedEffect(domainPagerState.currentPage) {
//        if (tabsState.selectedDomainIndex != domainPagerState.currentPage) {
//            onSelectDomain(domainPagerState.currentPage)
//        }
//    }
//
//    Column {
//        if (!isEditable) {
//            PermissionWarningBanner()
//        }
//
//        if (hasMultipleDomains) {
//            DomainTabRow(
//                domains = domains,
//                selectedDomainIndex = tabsState.selectedDomainIndex,
//                scope = scope,
//                domainPagerState = domainPagerState,
//                onSelectDomain = onSelectDomain
//            )
//        }
//
//        HorizontalPager(
//            state = domainPagerState,
//            modifier = Modifier.weight(1f),
//            userScrollEnabled = false,
//            key = { it }
//        ) { domainIndex ->
//            DomainPage(
//                domainIndex = domainIndex,
//                domains = domains,
//                products = products,
//                tabsState = tabsState,
//                isEditable = isEditable,
//                scope = scope,
//                onNavigateEditProduct = onNavigateEditProduct,
//                onSelectService = onSelectService,
//                onSelectEmployee = onSelectEmployee,
//                onDeleteProduct = onDeleteProduct
//            )
//        }
//    }
//}
//
//@Composable
//private fun PermissionWarningBanner() {
//    Text(
//        modifier = Modifier.padding(horizontal = BasePadding),
//        text = stringResource(R.string.youDoNotHavePermissionToEditProducts),
//        color = Error,
//        style = bodySmall
//    )
//    Spacer(Modifier.height(BasePadding))
//}
//
//@Composable
//private fun DomainTabRow(
//    domains: List<ServiceDomainWithEmployeeServices>,
//    selectedDomainIndex: Int,
//    scope: CoroutineScope,
//    domainPagerState: PagerState,
//    onSelectDomain: (Int) -> Unit
//) {
//    ScrollableTabRow(
//        selectedTabIndex = selectedDomainIndex,
//        edgePadding = BasePadding,
//        containerColor = Background,
//        contentColor = OnSurfaceBG,
//        indicator = {},
//        divider = {
//            HorizontalDivider(
//                modifier = Modifier.padding(top = 5.dp),
//                color = Divider,
//                thickness = 0.55.dp
//            )
//        }
//    ) {
//        domains.forEachIndexed { index, domain ->
//            key(domain.id) {
//                val isSelected = selectedDomainIndex == index
//
//                Button(
//                    modifier = Modifier.padding(vertical = 8.dp),
//                    onClick = remember(index, scope, domainPagerState, onSelectDomain) {
//                        {
//                            scope.launch { domainPagerState.animateScrollToPage(index) }
//                            onSelectDomain(index)
//                        }
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (isSelected) Primary else Color.Transparent,
//                        contentColor = if (isSelected) OnPrimary else Color.Gray
//                    ),
//                    shape = ShapeDefaults.ExtraLarge,
//                ) {
//                    Text(
//                        text = domain.name,
//                        style = bodyLarge,
//                        fontSize = 16.sp,
//                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun DomainPage(
//    domainIndex: Int,
//    domains: List<ServiceDomainWithEmployeeServices>,
//    products: FeatureState<List<ProductSection>>,
//    tabsState: ServicesTabsState,
//    isEditable: Boolean,
//    scope: CoroutineScope,
//    onSelectService: (Int, Int) -> Unit,
//    onSelectEmployee: (domainIndex: Int, serviceIndex: Int, employeeId: Int) -> Unit,
//    onNavigateEditProduct: ((Int, Int) -> Unit)? = null,
//    onDeleteProduct: ((productId: Int) -> Unit)? = null,
//) {
//    val services = remember(domainIndex, domains) {
//        domains.getOrNull(domainIndex)?.services ?: emptyList()
//    }
//
//    val selectedServiceIndex = remember(domainIndex, tabsState) {
//        tabsState.selectedServicePerDomain[domainIndex] ?: 0
//    }
//
//    val servicePagerState = rememberPagerState(
//        initialPage = selectedServiceIndex,
//        pageCount = { services.size }
//    )
//
//    LaunchedEffect(servicePagerState.currentPage) {
//        onSelectService(domainIndex, servicePagerState.currentPage)
//    }
//
//    Column {
//        ServiceTabRow(
//            services = services,
//            selectedServiceIndex = servicePagerState.currentPage,
//            scope = scope,
//            servicePagerState = servicePagerState,
//            onSelectService = remember(domainIndex, onSelectService) {
//                { index -> onSelectService(domainIndex, index) }
//            }
//        )
//
//        HorizontalPager(
//            state = servicePagerState,
//            modifier = Modifier.fillMaxSize(),
//            key = { it }
//        ) { serviceIndex ->
//            ServicePage(
//                domainIndex = domainIndex,
//                serviceIndex = serviceIndex,
//                domains = domains,
//                products = products,
//                isEditable = isEditable,
//                onNavigateEditProduct = onNavigateEditProduct,
//                onSelectEmployee = onSelectEmployee,
//                onDeleteProduct = onDeleteProduct
//            )
//        }
//    }
//}
//
//@Composable
//private fun ServiceTabRow(
//    services: List<ServiceWithEmployees>,
//    selectedServiceIndex: Int,
//    scope: CoroutineScope,
//    servicePagerState: PagerState,
//    onSelectService: (Int) -> Unit
//) {
//    ScrollableTabRow(
//        selectedTabIndex = selectedServiceIndex,
//        edgePadding = BasePadding,
//        containerColor = Background,
//        contentColor = OnSurfaceBG,
//        indicator = {},
//        divider = {
//            HorizontalDivider(
//                modifier = Modifier.padding(top = 5.dp),
//                color = Divider,
//                thickness = 0.55.dp
//            )
//        }
//    ) {
//        services.forEachIndexed { index, service ->
//            key(service.id) {
//                val isSelected = selectedServiceIndex == index
//
//                ServiceTab(
//                    isSelected = isSelected,
//                    serviceName = service.shortName,
//                    onClick = remember(index, scope, servicePagerState, onSelectService) {
//                        {
//                            scope.launch { servicePagerState.animateScrollToPage(index) }
//                            onSelectService(index)
//                        }
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun ServicePage(
//    domainIndex: Int,
//    serviceIndex: Int,
//    domains: List<ServiceDomainWithEmployeeServices>,
//    products: FeatureState<List<ProductSection>>,
//    isEditable: Boolean,
//    onSelectEmployee: (domainIndex: Int, serviceIndex: Int, employeeId: Int) -> Unit,
//    onNavigateEditProduct: ((Int, Int) -> Unit)? = null,
//    onDeleteProduct: ((productId: Int) -> Unit)? = null,
//) {
//    when (products) {
//        is FeatureState.Error -> ErrorScreen()
//        is FeatureState.Loading -> LoadingScreen()
//        is FeatureState.Success -> {
//            if (products.data.isEmpty()) {
//                MessageScreen(
//                    message = stringResource(R.string.noProductsFound),
//                    icon = painterResource(R.drawable.ic_list_bullet_outline)
//                )
//            } else {
//                ProductsList(
//                    domainIndex = domainIndex,
//                    serviceIndex = serviceIndex,
//                    domains = domains,
//                    productSections = products.data,
//                    isEditable = isEditable,
//                    onNavigateEditProduct = onNavigateEditProduct,
//                    onSelectEmployee = onSelectEmployee,
//                    onDeleteProduct = onDeleteProduct
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun ProductsList(
//    domainIndex: Int,
//    serviceIndex: Int,
//    domains: List<ServiceDomainWithEmployeeServices>,
//    productSections: List<ProductSection>,
//    isEditable: Boolean,
//    onSelectEmployee: (domainIndex: Int, serviceIndex: Int, employeeId: Int) -> Unit,
//    onNavigateEditProduct: ((Int, Int) -> Unit)? = null,
//    onDeleteProduct: ((productId: Int) -> Unit)? = null,
//) {
//    val employees = remember(domainIndex, serviceIndex, domains) {
//        domains.getOrNull(domainIndex)
//            ?.services?.getOrNull(serviceIndex)
//            ?.employees ?: emptyList()
//    }
//
//    val serviceDomainId = remember(domainIndex, domains) {
//        domains.getOrNull(domainIndex)?.id ?: 0
//    }
//
//    val handleSelectEmployee = remember(domainIndex, serviceIndex, onSelectEmployee) {
//        { employeeId: Int ->
//            onSelectEmployee(domainIndex, serviceIndex, employeeId)
//        }
//    }
//
//    val onNavigateToEditMemoized: ((Int) -> Unit)? = remember(serviceDomainId, onNavigateEditProduct) {
//        onNavigateEditProduct?.let { edit ->
//            { productId: Int -> edit(serviceDomainId, productId) }
//        }
//    }
//
//    val onDeleteProductMemoized: ((Int) -> Unit)? = remember(onDeleteProduct) {
//        onDeleteProduct?.let { delete ->
//            { productId: Int -> delete(productId) }
//        }
//    }
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        if (employees.isNotEmpty()) {
//            item(key = "employees_list") {
//                EmployeesList(
//                    employees = employees,
//                    onSetEmployee = handleSelectEmployee
//                )
//            }
//        }
//
//        items(
//            items = productSections,
//            key = { section -> section.subFilter?.id ?: "no_filter_${section.hashCode()}" }
//        ) { section ->
//            ProductSectionItem(
//                section = section,
//                isEditable = isEditable,
//                onNavigateToEdit = onNavigateToEditMemoized,
//                onDeleteProduct = onDeleteProductMemoized
//            )
//        }
//    }
//}
//
//@Composable
//private fun LazyItemScope.ProductSectionItem(
//    section: ProductSection,
//    isEditable: Boolean,
//    onNavigateToEdit: ((Int) -> Unit)? = null,
//    onDeleteProduct: ((productId: Int) -> Unit)? = null,
//) {
//    var isExpanded by remember { mutableStateOf(true) }
//
//    val hasSubFilterName = remember(section.subFilter) {
//        section.subFilter?.name?.isNotEmpty() == true
//    }
//
//    if (hasSubFilterName) {
//        Accordion(
//            title = section.subFilter?.name ?: "",
//            description = section.subFilter?.description,
//            isExpanded = isExpanded,
//            onSetExpanded = { isExpanded = !isExpanded },
//            containerColor = Color.Transparent,
//            titleContainerColor = SurfaceBG,
//            shape = ShapeDefaults.Large
//        ) {
//            ProductSectionContent(
//                section = section,
//                isEditable = isEditable,
//                onNavigateToEdit = onNavigateToEdit,
//                onDeleteProduct = onDeleteProduct
//            )
//        }
//    } else {
//        ProductSectionContent(
//            section = section,
//            isEditable = isEditable,
//            onNavigateToEdit = onNavigateToEdit,
//            onDeleteProduct = onDeleteProduct
//        )
//    }
//}
//
//@Composable
//private fun ProductSectionContent(
//    section: ProductSection,
//    isEditable: Boolean,
//    onNavigateToEdit: ((Int) -> Unit)? = null,
//    onDeleteProduct: ((productId: Int) -> Unit)? = null,
//) {
//    Column {
//        section.products.forEachIndexed { index, product ->
//            key(product.id) {
//                ProductCard(
//                    product = product,
//                    displayEditableActions = true,
//                    isEditable = isEditable,
//                    onNavigateToEdit = onNavigateToEdit,
//                    onDeleteProduct = onDeleteProduct
//                )
//
//                if (index < section.products.size - 1) {
//                    HorizontalDivider(
//                        color = Divider,
//                        thickness = 0.6.dp
//                    )
//                }
//            }
//        }
//    }
//}