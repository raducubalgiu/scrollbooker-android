package com.example.scrollbooker.screens.profile.myBusiness.myProducts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.screens.profile.myBusiness.myServices.MyServicesViewModel
import com.example.scrollbooker.ui.theme.Divider

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