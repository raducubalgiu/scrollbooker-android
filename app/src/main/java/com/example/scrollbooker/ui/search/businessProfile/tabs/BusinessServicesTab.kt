package com.example.scrollbooker.ui.search.businessProfile.tabs

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.customized.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductCardEnum
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.headlineMedium
import java.math.BigDecimal

@SuppressLint("ConfigurationScreenWidthHeight")
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
    val lazyRowListState = rememberLazyListState()

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val cardWidth = remember {
        (screenWidth / 3 - BasePadding)
    }

    var selectedEmployeesIndex by remember { mutableIntStateOf(0) }
    val scrollOffset = with(LocalDensity.current) {
        -BasePadding.roundToPx()
    }

    LaunchedEffect(selectedEmployeesIndex) {
        lazyRowListState.animateScrollToItem(
            index = selectedEmployeesIndex,
            scrollOffset = scrollOffset
        )
    }

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

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = BasePadding)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.services),
            style = headlineMedium,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(BasePadding))

        LazyRow(
            state = lazyRowListState,
            modifier = Modifier.padding(vertical = BasePadding)
        ) {
            item { Spacer(Modifier.width(BasePadding)) }

            itemsIndexed(employees) { index, emp ->
                val isSelected = selectedEmployeesIndex == index

                val animatedBgColor by animateColorAsState(
                    targetValue = if(isSelected) Primary.copy(alpha = 0.2f) else Color.Transparent,
                    animationSpec = tween(durationMillis = 300),
                    label = "Card Selection Background"
                )

                Box(
                    modifier = Modifier
                        .width(cardWidth)
                        .clip(ShapeDefaults.Medium)
                        .background(animatedBgColor)
                        .padding(vertical = SpacingS)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            selectedEmployeesIndex = index
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AvatarWithRating(
                            modifier = Modifier.size(90.dp),
                            url = emp.avatar,
                            rating = emp.rating
                        )
                        Spacer(Modifier.height(SpacingM))
                        Text(
                            text = emp.fullName,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.width(BasePadding))
            }
        }

        HorizontalDivider(color = Divider, thickness = 0.55.dp)

        LazyColumn(modifier = Modifier.height(420.dp)) {
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