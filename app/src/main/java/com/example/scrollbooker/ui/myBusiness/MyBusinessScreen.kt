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
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.ui.UserPermissionsController
import com.example.scrollbooker.ui.myBusiness.myProducts.components.MyBusinessCard
import androidx.compose.runtime.getValue
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.navigation.navigators.ProfileNavigator

data class BusinessCard(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val permission: PermissionEnum,
    val navigate: () -> Unit,
)

@Composable
fun MyBusinessScreen(
    viewModel: MyBusinessViewModel,
    permissionsController: UserPermissionsController,
    profileNavigate: ProfileNavigator,
    onBack: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val hasEmployees by viewModel.hasEmployees.collectAsStateWithLifecycle()
    val isEmployee by viewModel.isEmployee.collectAsStateWithLifecycle()

    val pages = listOf(
        BusinessCard(
            title = stringResource(R.string.businessDetails),
            description = stringResource(R.string.businessDetailsDescription),
            icon = Icons.Outlined.LocationOn,
            permission = PermissionEnum.MY_BUSINESS_LOCATION_VIEW,
            navigate = { profileNavigate.toMyBusinessDetails() },
        ),
        BusinessCard(
            title = stringResource(R.string.scheduleShort),
            description = stringResource(R.string.userScheduleDetails),
            icon = Icons.Outlined.Schedule,
            permission = PermissionEnum.MY_SCHEDULES_VIEW,
            navigate = { profileNavigate.toMySchedules() },
        ),
        BusinessCard(
            title = stringResource(R.string.categories),
            description = stringResource(R.string.categoryservicesDetails),
            icon = Icons.Outlined.Book,
            navigate = { profileNavigate.toMyServices() },
            permission = PermissionEnum.MY_SERVICES_VIEW
        ),
        BusinessCard(
            title = stringResource(R.string.services),
            description = stringResource(R.string.servicesDetails),
            icon = Icons.Outlined.ShoppingBag,
            permission = PermissionEnum.MY_PRODUCTS_VIEW,
            navigate = { profileNavigate.toMyProducts() },
        ),
        BusinessCard(
            title = stringResource(R.string.calendar),
            description = stringResource(R.string.calendarDetails),
            icon = Icons.Outlined.CalendarToday,
            navigate = { profileNavigate.toMyCalendar() },
            permission = PermissionEnum.MY_CALENDAR_VIEW,
        ),
        BusinessCard(
            title = stringResource(R.string.employees),
            description = stringResource(R.string.employeesAndEmploymentRequestsDetails),
            icon = Icons.Outlined.PeopleOutline,
            navigate = { profileNavigate.toMyEmployees() },
            permission = PermissionEnum.MY_EMPLOYEES_VIEW
        ),
    )

    val visiblePages = remember(permissionsController.values, hasEmployees, isEmployee) {
        pages.filter { page ->
            when (page.permission) {
                PermissionEnum.MY_EMPLOYEES_VIEW -> {
                    if (!hasEmployees) return@filter false
                }
                PermissionEnum.MY_SCHEDULES_VIEW -> {
                    if (!isEmployee) return@filter false
                }
                else -> {  }
            }

            permissionsController.hasPermission(page.permission)
        }
    }

    Layout(
        headerTitle = stringResource(R.string.myBusiness),
        onBack = onBack
    ) {
        if(isLoading) {
            LoadingScreen()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
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
}