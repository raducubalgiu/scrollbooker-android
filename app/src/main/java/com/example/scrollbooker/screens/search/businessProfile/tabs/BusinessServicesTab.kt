package com.example.scrollbooker.screens.search.businessProfile.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.customized.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.products.domain.model.Product
import com.example.scrollbooker.entity.products.domain.model.ProductCardEnum
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun BusinessServicesTab() {
    val products = listOf(
        Product(
            id = 1,
            name = "Tuns clasic",
            description = "defe",
            duration = 30,
            price = BigDecimal("50"),
            priceWithDiscount = BigDecimal("50"),
            discount = BigDecimal("0"),
            userId = 2,
            serviceId = 2,
            businessId = 1,
            currencyId = 1
        ),
        Product(
            id = 2,
            name = "Skin fade (zero in laterale)",
            description = "defe",
            duration = 30,
            price = BigDecimal("60"),
            priceWithDiscount = BigDecimal("60"),
            discount = BigDecimal("0"),
            userId = 2,
            serviceId = 2,
            businessId = 1,
            currencyId = 1
        ),
        Product(
            id = 3,
            name = "Tuns Barba",
            description = "defe",
            duration = 20,
            price = BigDecimal("45"),
            priceWithDiscount = BigDecimal("45"),
            discount = BigDecimal("0"),
            userId = 2,
            serviceId = 2,
            businessId = 1,
            currencyId = 1
        ),
        Product(
            id = 4,
            name = "Pachet Premium",
            description = "defe",
            duration = 50,
            price = BigDecimal("100"),
            priceWithDiscount = BigDecimal("100"),
            discount = BigDecimal("0"),
            userId = 2,
            serviceId = 2,
            businessId = 1,
            currencyId = 1
        )
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = BasePadding)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.services),
            style = headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(BasePadding))

        LazyColumn(
            modifier = Modifier.height(420.dp)
        ) {
            itemsIndexed(products) { index, product ->
                ProductCard(
                    product =product,
                    mode = ProductCardEnum.CLIENT,
                    onNavigateToEdit = {},
                    isLoadingDelete = false,
                    onDeleteProduct = {},
                    onNavigateToCalendar = {}
                )

                if(index < products.size - 1) {
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

        Spacer(Modifier.height(BasePadding))

        MainButtonOutlined(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BasePadding),
            shape = ShapeDefaults.Small,
            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = 15.dp
            ),
            title = "Vezi mai mult",
            onClick = {}
        )
    }
}