package com.example.scrollbooker.feature.profile.editProfile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.scrollbooker.components.Header

@Composable
fun EditGenderScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(
            navController = navController,
            title = "Sex",
        )
    }
}