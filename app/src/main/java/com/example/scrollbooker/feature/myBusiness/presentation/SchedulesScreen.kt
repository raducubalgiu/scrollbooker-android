package com.example.scrollbooker.feature.myBusiness.presentation
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Header
import com.example.scrollbooker.components.Layout

@Composable
fun SchedulesScreen(navController: NavController) {
    Layout {
        Header(
            navController = navController,
            title = stringResource(R.string.mySchedule),
        )
    }
}