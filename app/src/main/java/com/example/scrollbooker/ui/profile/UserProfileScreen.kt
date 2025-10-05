package com.example.scrollbooker.ui.profile
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.profile.components.ProfileLayout
import com.example.scrollbooker.ui.theme.Background

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    profileNavigate: ProfileNavigator
) {
    val profileData by viewModel.userProfileState.collectAsState()
    val posts = viewModel.userPosts.collectAsLazyPagingItems()
    val isInitLoading by viewModel.isInitLoading.collectAsState()
    val isFollow by viewModel.isFollowState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val userData = (profileData as? FeatureState.Success)?.data

    Scaffold(
        topBar = {
            Header(
                title = userData?.username ?: "",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            ProfileLayout(
                isInitLoading = isInitLoading,
                profileData = profileData,
                isFollow = isFollow,
                onFollow = { viewModel.onFollow() },
                isFollowEnabled = !isSaving,
                posts = posts,
                profileNavigate = profileNavigate
            )
        }
    }
}