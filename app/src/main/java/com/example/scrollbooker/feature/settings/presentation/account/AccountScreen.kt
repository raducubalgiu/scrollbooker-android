package com.example.scrollbooker.feature.settings.presentation.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Header

@Composable
fun AccountScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(
            navController = navController,
            title = stringResource(id = R.string.account),
        )
    }
}