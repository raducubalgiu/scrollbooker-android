package com.example.scrollbooker.ui.shared.products

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.customized.ProductCard
import com.example.scrollbooker.components.customized.ProductCardShimmer
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import androidx.compose.runtime.getValue
import com.example.scrollbooker.ui.shared.products.components.EmployeesList
import com.example.scrollbooker.ui.theme.Divider

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun UserProductsServiceTab(
    viewModel: UserProductsViewModel,
    employees: List<UserSocial>,
    serviceId: Int,
    userId: Int,
    onSelect: (Product) -> Unit
    //onNavigateToCalendar: (NavigateCalendarParam) -> Unit
) {
    val firstEmployeesId = employees.firstOrNull()?.id
    val productsState = viewModel.loadProducts(serviceId, userId, firstEmployeesId).collectAsLazyPagingItems()
    val selectedProducts by viewModel.selectedProducts.collectAsState()

    Column(Modifier.fillMaxSize()) {
        productsState.apply {
            when (loadState.refresh) {
                is LoadState.Loading -> repeat(5) { ProductCardShimmer() }
                is LoadState.Error -> ErrorScreen()
                is LoadState.NotLoading -> {
                    if(productsState.itemCount == 0) {
                        EmptyScreen(
                            modifier = Modifier.padding(top = 30.dp),
                            arrangement = Arrangement.Top,
                            message = stringResource(R.string.noProductsFound),
                            icon = painterResource(R.drawable.ic_shopping_outline)
                        )
                    }

                    LazyColumn(Modifier.weight(1f)) {
                        item {
                            if(employees.isNotEmpty()) {
                                EmployeesList(
                                    employees = employees,
                                    onSetEmployee = {
                                        viewModel.loadProducts(
                                            serviceId = serviceId,
                                            userId = userId,
                                            employeeId = it
                                        )
                                    }
                                )
                            }
                        }

                        items(productsState.itemCount) { index ->
                            productsState[index]?.let { product ->
                                ProductCard(
                                    product = product,
                                    isSelected = product in selectedProducts,
                                    onSelect = onSelect
                                    //onNavigateToCalendar = onNavigateToCalendar
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
                                    is LoadState.Loading -> LoadMoreSpinner()
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