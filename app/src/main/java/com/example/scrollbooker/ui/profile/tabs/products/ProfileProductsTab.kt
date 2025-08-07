package com.example.scrollbooker.ui.profile.tabs.products
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.NavigateCalendarParam
import com.example.scrollbooker.ui.profile.tabs.ProfileTabViewModel
import com.example.scrollbooker.ui.profile.tabs.products.components.ServiceTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileProductsTab(
    viewModel: ProfileTabViewModel,
    userId: Int,
    isOwnProfile: Boolean,
    businessId: Int?,
    onNavigateToCalendar: (NavigateCalendarParam) -> Unit
) {
    val servicesState by viewModel.servicesState.collectAsState()

    when(servicesState) {
        is FeatureState.Loading -> {
            LoadingScreen(
                arrangement = Arrangement.Top,
                modifier = Modifier.padding(top = 50.dp)
            )
        }
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Success -> {
            val services = (servicesState as FeatureState.Success).data

            val pagerState = rememberPagerState(initialPage = 0) { services.size }
            val coroutineScope = rememberCoroutineScope()
            val selectedTabIndex = pagerState.currentPage

            Column(modifier = Modifier.fillMaxSize()) {
                ScrollableTabRow(
                    containerColor = Background,
                    contentColor = OnSurfaceBG,
                    edgePadding = BasePadding,
                    selectedTabIndex = pagerState.currentPage,
                    indicator = {},
                    divider = {
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 5.dp),
                            color = Divider,
                            thickness = 0.55.dp
                        )
                    }
                ) {
                    services.forEachIndexed { index, serv ->
                        val isSelected = selectedTabIndex == index

                        ServiceTab(
                            isSelected = isSelected,
                            serviceName = serv.service.name,
                            productsCount = serv.productsCount,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    beyondViewportPageCount = 0,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val serviceId = services[page].service.id
                    val employees = services[page].employees

                    ProfileServiceProductsTab(
                        viewModel = viewModel,
                        employees = employees,
                        userId = userId,
                        serviceId = serviceId,
                        onNavigateToCalendar = onNavigateToCalendar
                    )
                }
            }
        }
    }
}