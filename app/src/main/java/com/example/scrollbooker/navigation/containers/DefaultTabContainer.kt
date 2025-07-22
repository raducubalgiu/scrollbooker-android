package com.example.scrollbooker.navigation.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DefaultTabContainer(
    navController: NavHostController,
    innerPadding: PaddingValues,
    enablePadding: Boolean = true,
    content: @Composable (NavHostController) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = if(enablePadding) innerPadding.calculateTopPadding() else 0.dp)
    ) {
        content(navController)
    }
}