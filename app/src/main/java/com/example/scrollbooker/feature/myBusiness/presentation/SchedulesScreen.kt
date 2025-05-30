package com.example.scrollbooker.feature.myBusiness.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Header

@Composable
fun SchedulesScreen(navController: NavController) {
    Column(Modifier.fillMaxSize()) {
        Header(
            navController = navController,
            title = stringResource(R.string.mySchedule),
        )
    }
}