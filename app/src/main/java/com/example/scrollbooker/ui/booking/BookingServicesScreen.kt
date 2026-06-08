package com.example.scrollbooker.ui.booking
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.headers.Header
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.booking.services.BookingProductsList
import com.example.scrollbooker.ui.booking.services.BookingServicesTabs
import kotlinx.coroutines.launch

@Composable
fun BookingServicesScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel,
    onNavigateToSpecialists: () -> Unit,
    onBack: () -> Unit
) {
    val productsState by viewModel.productsState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            Header(onBack = onBack)
        },
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (val state = productsState) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    val serviceGroups = state.data

                    val activeTabIndex by remember(serviceGroups) {
                        derivedStateOf {
                            val firstVisibleIndex = listState.firstVisibleItemIndex
                            if (firstVisibleIndex == 0) 0 else firstVisibleIndex - 1
                        }
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        if (serviceGroups.isNotEmpty()) {
                            BookingServicesTabs(
                                activeTabIndex = activeTabIndex,
                                onTabChange = { tabIndex ->
                                    scope.launch {
                                        listState.animateScrollToItem(tabIndex + 1)
                                    }
                                },
                                serviceGroups = serviceGroups
                            )
                        }

                        BookingProductsList(
                            state = listState,
                            serviceGroups = serviceGroups
                        )
                    }
                }
            }
        }
    }
}