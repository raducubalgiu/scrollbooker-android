package com.example.scrollbooker.screens.profile.myBusiness.myProducts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
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
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.components.core.headers.HeaderEdit
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun MyProductsScreen(
    viewModel: MyProductsViewModel,
    onBack: () -> Unit,
    onAddProduct: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val myProductsViewModel: MyProductsViewModel = hiltViewModel()
    val servicesState by viewModel.servicesState.collectAsState()

    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = stringResource(R.string.myProducts),
        header = {
            HeaderEdit(
                modifier = Modifier.padding(horizontal = BasePadding),
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

                val pagerState = rememberPagerState(initialPage = 0) { services.size }
                val coroutineScope = rememberCoroutineScope()
                val selectedTabIndex = pagerState.currentPage

                Column(modifier = Modifier.fillMaxSize()) {
                    ScrollableTabRow(
                        modifier = Modifier.padding(start = BasePadding),
                        containerColor = Background,
                        contentColor = OnSurfaceBG,
                        edgePadding = 0.dp,
                        selectedTabIndex = pagerState.currentPage,
                        indicator = {},
                        divider = {}
                    ) {
                        services.forEachIndexed { index, service ->
                            val isSelected = selectedTabIndex == index

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(if (isSelected) Primary else Color.Transparent)
                                    .clickable {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    }
                                    .padding(horizontal = BasePadding, vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = service.name,
                                    style = bodyLarge,
                                    color = if (isSelected) OnPrimary else OnSurfaceBG,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }

                    HorizontalPager(
                        state = pagerState,
                        beyondViewportPageCount = 0,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val serviceId = services[page].id

                        ProductsTab(
                            myProductsViewModel = myProductsViewModel,
                            serviceId = serviceId,
                            onNavigateToEdit = onNavigateToEdit
                        )
                    }
                }
            }
        }
    }
}