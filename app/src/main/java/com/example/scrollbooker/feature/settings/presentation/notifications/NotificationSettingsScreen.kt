package com.example.scrollbooker.feature.settings.presentation.notifications
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.components.list.ItemSwitch

@Composable
fun NotificationSettings(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(
            title = stringResource(id = R.string.notifications),
            navController = navController
        )

        ItemSwitch(
            headLine = "Bookings",
            supportingText = "Alege sa primesti notificari despre programarile tale viitoare si cele vechi",
            onClick = {},
        )

        ItemSwitch(
            headLine = "Likes",
            onClick = {},
        )

        ItemSwitch(
            headLine = "Comments",
            onClick = {},
        )

        ItemSwitch(
            headLine = "New Followers",
            onClick = {},
        )

        ItemSwitch(
            headLine = "Mentions and Tags",
            onClick = {},
        )
    }
}