package com.example.scrollbooker.ui.profile
import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.profile.components.ProfileLayout
import com.example.scrollbooker.ui.profile.components.ProfileLayoutViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    profileNavigate: ProfileNavigator
) {
    val layoutViewModel: ProfileLayoutViewModel = hiltViewModel()

    val profile by viewModel.userProfileState.collectAsStateWithLifecycle()
    val posts = viewModel.userPosts.collectAsLazyPagingItems()
    val userData = (profile as? FeatureState.Success)?.data

    LaunchedEffect(Unit) {
        layoutViewModel.setUserId(userData?.id)
    }

    Scaffold(
        topBar = {
            Header(
                title = userData?.username ?: "",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        ProfileLayout(
            layoutViewModel = layoutViewModel,
            innerPadding = innerPadding,
            profile = profile,
            profileNavigate = profileNavigate,
            onNavigateToPost = { postUi, post -> },
            posts = posts
        )
    }
}