package com.example.scrollbooker.ui.myBusiness.myDashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.navigation.navigators.ProfileNavigator

@Composable
fun MyDashboardScreen(
    viewModel: MyDashboardViewModel,
    profileNavigate: ProfileNavigator
) {
    Scaffold(
        topBar = {
            Header(
                title = stringResource(R.string.dashboard),
                onBack = { profileNavigate.back() }
            )
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {

        }
    }
}