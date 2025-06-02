package com.example.scrollbooker.feature.products.presentation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Header
import com.example.scrollbooker.components.Layout
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.feature.products.presentation.ProductsViewModel

@Composable
fun ProductsScreen(userId: Int, navController: NavController) {
    val viewModel: ProductsViewModel = hiltViewModel()

    LaunchedEffect(userId) {
        viewModel.loadProducts(userId)
    }

    val lazyPagingItems = viewModel.products.collectAsLazyPagingItems()

    Layout {
        Header(
            navController = navController,
            title = stringResource(R.string.myProducts),
        )
        LazyColumn {
            items(count = lazyPagingItems.itemCount) { index ->
                val product = lazyPagingItems[index]
                product?.let { Box(Modifier.fillMaxSize()) { Text(it.name) } }
            }

            lazyPagingItems.apply {
                when {
                    loadState.append is LoadState.Loading -> {
                        item { CircularProgressIndicator() }
                    }
                    loadState.append is LoadState.Error -> {
                        item { Text("Eroare la incarcare") }
                    }
                }
            }
        }
    }
}