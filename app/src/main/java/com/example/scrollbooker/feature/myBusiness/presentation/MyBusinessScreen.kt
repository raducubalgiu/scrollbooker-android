package com.example.scrollbooker.feature.myBusiness.presentation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun MyBusinessScreen(
    onNavigation: (String) -> Unit,
    onBack: () -> Unit
) {
    Layout(
        headerTitle = stringResource(R.string.myBusiness),
        onBack = onBack
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { onNavigation(MainRoute.Services.route) }) {
                Text(text = "Go To Services")
            }
            Spacer(Modifier.height(BasePadding))
            Button(onClick = { onNavigation(MainRoute.Products.route) }) {
                Text(text = "Go To Products")
            }
            Spacer(Modifier.height(BasePadding))
            Button(onClick = { MainRoute.Schedules.route }) {
                Text(text = "Go To Schedules")
            }
        }
    }
}