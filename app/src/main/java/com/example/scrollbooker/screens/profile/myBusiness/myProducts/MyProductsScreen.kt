package com.example.scrollbooker.screens.profile.myBusiness.myProducts
import androidx.compose.runtime.Composable
import com.example.scrollbooker.screens.profile.myBusiness.myServices.MyServicesViewModel

@Composable
fun MyProductsScreen(
    viewModel: MyServicesViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
//    val myProductsViewModel: MyProductsViewModel = hiltViewModel()
//    val servicesState by viewModel.servicesState.collectAsState()
//
//    Layout(
//        headerTitle = stringResource(R.string.myProducts),
//        onBack = onBack,
//        enablePaddingH = false
//    ) {
//        when(servicesState) {
//            is FeatureState.Loading -> LoadingScreen()
//            is FeatureState.Error -> ErrorScreen()
//            is FeatureState.Success -> {
//                val services = (servicesState as FeatureState.Success).data
//
//                val pagerState = rememberPagerState(initialPage = 0) { services.size }
//                val coroutineScope = rememberCoroutineScope()
//                val selectedTabIndex = pagerState.currentPage
//
//                Column {
//                    ScrollableTabRow(
//                        containerColor = Background,
//                        contentColor = OnSurfaceBG,
//                        edgePadding = 0.dp,
//                        selectedTabIndex = pagerState.currentPage,
//                    ) {
//                        services.forEachIndexed { index, service ->
//                            val isSelected = selectedTabIndex == index
//
//                            Tab(
//                                selected = isSelected,
//                                onClick = {
//                                    coroutineScope.launch {
//                                        pagerState.animateScrollToPage(index)
//                                    }
//                                },
//                                text = {
//                                    Text(
//                                        text = service.name,
//                                        style = bodyLarge,
//                                        color = if (isSelected) OnBackground else OnSurfaceBG,
//                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
//                                    )
//                                }
//                            )
//                        }
//                    }
//                    HorizontalDivider(color = Divider)
//
//                    HorizontalPager(
//                        state = pagerState,
//                        beyondViewportPageCount = 0,
//                        modifier = Modifier.fillMaxSize()
//                    ) { page ->
//                        val serviceId = services[page].id
//
//                        ProductsTab(
//                            myProductsViewModel = myProductsViewModel,
//                            serviceId = serviceId
//                        )
//                    }
//                }
//            }
//        }
//    }
}