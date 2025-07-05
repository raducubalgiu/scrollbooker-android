package com.example.scrollbooker.screens.profile.components.myProfile

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.list.ItemList
import com.example.scrollbooker.core.nav.routes.MainRoute

@Composable
fun MyProfileMenuList(
    onNavigate: (String) -> Unit
) {
    ItemList(
        headLine = "Creaza o postare",
        leftIcon = painterResource(R.drawable.ic_camera_outline),
        displayRightIcon = false,
        onClick = {  }
    )
    ItemList(
        headLine = stringResource(id = R.string.calendar),
        leftIcon = painterResource(R.drawable.ic_calendar_outline),
        displayRightIcon = false,
        onClick = { onNavigate(MainRoute.MyCalendar.route) }
    )
    ItemList(
        headLine = stringResource(id = R.string.myBusiness),
        leftIcon = painterResource(R.drawable.ic_business_outline),
        displayRightIcon = false,
        onClick = { onNavigate(MainRoute.MyBusiness.route) }
    )
    ItemList(
        headLine = stringResource(id = R.string.settings),
        leftIcon = painterResource(R.drawable.ic_settings_outline),
        displayRightIcon = false,
        onClick = { onNavigate(MainRoute.Settings.route) }
    )
}