package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.runtime.getValue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun ProductsTab(
    myProductsViewModel: MyProductsViewModel,
    serviceId: Int,
    onNavigateToEdit: (Int) -> Unit
) {
    val reloadKey by myProductsViewModel.productsReloadTrigger

    val productsState = remember(reloadKey) {
        myProductsViewModel.loadProducts(serviceId, employeeId = null)
    }.collectAsLazyPagingItems()

    val selectedProduct by myProductsViewModel.selectedProduct.collectAsState()
    val isSaving by myProductsViewModel.isSaving.collectAsState()

    Column(Modifier.fillMaxSize()) {
        productsState.apply {
            when (loadState.refresh) {
                is LoadState.Loading -> LoadingScreen()
                is LoadState.Error -> ErrorScreen()
                is LoadState.NotLoading -> {
                    if(productsState.itemCount == 0) {
                        EmptyScreen(
                            message = stringResource(R.string.noProductsFound),
                            icon = painterResource(R.drawable.ic_shopping_outline)
                        )
                    }

                    LazyColumn(Modifier.weight(1f)) {
                        item { Spacer(Modifier.height(SpacingM)) }

                        items(productsState.itemCount) { index ->
                            productsState[index]?.let { product ->
                                ProductCard(
                                    product = product,
                                    onNavigateToEdit = onNavigateToEdit,
                                    isLoadingDelete = isSaving && selectedProduct?.id == product.id,
                                    onNavigateToCalendar = {},
                                    onDeleteProduct = { productId: Int ->
                                        myProductsViewModel.deleteProduct(product, serviceId)
                                    },
                                )

                                if(index < productsState.itemCount - 1) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = BasePadding)
                                            .height(0.55.dp)
                                            .background(Divider)
                                    )
                                }
                            }
                        }

                        item {
                            productsState.apply {
                                when (loadState.append) {
                                    is LoadState.Loading -> {
                                        LoadMoreSpinner()
                                    }

                                    is LoadState.Error -> {
                                        Text("Eroare la incarcare")
                                    }

                                    is LoadState.NotLoading -> Unit
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}