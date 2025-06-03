package com.example.scrollbooker.feature.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.ui.theme.Background

@Composable
fun CalendarScreen(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Background)
    ) {
//        Header(
//            navController = navController,
//            title = stringResource(R.string.calendar)
//        )
    }
}