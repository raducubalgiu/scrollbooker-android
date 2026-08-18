package com.example.scrollbooker.ui.myBusiness.myBusinessDetails
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.myBusiness.myBusinessDetails.tabs.MyBusinessGalleryTab
import com.example.scrollbooker.ui.myBusiness.myBusinessDetails.tabs.MyBusinessDetailsTab
import com.example.scrollbooker.ui.myBusiness.myBusinessDetails.tabs.MyBusinessSchedulesTab
import com.example.scrollbooker.ui.myBusiness.myBusinessDetails.tabs.MyBusinessSummaryTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.tabs.ServiceTab
import com.example.scrollbooker.core.snackbar.CustomSnackBar
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.ProfileNavigator

@Composable
fun MyBusinessDetailsScreen(
    viewModel: MyBusinessDetailsViewModel,
    profileNavigate: ProfileNavigator
) {
    val scope = rememberCoroutineScope()
    val tabs = remember { MyBusinessDetailsTab.getTabs }

    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
    val selectedTabIndex = pagerState.currentPage

    val business by viewModel.business.collectAsStateWithLifecycle()
    val photosState by viewModel.photosState.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    val hostState = remember { SnackbarHostState() }
    val snackBarController = rememberSnackBarController(hostState)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackBarController.show(event)
        }
    }

    Box(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { Header(
                onBack = { profileNavigate.back() },
                title = stringResource(R.string.myBusiness)
            ) }
        ) { innerPadding ->
            Column(Modifier.fillMaxSize().padding(innerPadding)) {
                when(business) {
                    is FeatureState.Loading -> LoadingScreen()
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Success -> {
                        val businessData = (business as FeatureState.Success).data

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
                                0 -> MyBusinessSummaryTab()
                                1 -> MyBusinessGalleryTab(
                                    isSaving = isSaving,
                                    photosState = photosState,
                                    onClearImage = { index -> viewModel.clearImage(index) },
                                    onSetImage = { index, uri -> viewModel.setImage(index, uri) },
                                    onSaveGallery = { viewModel.saveBusinessGallery() }
                                )
                                2 -> MyBusinessSchedulesTab()
                            }
                        }
                    }
                }
            }
        }

        CustomSnackBar(hostState = hostState)
    }
}