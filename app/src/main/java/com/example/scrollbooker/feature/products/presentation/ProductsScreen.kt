package com.example.scrollbooker.feature.products.presentation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.customized.ProductCard
import com.example.scrollbooker.feature.products.domain.model.ProductCardEnum
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.core.util.LoadingScreen

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel,
    userId: Int,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    LaunchedEffect(userId) {
        viewModel.loadProducts(userId)
    }

    val state = viewModel.products.collectAsLazyPagingItems()

    Layout(
        headerTitle = stringResource(R.string.myProducts),
        onBack = onBack
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            state.apply {
                when(loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen()
                    is LoadState.Error -> ErrorScreen()
                    is LoadState.NotLoading -> Unit
                }
            }

            LazyColumn(Modifier.weight(1f)) {
                items(count = state.itemCount) { index ->
                    val product = state[index]
                    product?.let { Box(Modifier.fillMaxSize()) {
                        ProductCard(
                            product = it,
                            mode = ProductCardEnum.OWNER,
                            onNavigate = onNavigate
                        )
                    } }
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
//            MainButton(
//                onClick = { onNavigate(MainRoute.AddProduct.route) },
//                title = stringResource(id = R.string.createNewProduct)
//            )
        }
    }
}