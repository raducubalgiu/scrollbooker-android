package com.example.scrollbooker.feature.profile.presentation.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Header
import com.example.scrollbooker.components.inputs.EditInput
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun EditBioScreen(
    navController: NavController,
    viewModel: ProfileSharedViewModel
) {
    var bio by rememberSaveable { mutableStateOf(viewModel.user?.bio ?: "") }

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {

        Header(
            navController = navController,
            title = stringResource(R.string.biography),
        )

        Column(Modifier.padding(BasePadding)) {
            EditInput(
                value = bio,
                singleLine = false,
                minLines = 5,
                maxLines = 5,
                onValueChange = { bio = it },
                placeholder = stringResource(R.string.yourBio)
            )
        }
    }
}