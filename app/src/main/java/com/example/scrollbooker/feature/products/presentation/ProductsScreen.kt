package com.example.scrollbooker.feature.products.presentation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.components.core.Layout
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.MainButton
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun ProductsScreen(userId: Int, navController: NavController) {
    val viewModel: ProductsViewModel = hiltViewModel()

    LaunchedEffect(userId) {
        viewModel.loadProducts(userId)
    }

    val lazyPagingItems = viewModel.products.collectAsLazyPagingItems()

    Layout {
//        Header(
//            navController = navController,
//            title = stringResource(R.string.myProducts),
//        )
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = BasePadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(count = lazyPagingItems.itemCount) { index ->
                    val product = lazyPagingItems[index]
                    product?.let { Box(Modifier.fillMaxSize()) { Text(it.name) } }
                }

                lazyPagingItems.apply {
                    when (loadState.append) {
                        is LoadState.Loading -> {
                            item { CircularProgressIndicator() }
                        }

                        is LoadState.Error -> {
                            item { Text("Eroare la incarcare") }
                        }

                        is LoadState.NotLoading -> {}
                    }
                }
            }
            MainButton(
                onClick = { navController.navigate(MainRoute.AddProduct.route) },
                title = stringResource(id = R.string.createNewProduct)
            )
        }
    }
}