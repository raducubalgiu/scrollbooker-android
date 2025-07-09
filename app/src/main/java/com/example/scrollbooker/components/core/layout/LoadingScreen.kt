package com.example.scrollbooker.components.core.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    arrangement: Arrangement.Vertical = Arrangement.Center
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = arrangement
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(30.dp)
                .then(modifier)
        )
    }
}