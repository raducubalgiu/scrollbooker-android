package com.example.scrollbooker.feature.profile.presentation.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.scrollbooker.components.Header

@Composable
fun EditBioScreen(navController: NavController) {
    Column(Modifier.fillMaxSize()) {
        Header(
            navController = navController,
            title = "Bio",
        )
    }
}