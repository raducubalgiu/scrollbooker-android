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
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun MyBusinessScreen(navController: NavController) {
    Layout {
//        Header(
//            navController = navController,
//            title = stringResource(R.string.myBusiness),
//        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { navController.navigate("Services") }) {
                Text(text = "Go To Services")
            }
            Spacer(Modifier.height(BasePadding))
            Button(onClick = { navController.navigate("Products") }) {
                Text(text = "Go To Products")
            }
            Spacer(Modifier.height(BasePadding))
            Button(onClick = { navController.navigate("Schedules") }) {
                Text(text = "Go To Schedules")
            }
        }
    }
}