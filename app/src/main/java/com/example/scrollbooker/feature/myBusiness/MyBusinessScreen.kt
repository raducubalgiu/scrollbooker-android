package com.example.scrollbooker.feature.myBusiness
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.myBusiness.components.MyBusinessCard

@Composable
fun MyBusinessScreen(
    onNavigation: (String) -> Unit,
    onBack: () -> Unit
) {
    val verticalScroll = rememberScrollState()

    Layout(
        headerTitle = stringResource(R.string.myBusiness),
        onBack = onBack
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
        ) {
            Row {
                MyBusinessCard(
                    modifier = Modifier.weight(0.5f),
                    title = stringResource(R.string.location),
                    icon = Icons.Outlined.LocationOn,
                    onClick = {}
                )

                Spacer(Modifier.width(BasePadding))

                MyBusinessCard(
                    modifier = Modifier.weight(0.5f),
                    title = stringResource(R.string.schedule),
                    icon = Icons.Outlined.Schedule,
                    onClick = { onNavigation(MainRoute.Schedules.route) }
                )
            }

            Spacer(Modifier.height(BasePadding))

            Row {
                MyBusinessCard(
                    modifier = Modifier.weight(0.5f),
                    title = stringResource(R.string.products),
                    icon = Icons.Outlined.ShoppingBag,
                    onClick = { onNavigation(MainRoute.Products.route) }
                )

                Spacer(Modifier.width(BasePadding))

                MyBusinessCard(
                    modifier = Modifier.weight(0.5f),
                    title = stringResource(R.string.services),
                    icon = Icons.Outlined.Book,
                    onClick = { onNavigation(MainRoute.MyServices.route) }
                )
            }

            Spacer(Modifier.height(BasePadding))

            Row {
                MyBusinessCard(
                    modifier = Modifier.weight(0.5f),
                    title = stringResource(R.string.employees),
                    icon = Icons.Outlined.PeopleOutline,
                    onClick = { onNavigation(MainRoute.Employees.route) }
                )

                Spacer(Modifier.width(BasePadding))

                MyBusinessCard(
                    modifier = Modifier.weight(0.5f),
                    title = stringResource(R.string.employmentRequests),
                    icon = Icons.Outlined.Repeat,
                    onClick = { onNavigation(MainRoute.EmploymentsRequests.route) }
                )
            }
        }
    }
}