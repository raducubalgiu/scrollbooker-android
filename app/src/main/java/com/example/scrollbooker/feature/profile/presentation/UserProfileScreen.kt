package com.example.scrollbooker.feature.profile.presentation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.feature.profile.presentation.components.common.ProfileLayout
import com.example.scrollbooker.feature.profile.presentation.components.userProfile.UserProfileActions

@Composable
fun UserProfileScreen(
    viewModel: ProfileViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    val userProfileState by viewModel.userProfileState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        when(userProfileState) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Success-> {
                val user = (userProfileState as FeatureState.Success).data

                Header(
                    title = user.username,
                    onBack = onBack
                )

                ProfileLayout(
                    user = user,
                    onNavigate = onNavigate
                ) {
                    UserProfileActions()
                }
            }
        }
    }
}