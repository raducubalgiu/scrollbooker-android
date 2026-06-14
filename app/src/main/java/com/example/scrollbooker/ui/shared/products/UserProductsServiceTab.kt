package com.example.scrollbooker.ui.shared.products

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceEmployee

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun UserProductsServiceTab(
    viewModel: UserProductsViewModel,
    selectedProducts: Set<Product>,
    employees: List<ServiceEmployee>,
    serviceId: Int,
    userId: Int,
    onSelect: (Product) -> Unit
) {
//    val firstEmployeesId = employees.firstOrNull()?.id
//    val productsState = viewModel.loadProducts(serviceId, userId, firstEmployeesId).collectAsLazyPagingItems()
//
//    val refresh = productsState.loadState.refresh
//    val append = productsState.loadState.append
//
//    Column(Modifier.fillMaxSize()) {
//        when (refresh) {
//            is LoadState.Loading -> LoadingScreen()
//            is LoadState.Error -> ErrorScreen()
//            is LoadState.NotLoading -> {
//                if(productsState.itemCount == 0) {
//                    EmptyScreen(
//                        modifier = Modifier.padding(top = 30.dp),
//                        arrangement = Arrangement.Top,
//                        message = stringResource(R.string.noProductsFound),
//                        icon = painterResource(R.drawable.ic_shopping_outline)
//                    )
//                }
//
//                LazyColumn(Modifier.weight(1f)) {
//                    item {
//                        if(employees.isNotEmpty()) {
//                            EmployeesList(
//                                employees = employees,
//                                onSetEmployee = {
//                                    viewModel.loadProducts(
//                                        serviceId = serviceId,
//                                        userId = userId,
//                                        employeeId = it
//                                    )
//                                }
//                            )
//                        }
//                    }
//
//                    items(productsState.itemCount) { index ->
//                        productsState[index]?.let { product ->
//                            ProductCard(
//                                product = product,
//                                isSelected = product in selectedProducts,
//                                onSelect = onSelect
//                            )
//
//                            if(index < productsState.itemCount - 1) {
//                                HorizontalDivider(
//                                    modifier = Modifier.padding(horizontal = BasePadding),
//                                    color = Divider,
//                                    thickness = 0.55.dp
//                                )
//                            }
//                        }
//                    }
//
//                    item {
//                        when (append) {
//                            is LoadState.Loading -> LoadMoreSpinner()
//                            is LoadState.Error -> {
//                                Text("Eroare la incarcare")
//                            }
//
//                            is LoadState.NotLoading -> Unit
//                        }
//                    }
//                }
//            }
//        }
//    }
}