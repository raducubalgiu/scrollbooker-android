package com.example.scrollbooker.feature.myBusiness.myCalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
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