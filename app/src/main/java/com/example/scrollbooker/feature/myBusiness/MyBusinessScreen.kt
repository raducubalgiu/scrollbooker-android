package com.example.scrollbooker.feature.myBusiness

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.SurfaceBG

data class MyBusinessCard(
    val route: String
)

@Composable
fun MyBusinessScreen(navController: NavController) {
    val verticalScroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Header(
            navController = navController,
            title = stringResource(R.string.myBusiness),
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
        ) {
            Row {
                Column(modifier = Modifier
                    .background(SurfaceBG)
                    .padding(BasePadding)
                ) {
                    Text("My Business")
                }

                Spacer(Modifier.width(BasePadding))

                Column(Modifier
                    .background(SurfaceBG)
                    .padding(BasePadding)
                ) {
                    Text("My Business")
                }
            }
        }

//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Button(onClick = { navController.navigate("Services") }) {
//                Text(text = "Go To Services")
//            }
//            Spacer(Modifier.height(BasePadding))
//            Button(onClick = { navController.navigate("Products") }) {
//                Text(text = "Go To Products")
//            }
//            Spacer(Modifier.height(BasePadding))
//            Button(onClick = { navController.navigate("Schedules") }) {
//                Text(text = "Go To Schedules")
//            }
//        }
    }
}