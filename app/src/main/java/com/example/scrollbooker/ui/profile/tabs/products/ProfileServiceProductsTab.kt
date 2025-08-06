package com.example.scrollbooker.ui.profile.tabs.products

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductCardEnum
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.navigation.navigators.NavigateCalendarParam
import com.example.scrollbooker.ui.profile.tabs.ProfileTabViewModel
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileServiceProductsTab(
    viewModel: ProfileTabViewModel,
    employees: List<UserSocial>,
    serviceId: Int,
    userId: Int,
    onNavigateToCalendar: (NavigateCalendarParam) -> Unit
) {
    val productsState = viewModel.loadProducts(serviceId, userId)
        .collectAsLazyPagingItems()
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

    Column(Modifier.fillMaxSize()) {
        if(employees.isNotEmpty()) {
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
                                url = "${emp.avatar}",
                                rating = "${emp.ratingsAverage}"
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
        }

        productsState.apply {
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    LoadingScreen(
                        modifier = Modifier.padding(top = 50.dp),
                        arrangement = Arrangement.Top
                    )
                }
                is LoadState.Error -> ErrorScreen()
                is LoadState.NotLoading -> {
                    if(productsState.itemCount == 0) {
                        EmptyScreen(
                            modifier = Modifier.padding(top = 30.dp),
                            arrangement = Arrangement.Top,
                            message = stringResource(R.string.noProductsFound),
                            icon = painterResource(R.drawable.ic_shopping_outline)
                        )
                    }

                    LazyColumn(Modifier.weight(1f)) {
                        items(productsState.itemCount) { index ->
                            productsState[index]?.let { product ->
                                ProductCard(
                                    product = product,
                                    mode = ProductCardEnum.CLIENT,
                                    onNavigateToEdit = {},
                                    isLoadingDelete = false,
                                    onDeleteProduct = {},
                                    onNavigateToCalendar = onNavigateToCalendar
                                )

                                if(index < productsState.itemCount - 1) {
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

                        item {
                            productsState.apply {
                                when (loadState.append) {
                                    is LoadState.Loading -> {
                                        LoadMoreSpinner()
                                    }

                                    is LoadState.Error -> {
                                        Text("Eroare la incarcare")
                                    }

                                    is LoadState.NotLoading -> Unit
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}