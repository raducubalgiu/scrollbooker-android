package com.example.scrollbooker.feature.profile.presentation.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Header
import com.example.scrollbooker.components.list.ItemListInfo
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.ui.theme.Background

@Composable
fun EditProfileScreen(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Background)
    ) {
        Header(
            navController = navController,
            title = stringResource(R.string.editProfile),
        )
        Text(text = "Despre tine")

        ItemListInfo(
            headLine = stringResource(R.string.name),
            supportingText = "Radu Balgiu",
            onClick = { navController.navigate(MainRoute.EditFullName.route) }
        )
        ItemListInfo(
            headLine = "Username",
            supportingText = "@radu_balgiu",
            onClick = { navController.navigate(MainRoute.EditUsername.route) }
        )
        ItemListInfo(
            headLine = "Bio",
            supportingText = "Sunt medic stomatolog si sunt..",
            onClick = { navController.navigate(MainRoute.EditBio.route) }
        )
        ItemListInfo(
            headLine = "Gender",
            supportingText = "Male",
            onClick = { navController.navigate(MainRoute.EditGender.route) }
        )
    }
}