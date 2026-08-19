package com.example.scrollbooker.ui.myBusiness.myDashboard.tabs
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.myBusiness.myDashboard.DashboardPeriod
import com.example.scrollbooker.ui.myBusiness.myDashboard.components.PeriodSelector

@Composable
fun MyDashboardPostsTab() {
    var selectedPeriod by remember { mutableStateOf(DashboardPeriod.SEVEN_DAYS) }

    Column(modifier = Modifier.fillMaxSize()) {
        PeriodSelector(
            selectedPeriod = selectedPeriod,
            onPeriodSelected = { selectedPeriod = it }
        )
    }
}