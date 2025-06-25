package com.example.scrollbooker.screens.profile.userProfile
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.screens.profile.components.common.ProfileLayout
import com.example.scrollbooker.screens.profile.components.userProfile.UserProfileActions

@Composable
fun UserProfileScreen(
    viewModel: ProfileViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    val userProfileState by viewModel.userProfileState.collectAsState()
    val userPosts = viewModel.userPosts.collectAsLazyPagingItems()

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
                    onNavigate = onNavigate,
                    userPosts = userPosts,
                    userBookmarkedPosts = null,
                    userReposts = null
                ) {
                    UserProfileActions(
                        isFollow = user.isFollow,
                        onNavigateToCalendar = { onNavigate(MainRoute.Calendar.route) }
                    )
                }
            }
        }
    }
}