package com.example.scrollbooker.feature.profile.presentation.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun EditGenderScreen(
    navController: NavController,
    viewModel: ProfileSharedViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
//        Header(
//            navController = navController,
//            title = "Sex",
//        )
    }
}