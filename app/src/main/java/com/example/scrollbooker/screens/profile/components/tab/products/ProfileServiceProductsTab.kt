package com.example.scrollbooker.screens.profile.components.tab.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.ProductCard
import com.example.scrollbooker.components.customized.ProductCardNavigationData
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.products.domain.model.ProductCardEnum
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun ProfileServiceProductsTab(
    viewModel: ProfileProductsTabViewModel,
    serviceId: Int,
    userId: Int,
    onNavigateToCalendar: (ProductCardNavigationData) -> Unit
) {
    val productsState = viewModel.loadProducts(serviceId, userId).collectAsLazyPagingItems()

    data class Emp(
        val avatar: String,
        val fullName: String,
        val rating: String
    )

    val employees = listOf(
        Emp(avatar = "https://media.scrollbooker.ro/avatar-male-9.jpeg", fullName ="Cristian Ionel", rating= "4.9"),
        Emp(avatar = "https://media.scrollbooker.ro/avatar-male-10.jpg", fullName ="Radu Dan", rating = "4.3"),
        Emp(avatar = "https://media.scrollbooker.ro/avatar-male-11.jpeg", fullName ="Laur Oprea", rating = "4.2"),
        Emp(avatar = "https://media.scrollbooker.ro/avatar-male-12.jpg", fullName ="Mihai Gandac", rating = "4.9"),
        Emp(avatar = "https://media.scrollbooker.ro/avatar-male-14.jpeg", fullName ="Gigi Corsicanu", rating = "3.2"),
    )

    Column(Modifier.fillMaxSize()) {
        productsState.apply {
            when (loadState.refresh) {
                is LoadState.Loading -> LoadingScreen()
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

                    LazyRow(modifier = Modifier.padding(BasePadding)) {
                        itemsIndexed(employees) { index, emp ->
                            Column(
                                modifier = Modifier
                                    .width(120.dp)
                                    .fillMaxWidth()
                                    .background(if(index == 0) Primary.copy(alpha = 0.2f) else Color.Transparent, shape = ShapeDefaults.Small)
                                    .padding(vertical = SpacingXS),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AvatarWithRating(
                                    modifier = Modifier.size(70.dp),
                                    url = emp.avatar,
                                    rating = emp.rating
                                )
                                Spacer(Modifier.height(20.dp))
                                Text(
                                    text = emp.fullName,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
//                            if(index < employees.size) {
//                                VerticalDivider()
//                            }
                        }
                    }

                    HorizontalDivider(color = Divider, thickness = 0.55.dp)

                    LazyColumn(Modifier.weight(1f)) {
                        items(productsState.itemCount) { index ->
                            productsState[index]?.let { product ->
                                ProductCard(
                                    product = product,
                                    mode = ProductCardEnum.CLIENT,
                                    onNavigateToEdit = {},
                                    isLoadingDelete = false,
                                    onDeleteProduct = {},
                                    onNavigateToCalendar = onNavigateToCalendar
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