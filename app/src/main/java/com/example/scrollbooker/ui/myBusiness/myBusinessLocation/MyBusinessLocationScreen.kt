package com.example.scrollbooker.ui.myBusiness.myBusinessLocation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.myBusiness.myBusinessLocation.tabs.MyBusinessGalleryTab
import com.example.scrollbooker.ui.myBusiness.myBusinessLocation.tabs.MyBusinessLocationTab
import com.example.scrollbooker.ui.shared.products.components.ServiceTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

@Composable
fun MyBusinessLocationScreen(
    viewModel: MyBusinessLocationViewModel,
    onBack: () -> Unit,
    onNavigateToEditGallery: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val tabs = remember { MyBusinessLocationTab.getTabs }

    val pagerState = rememberPagerState(initialPage = 0) { 4 }
    val selectedTabIndex = pagerState.currentPage

    Scaffold(
        topBar = { Header(
            onBack = onBack,
            title = stringResource(R.string.myBusiness)
        ) }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
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
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTabIndex == index

                    ServiceTab(
                        isSelected = isSelected,
                        serviceName = stringResource(tab.label),
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                modifier = Modifier.fillMaxSize(),
                pageSize = PageSize.Fill,
                key = { it }
            ) { page ->
                when(page) {
                    0 -> {}
                    1 -> MyBusinessGalleryTab(onNavigateToEditGallery)
                }
            }
        }

    }
}