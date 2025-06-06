package com.example.scrollbooker.feature.profile.presentation.components

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ViewComfyAlt
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.CustomBadge
import com.example.scrollbooker.components.customized.ProductCard
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.nav.routes.MainRoute.Appointments
import com.example.scrollbooker.core.nav.routes.MainRoute.Inbox
import com.example.scrollbooker.feature.products.domain.model.ProductCardEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSecondary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.OnTertiary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.Secondary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.Tertiary
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ProfileTab(
    val route: String,
    val icon: ImageVector
)

@Composable
fun ProfileTabs() {
    val pagerState = rememberPagerState(initialPage = 0) { 5 }
    val selectedTabIndex = pagerState.currentPage

    val tabs = listOf(
        ProfileTab(route = "Posts", icon = Icons.Outlined.ViewComfyAlt),
        ProfileTab(route = "Products", icon = Icons.Outlined.ShoppingBag),
        ProfileTab(route = "Reposts", icon = Icons.Outlined.Repeat),
        ProfileTab(route = "Bookmarks", icon = Icons.Outlined.BookmarkBorder),
        ProfileTab(route = "Info", icon = Icons.Outlined.LocationOn)
    )

    TabRow(
        containerColor = Background,
        contentColor = OnSurfaceBG,
        indicator = {  tabPositions ->
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(1.5.dp)
                    .padding(horizontal = 10.dp)
                    .background(OnBackground)
            )
        },
        selectedTabIndex = selectedTabIndex
    ) {
        val coroutineScope = rememberCoroutineScope()

        tabs.forEachIndexed { index, item ->
            val isSelected = selectedTabIndex == index

            Tab(
                selected = isSelected,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if(index == 1) {
//                                CustomBadge(
//                                    content = 10,
//                                    containerColor = Secondary,
//                                    contentColor = OnSecondary
//                                )
                                Box(Modifier
                                    .offset(x = 15.dp, y = (-12).dp),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    Box(modifier = Modifier
                                        .width(9.dp)
                                        .height(9.dp)
                                        .clip(CircleShape)
                                        .background(Error)
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = if(isSelected) OnBackground else Color.Gray
                        )
                    }
                }
            )
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        when(page) {
            0 -> Column(Modifier.fillMaxSize()) {
                Text("Posts Screen")
            }
            1 -> Column(Modifier.fillMaxSize().padding(horizontal = BasePadding)) {
                LazyColumn {
                    items(10) { item ->
                        ProductCard(
                            product = Product(
                                id = 1,
                                name = "Tuns Special",
                                description = "Ceva descriere despre acest produs",
                                duration = 60,
                                price = BigDecimal(100),
                                priceWithDiscount = BigDecimal(50),
                                discount = BigDecimal(50),
                            ),
                            mode = ProductCardEnum.CLIENT,
                            onNavigate = {}
                        )
                    }
                }
            }
            2 -> Column(Modifier.fillMaxSize()) {
                Text("Saves Screen")
            }
            3 -> Column(Modifier.fillMaxSize()) {
                Text("Reports Screen")
            }
            4 -> Column(Modifier.fillMaxSize()) {
                Text("Info Screen")
            }
        }
    }
}