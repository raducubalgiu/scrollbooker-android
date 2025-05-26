package com.example.scrollbooker.feature.profile.editProfile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Header
import com.example.scrollbooker.ui.theme.Background

@Composable
fun EditProfileScreen(navController: NavController) {
    Column(Modifier.fillMaxSize().background(Background)) {
        Header(
            navController = navController,
            title = "Edit Profile",
        )
        Text(text = "Despre tine")
        ListItem(
            headlineContent = {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Username"
                )
            },
            trailingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        text = "radu_balgiu"
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_next),
                        contentDescription = null
                    )
                }
            }
        )
    }
}