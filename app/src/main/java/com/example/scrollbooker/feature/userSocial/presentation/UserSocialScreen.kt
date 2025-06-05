package com.example.scrollbooker.feature.userSocial.presentation

import androidx.compose.runtime.Composable
import com.example.scrollbooker.components.core.Layout

@Composable
fun UserSocialScreen(
    viewModal: UserSocialViewModel,
    onBack: () -> Unit,
    userId: Int,
    username: String
) {
    Layout(
        headerTitle = username,
        onBack = onBack
    ) {

    }
}