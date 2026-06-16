package com.example.scrollbooker.ui.profile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.scrollbooker.navigation.navigators.ProfileNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilePostDetailScreen(
    postTabKey: String,
    postIndex: Int,
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    profileNavigate: ProfileNavigator,
) {
    val detailScopeKey = "USER_PROFILE_DETAIL_${postTabKey}_${viewModel.userId}"

    BaseProfilePostDetailScreen(
        detailScopeKey = detailScopeKey,
        viewModel = viewModel,
        postTabKey = postTabKey,
        postIndex = postIndex,
        onBack = onBack,
        profileNavigate = profileNavigate
    )
}