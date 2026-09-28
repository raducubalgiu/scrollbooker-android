package com.example.scrollbooker.ui.search.businessProfile.sections.services
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.tabs.ServiceTab
import com.example.scrollbooker.components.customized.productCard.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun BusinessServicesSection(
    products: UserProducts,
    onNavigateToBookingFromProfile: () -> Unit,
    onNavigateToBookingFromProduct: (product: Product) -> Unit
) {
    val serviceGroups = products.data
    val totalCount = products.totalCount
    var currentPage by remember { mutableStateOf(0) }

    Column(modifier = Modifier.padding(BasePadding)) {
        Text(
            text = stringResource(R.string.services),
            style = titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(BasePadding))

        if (serviceGroups.isNotEmpty()) {
            ScrollableTabRow(
                selectedTabIndex = currentPage,
                containerColor = Background,
                divider = {},
                indicator = { _ -> Box(Modifier.size(0.dp)) },
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                serviceGroups.forEachIndexed { index, group ->
                    val isSelected = currentPage == index

                    ServiceTab(
                        isSelected = isSelected,
                        serviceName = group.service.shortName,
                        onClick = { currentPage = index },
                    )
                }
            }

            HorizontalDivider(
                color = Divider,
                thickness = 0.55.dp
            )

            Spacer(Modifier.height(SpacingS))

            AnimatedContent(
                targetState = currentPage,
                modifier = Modifier.fillMaxWidth(),
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220)) using
                        SizeTransform(clip = false)
                },
                label = "BusinessServicesPage"
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
                            shouldToggleDescription = true,
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
                text = stringResource(R.string.notFoundServices),
                style = bodyMedium,
                modifier = Modifier.padding(vertical = BasePadding)
            )
        }
    }
}
