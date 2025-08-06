package com.example.scrollbooker.ui.profile.tabs.products
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.navigators.NavigateCalendarParam
import com.example.scrollbooker.ui.profile.tabs.ProfileTabViewModel
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
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

                        Box(modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clip(shape = ShapeDefaults.Small)
                            .background(if(isSelected) SurfaceBG else Color.Transparent)
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(
                                        vertical = 10.dp,
                                        horizontal = 14.dp
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${serv.service.name} ${serv.productsCount}",
                                    style = bodyLarge,
                                    fontSize = 16.sp,
                                    color = if (isSelected) OnSurfaceBG else Color.Gray,
                                    fontWeight = if(isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                )
                            }
                        }
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