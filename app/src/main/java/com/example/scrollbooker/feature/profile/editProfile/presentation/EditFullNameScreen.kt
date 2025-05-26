package com.example.scrollbooker.feature.profile.editProfile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.scrollbooker.components.Header
import com.example.scrollbooker.components.inputs.EditInput
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun EditFullNameScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        Header(
            navController = navController,
            title = "Nume"
        )

        Column(Modifier.padding(BasePadding)) {
            EditInput(
                value = name,
                onValueChange = { name = it },
            )
        }
    }
}