package com.example.scrollbooker.ui.myBusiness
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.navigation.navigators.MyBusinessNavigator
import com.example.scrollbooker.ui.UserPermissionsController
import com.example.scrollbooker.ui.myBusiness.myProducts.components.MyBusinessCard

data class BusinessCard(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val permission: PermissionEnum,
    val navigate: () -> Unit,
)

@Composable
fun MyBusinessScreen(
    permissionsController: UserPermissionsController,
    myBusinessNavigate: MyBusinessNavigator,
    onBack: () -> Unit
) {
    val pages = listOf(
        BusinessCard(
            title = stringResource(R.string.location),
            description = stringResource(R.string.businessLocationDetails),
            icon = Icons.Outlined.LocationOn,
            permission = PermissionEnum.MY_BUSINESS_LOCATION_VIEW,
            navigate = { myBusinessNavigate.toMyBusinessLocation() },
        ),
        BusinessCard(
            title = "Subscription",
            description = stringResource(R.string.userScheduleDetails),
            icon = Icons.Outlined.Schedule,
            permission = PermissionEnum.MY_SUBSCRIPTION_VIEW,
            navigate = { myBusinessNavigate.toMySubscription() },
        ),
        BusinessCard(
            title = stringResource(R.string.scheduleShort),
            description = stringResource(R.string.userScheduleDetails),
            icon = Icons.Outlined.Schedule,
            permission = PermissionEnum.MY_SCHEDULES_VIEW,
            navigate = { myBusinessNavigate.toMySchedules() },
        ),
        BusinessCard(
            title = stringResource(R.string.services),
            description = stringResource(R.string.servicesDetails),
            icon = Icons.Outlined.Book,
            navigate = { myBusinessNavigate.toMyServices() },
            permission = PermissionEnum.MY_SERVICES_VIEW
        ),
        BusinessCard(
            title = stringResource(R.string.products),
            description = stringResource(R.string.userProductsDetails),
            icon = Icons.Outlined.ShoppingBag,
            permission = PermissionEnum.MY_PRODUCTS_VIEW,
            navigate = { myBusinessNavigate.toMyProducts() },
        ),
        BusinessCard(
            title = stringResource(R.string.currency),
            description = stringResource(R.string.chooseDesiredCurrencies),
            icon = Icons.Outlined.Money,
            permission = PermissionEnum.MY_CURRENCIES_VIEW,
            navigate = { myBusinessNavigate.toMyCurrencies() },
        ),
        BusinessCard(
            title = stringResource(R.string.calendar),
            description = stringResource(R.string.servicesDetails),
            icon = Icons.Outlined.CalendarToday,
            navigate = { myBusinessNavigate.toMyCalendar() },
            permission = PermissionEnum.MY_CALENDAR_VIEW,
        ),
        BusinessCard(
            title = stringResource(R.string.employees),
            description = stringResource(R.string.servicesDetails),
            icon = Icons.Outlined.PeopleOutline,
            navigate = { myBusinessNavigate.toMyEmployees() },
            permission = PermissionEnum.MY_EMPLOYEES_VIEW
        ),
        BusinessCard(
            title = stringResource(R.string.employmentRequests),
            description = stringResource(R.string.servicesDetails),
            icon = Icons.Outlined.Repeat,
            navigate = { myBusinessNavigate.toMyEmploymentRequests() },
            permission = PermissionEnum.MY_EMPLOYMENT_REQUESTS_VIEW
        )
    )

    Layout(
        headerTitle = stringResource(R.string.myBusiness),
        onBack = onBack
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            val visiblePages = pages.filter { permissionsController.hasPermission(it.permission) }

            items(visiblePages) { page ->
                MyBusinessCard(
                    title = page.title,
                    icon = page.icon,
                    description = page.description,
                    onClick = page.navigate
                )
            }
        }
    }
}