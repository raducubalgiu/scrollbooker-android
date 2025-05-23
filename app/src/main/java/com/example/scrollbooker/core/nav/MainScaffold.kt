package com.example.scrollbooker.core.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.scrollbooker.core.nav.routes.MainRoute

@Composable
fun MainScaffold(
    navController: NavHostController,
    startDestination: String
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val containerColor = if(currentRoute == MainRoute.Feed.route) Color(0xFF121212) else MaterialTheme.colorScheme.background

    Scaffold(
        containerColor = containerColor,
        bottomBar = {
            Column {
                HorizontalDivider(color = Color.DarkGray)
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavHost(
                navController = navController,
                startDestination = startDestination
            )
        }
    }
}