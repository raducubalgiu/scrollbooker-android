package com.example.scrollbooker.screens.auth.presentation.components.collectClientDetails

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R

@Composable
fun CollectBirthDateScreen(navController: NavController) {
    CollectClientDetails(
        headLine = stringResource(id = R.string.dateOfBirth),
        subHeadLine = "Some description for date of birth",
        screenSize = 3,
        selectedScreen = 1,
        onOmit = {},
        onNext = {},
    ) {
        Text(text = "BirthDate Screen")
    }
}