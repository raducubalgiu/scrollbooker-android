package com.example.scrollbooker.screens.profile.myBusiness.myProducts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.entity.products.domain.model.ProductCardEnum

@Composable
fun ProductsTab(
    myProductsViewModel: MyProductsViewModel,
    serviceId: Int
) {
    val state = myProductsViewModel.getProductsFlow(serviceId).collectAsLazyPagingItems()

    Column(Modifier.fillMaxSize()) {
        state.apply {
            when (loadState.refresh) {
                is LoadState.Loading -> LoadingScreen()
                is LoadState.Error -> ErrorScreen()
                is LoadState.NotLoading -> {
                    if(state.itemCount == 0) {
                        EmptyScreen(
                            message = stringResource(R.string.noProductsFound),
                            icon = Icons.Outlined.ShoppingBag
                        )
                    }
                }
            }
            LazyColumn(Modifier.weight(1f)) {
                items(count = state.itemCount) { index ->
                    val product = state[index]

                    product?.let {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = BasePadding)
                        ) {
                            ProductCard(
                                product = it,
                                mode = ProductCardEnum.OWNER,
                                onNavigate = {}
                            )
                        }
                    }
                }

                state.apply {
                    when (loadState.append) {
                        is LoadState.Loading -> {
                            item { LoadMoreSpinner() }
                        }

                        is LoadState.Error -> {
                            item { Text("Eroare la incarcare") }
                        }

                        is LoadState.NotLoading -> Unit
                    }
                }
            }
        }
    }
}