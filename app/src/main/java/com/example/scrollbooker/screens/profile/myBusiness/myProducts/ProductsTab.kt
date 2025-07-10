package com.example.scrollbooker.screens.profile.myBusiness.myProducts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.entity.products.domain.model.ProductCardEnum
import com.example.scrollbooker.screens.appointments.components.AppointmentCard
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun ProductsTab(
    myProductsViewModel: MyProductsViewModel,
    serviceId: Int
) {
    val productsState = myProductsViewModel.loadProducts(serviceId).collectAsLazyPagingItems()

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
                                    mode = ProductCardEnum.OWNER,
                                    onNavigate = {}
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