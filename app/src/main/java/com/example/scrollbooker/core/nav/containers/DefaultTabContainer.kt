package com.example.scrollbooker.core.nav.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun DefaultTabContainer(
    navController: NavHostController,
    innerPadding: PaddingValues,
    content: @Composable (NavHostController) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = innerPadding.calculateTopPadding())
    ) {
        content(navController)
    }
}