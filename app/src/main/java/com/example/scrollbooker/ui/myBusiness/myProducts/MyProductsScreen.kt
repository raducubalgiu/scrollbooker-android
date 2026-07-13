package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.booking.services.BookingServicesTabs
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.customized.productCard.ProductCard
import com.example.scrollbooker.core.snackbar.CustomSnackBar
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun MyProductsScreen(
    viewModel: MyProductsViewModel,
    onBack: () -> Unit,
    onNavigateAddProduct: () -> Unit,
    onNavigateEditProduct: (Int, Int) -> Unit
) {
    val productsState by viewModel.productsState.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val hostState = remember { SnackbarHostState() }
    val snackBarController = rememberSnackBarController(hostState)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackBarController.show(event)
        }
    }

    Scaffold(
        topBar = {
            Header(
                title = stringResource(R.string.myServices),
                onBack = onBack,
                actions = {
                    IconButton(onClick = onNavigateAddProduct) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        snackbarHost = {
            CustomSnackBar(
                hostState = hostState,
                type = snackBarController.currentType
            )
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when(val state = productsState) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    val serviceGroups = state.data.data

                    val lazyItemIndexToGroupIndexMap = remember(serviceGroups) {
                        mutableListOf<Int>().apply {
                            serviceGroups.forEachIndexed { groupIndex, group ->
                                add(groupIndex)

                                if (group.products.isEmpty()) {
                                    add(groupIndex)
                                } else {
                                    repeat(group.products.size) {
                                        add(groupIndex)
                                    }
                                }
                            }
                        }
                    }

                    val groupIndexToLazyItemIndexMap = remember(serviceGroups) {
                        mutableListOf<Int>().apply {
                            var currentLazyIndex = 0
                            serviceGroups.forEach { group ->
                                add(currentLazyIndex)
                                val itemsInGroup = 1 + if (group.products.isEmpty()) 1 else group.products.size
                                currentLazyIndex += itemsInGroup
                            }
                        }
                    }

                    val activeTabIndexProvider = remember(lazyItemIndexToGroupIndexMap) {
                        derivedStateOf {
                            val visibleItems = listState.layoutInfo.visibleItemsInfo
                            if (visibleItems.isEmpty() || lazyItemIndexToGroupIndexMap.isEmpty()) {
                                0
                            } else {
                                val firstVisibleItem = visibleItems.firstOrNull { item ->
                                    item.offset <= 0 && item.offset + item.size > 0
                                } ?: visibleItems.first()

                                val groupIndex = lazyItemIndexToGroupIndexMap.getOrNull(firstVisibleItem.index) ?: 0
                                groupIndex.coerceIn(0, serviceGroups.lastIndex)
                            }
                        }
                    }

                    Column(Modifier.fillMaxSize()) {
                        if (serviceGroups.isNotEmpty()) {
                            BookingServicesTabs(
                                activeTabIndexProvider = { activeTabIndexProvider.value },
                                onTabChange = { tabIndex ->
                                    scope.launch {
                                        val targetLazyIndex = groupIndexToLazyItemIndexMap.getOrNull(tabIndex) ?: 0
                                        listState.animateScrollToItem(targetLazyIndex)
                                    }
                                },
                                serviceGroups = serviceGroups
                            )
                        }

                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            serviceGroups.forEach { group ->
                                item(key = "section_${group.service.id}") {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp, bottom = BasePadding),
                                        text = group.service.name,
                                        style = titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = OnBackground
                                    )
                                }

                                if (group.products.isEmpty()) {
                                    item(key = "empty_section_${group.service.id}") {
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = BasePadding),
                                            text = stringResource(R.string.notFoundServicesForThisCategory),
                                            color = Color.Gray
                                        )
                                    }
                                }

                                items(
                                    items = group.products,
                                    key = { product -> product.id }
                                ) { product ->
                                    ProductCard(
                                        product = product,
                                        onDeleteProduct = { viewModel.deleteProduct(it) },
                                        isLoadingDelete = isSaving,
                                        onOpenProductDetail = {},
                                        displayEditableActions = true
                                    )

                                    if (group.products.lastOrNull()?.id != product.id) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = BasePadding),
                                            color = Divider,
                                            thickness = 0.55.dp
                                        )
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