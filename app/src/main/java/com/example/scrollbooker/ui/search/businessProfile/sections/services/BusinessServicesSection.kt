package com.example.scrollbooker.ui.search.businessProfile.sections.services
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.customized.ProductCard.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleLarge
import kotlinx.coroutines.launch

@Composable
fun BusinessServicesSection(
    products: UserProducts,
    onNavigateToBookingFromProfile: () -> Unit,
    onNavigateToBookingFromProduct: (product: Product) -> Unit
) {
    val serviceGroups = products.data
    val totalCount = products.totalCount
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(BasePadding)) {
        Text(
            text = stringResource(R.string.services),
            style = titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(BasePadding))

        if (serviceGroups.isNotEmpty()) {
            val pagerState = rememberPagerState { serviceGroups.size }

            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Background,
                divider = {},
                indicator = { _ -> Box(Modifier.size(0.dp)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                serviceGroups.forEachIndexed { index, group ->
                    val isSelected = pagerState.currentPage == index

                    Tab(
                        selected = isSelected,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        modifier = Modifier
                            .clip(ShapeDefaults.Medium)
                            .background(if (isSelected) SurfaceBG else Background)
                            .height(42.dp),
                        text = {
                            val tabTitle = group.service.shortName
                            Text(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                text = tabTitle,
                                fontSize = 16.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) OnSurfaceBG else OnBackground,
                            )
                        }
                    )
                }
            }

            HorizontalDivider(
                color = Divider,
                thickness = 0.55.dp
            )

            Spacer(Modifier.height(SpacingS))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) { page ->
                val currentGroupProducts = serviceGroups[page].products

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = SpacingS)
                ) {
                    currentGroupProducts.forEachIndexed { index, product ->
                        ProductCard(
                            product = product,
                            displayDescription = false,
                            onOpenProductDetail = {},
                            onNavigateToBooking = onNavigateToBookingFromProduct
                        )

                        if (index < currentGroupProducts.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = BasePadding),
                                color = Divider,
                                thickness = 0.55.dp
                            )
                        }
                    }

                    val shouldShowViewMore = (serviceGroups.size * 5) < totalCount
                    if (shouldShowViewMore) {
                        MainButtonOutlined(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = BasePadding),
                            contentPadding = PaddingValues(vertical = BasePadding),
                            shape = ShapeDefaults.Medium,
                            title = "Vezi toate cele $totalCount servicii",
                            onClick = onNavigateToBookingFromProfile
                        )
                    }
                }
            }
        } else {
            Text(
                text = "Nu există servicii disponibile.",
                style = bodyMedium,
                modifier = Modifier.padding(vertical = BasePadding)
            )
        }
    }
}