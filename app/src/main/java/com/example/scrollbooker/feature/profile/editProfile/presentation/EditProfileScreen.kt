package com.example.scrollbooker.feature.profile.editProfile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.scrollbooker.components.Header
import com.example.scrollbooker.components.list.ItemListInfo
import com.example.scrollbooker.ui.theme.Background

@Composable
fun EditProfileScreen(navController: NavController) {
    Column(Modifier.fillMaxSize().background(Background)) {
        Header(
            navController = navController,
            title = "Edit Profile",
        )
        Text(text = "Despre tine")
        ItemListInfo(
            headLine = "Nume",
            supportingText = "Radu Balgiu",
            onClick = {}
        )
        ItemListInfo(
            headLine = "Username",
            supportingText = "@radu_balgiu",
            onClick = {}
        )
        ItemListInfo(
            headLine = "Bio",
            supportingText = "Sunt medic stomatolog si sunt..",
            onClick = {}
        )
    }
}