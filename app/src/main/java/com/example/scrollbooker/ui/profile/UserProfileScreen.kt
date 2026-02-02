package com.example.scrollbooker.ui.profile
import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.profile.components.ProfileLayout
import com.example.scrollbooker.ui.profile.components.ProfileLayoutViewModel
import com.example.scrollbooker.ui.profile.components.SelectedPostUi
import com.example.scrollbooker.ui.theme.Background

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    layoutViewModel: ProfileLayoutViewModel,
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    onNavigateToSocial: (NavigateSocialParam) -> Unit,
    profileNavigate: ProfileNavigator
) {
    val profile by viewModel.userProfileState.collectAsStateWithLifecycle()
    val posts = layoutViewModel.posts.collectAsLazyPagingItems()

    val isFollow by viewModel.isFollowState.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    val userData = (profile as? FeatureState.Success)?.data

    LaunchedEffect(userData) {
        layoutViewModel.setUserId(userData?.id)
    }

    Scaffold(
        topBar = {
            Header(
                title = userData?.username ?: "",
                onBack = onBack
            )
        },
        containerColor = Background
    ) { innerPadding ->
        ProfileLayout(
            layoutViewModel = layoutViewModel,
            innerPadding = innerPadding,
            profile = profile,
            profileNavigate = profileNavigate,
            onNavigateToSocial = onNavigateToSocial,
            onNavigateToPost = {
                navigateToPost(layoutViewModel, profileNavigate, it)
            },
            posts = posts,
            isFollow = isFollow == true,
            isFollowEnabled = !isSaving,
            onFollow = { viewModel.follow() },
        )
    }
}

private fun navigateToPost(
    viewModel: ProfileLayoutViewModel,
    profileNavigate: ProfileNavigator,
    postUi: SelectedPostUi
) {
    viewModel.onPageSettled(postUi.index)

    viewModel.ensureImmediate(
        centerIndex = postUi.index,
        getPost = { i -> if(i == postUi.index) postUi.post else null }
    )

    viewModel.setSelectedPost(postUi)
    profileNavigate.toPostDetail()
}