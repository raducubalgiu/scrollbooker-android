package com.example.scrollbooker.screens.profile.myBusiness.myProducts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.screens.profile.myBusiness.myServices.MyServicesViewModel
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun MyProductsScreen(
    viewModel: MyServicesViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val myProductsViewModel: MyProductsViewModel = hiltViewModel()
    val servicesState by viewModel.state.collectAsState()

    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = stringResource(R.string.myProducts),
        onBack = onBack,
        enablePaddingH = false
    ) {
        when(servicesState) {
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                val services = (servicesState as FeatureState.Success).data

                val pagerState = rememberPagerState(initialPage = 0) { services.size }
                val coroutineScope = rememberCoroutineScope()
                val selectedTabIndex = pagerState.currentPage

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        ScrollableTabRow(
                            modifier = Modifier.padding(start = BasePadding),
                            containerColor = Background,
                            contentColor = OnSurfaceBG,
                            edgePadding = 0.dp,
                            selectedTabIndex = pagerState.currentPage,
                            indicator = {},
                            divider = { HorizontalDivider(color = Color.Transparent) }
                        ) {
                            services.forEachIndexed { index, service ->
                                val isSelected = selectedTabIndex == index

                                Tab(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(if(isSelected) Primary else Color.Transparent),
                                    selected = isSelected,
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    },
                                    text = {
                                        Text(
                                            text = service.name,
                                            style = bodyLarge,
                                            color = if (isSelected) OnPrimary else OnSurfaceBG,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                )
                            }
                        }
                        HorizontalDivider(color = Divider)

                        HorizontalPager(
                            state = pagerState,
                            beyondViewportPageCount = 0,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            val serviceId = services[page].id

                            ProductsTab(
                                myProductsViewModel = myProductsViewModel,
                                serviceId = serviceId
                            )
                        }
                    }

                    Column {
                        HorizontalDivider(color = Divider, thickness = 0.5.dp)
                        MainButton(
                            modifier = Modifier.padding(BasePadding),
                            title = stringResource(R.string.createNewProduct),
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}