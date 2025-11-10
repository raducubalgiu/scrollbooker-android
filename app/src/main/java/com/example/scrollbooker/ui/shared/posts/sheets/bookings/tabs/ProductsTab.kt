package com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.TabsViewModel
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.components.BookingsSheetFooter
import com.example.scrollbooker.ui.shared.products.UserProductsServiceTabs
import com.example.scrollbooker.ui.shared.products.UserProductsViewModel
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
fun ProductsTab(
    productsViewModel: UserProductsViewModel,
    onSelect: (Product) -> Unit,
    userId: Int,
    selectedProducts: Set<Product>,
    totalPrice: BigDecimal,
    totalDuration: Int,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            UserProductsServiceTabs(
                viewModel = productsViewModel,
                userId = userId,
                onSelect = onSelect
            )
        }

        BookingsSheetFooter(
            selectedProducts = selectedProducts,
            totalPrice = totalPrice,
            totalDuration = totalDuration,
            onNext = onNext
        )
    }
}