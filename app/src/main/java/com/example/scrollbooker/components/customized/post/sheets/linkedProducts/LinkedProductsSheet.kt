package com.example.scrollbooker.components.customized.post.sheets.linkedProducts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.components.customized.productCard.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun LinkedProductsSheet(
    modifier: Modifier = Modifier,
    postId: Int,
    onClose: () -> Unit,
    onNavigateToBooking: (product: Product) -> Unit
) {
    val viewModel: LinkedProductsViewModel = hiltViewModel()

    LaunchedEffect(postId) {
        viewModel.setPostId(postId)
    }

    val state by viewModel.productsState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.85f)
    ) {
        SheetHeader(
            title = stringResource(R.string.recommendedServices),
            onClose = onClose
        )

        when (val currentState = state) {
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                val products = currentState.data

                if (products.isEmpty()) {
                    EmptyScreen(
                        message = stringResource(R.string.notFoundServices),
                        icon = painterResource(R.drawable.ic_shopping_outline),
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = BasePadding)
                    ) {
                        itemsIndexed(products) { index, product ->
                            ProductCard(
                                modifier = Modifier.padding(horizontal = BasePadding),
                                product = product,
                                onOpenProductDetail = {},
                                onNavigateToBooking = onNavigateToBooking
                            )

                            if (index < products.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(
                                        vertical = SpacingXL,
                                        horizontal = BasePadding
                                    ),
                                    color = Divider,
                                    thickness = 0.55.dp
                                )
                            }
                        }

                        item {
                            MainButtonOutlined(
                                modifier = Modifier.padding(BasePadding),
                                title = stringResource(R.string.seeAllServices),
                                onClick = {  }
                            )
                        }
                    }
                }
            }
        }
    }
}