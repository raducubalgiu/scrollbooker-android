package com.example.scrollbooker.ui.search.businessProfile.sections.services
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.enums.ProductTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.search.components.card.SearchCardProductRow
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.headlineSmall
import java.math.BigDecimal

@Composable
fun BusinessServicesSection() {
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
            currencyId = 1,
            filters = emptyList(),
            type = ProductTypeEnum.SINGLE,
            sessionsCount = null,
            validityDays = null,
            canBeBooked = true
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
            currencyId = 1,
            filters = emptyList(),
            type = ProductTypeEnum.SINGLE,
            sessionsCount = null,
            validityDays = null,
            canBeBooked = true
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
            currencyId = 1,
            filters = emptyList(),
            type = ProductTypeEnum.SINGLE,
            sessionsCount = null,
            validityDays = null,
            canBeBooked = true
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
            currencyId = 1,
            filters = emptyList(),
            type = ProductTypeEnum.SINGLE,
            sessionsCount = null,
            validityDays = null,
            canBeBooked = true
        )
    )

    Column(modifier = Modifier.padding(BasePadding)) {
        Text(
            text = stringResource(R.string.services),
            style = headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(SpacingXL))

        products.forEachIndexed { index, prod ->
            SearchCardProductRow(product = prod)

            if(index < products.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = SpacingS),
                    color = Divider,
                    thickness = 0.55.dp
                )
            }
        }

        MainButtonOutlined(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BasePadding),
            contentPadding = PaddingValues(
                vertical = BasePadding
            ),
            shape = ShapeDefaults.Medium,
            title = stringResource(R.string.seeAllServices),
            onClick = {  }
        )
    }
}