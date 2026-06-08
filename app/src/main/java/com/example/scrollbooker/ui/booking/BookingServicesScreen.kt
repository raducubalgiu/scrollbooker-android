package com.example.scrollbooker.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.headers.Header
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
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
                            ScrollableTabRow(
                                selectedTabIndex = activeTabIndex.coerceIn(0, serviceGroups.lastIndex),
                                edgePadding = 16.dp,
                                containerColor = Background,
                                divider = {},
                                indicator = { _ -> Box(Modifier.size(0.dp)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                serviceGroups.forEachIndexed { index, group ->
                                    val isSelected = activeTabIndex == index

                                    Tab(
                                        selected = isSelected,
                                        onClick = {
                                            scope.launch {
                                                listState.animateScrollToItem(index + 1)
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(vertical = 6.dp)
                                            .clip(ShapeDefaults.ExtraLarge)
                                            .background(if (isSelected) Primary else Background),
                                        text = {
                                            val tabTitle = group.service.shortName
                                            Text(
                                                text = tabTitle,
                                                fontSize = 14.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) OnPrimary else OnBackground,
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            itemsIndexed(serviceGroups) { index, group ->
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = group.service.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )

                                    group.products.forEach { product ->
                                        // TODO: Aici vei înlocui textul simplu cu componenta optimizată SearchCardProductRow(product = product)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                                .padding(16.dp)
                                        ) {
                                            Text(text = "Product: ${product.name}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}