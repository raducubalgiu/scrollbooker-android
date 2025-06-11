package com.example.scrollbooker.feature.profile.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.feature.profile.presentation.components.common.ProfileLayout
import com.example.scrollbooker.feature.profile.presentation.components.userProfile.UserProfileActions
import com.example.scrollbooker.ui.theme.Background

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun UserProfileScreen(
    viewModel: ProfileSharedViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    val user = viewModel.user

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Background)
    ) {
        Header(
            title = user?.username ?: "",
            onBack = onBack
        )

        ProfileLayout(user, onNavigate) {
            UserProfileActions()
        }
    }
}