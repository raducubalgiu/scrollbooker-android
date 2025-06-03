package com.example.scrollbooker.feature.profile.presentation.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun EditUsernameScreen(
    navController: NavController,
    viewModel: ProfileSharedViewModel
) {
    Column(Modifier.fillMaxSize().statusBarsPadding()) {
//        Header(
//            navController = navController,
//            title = stringResource(R.string.username),
//        )
    }
}