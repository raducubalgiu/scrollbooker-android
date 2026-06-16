@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.scrollbooker.ui.profile
import androidx.annotation.OptIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.navigation.navigators.ProfileNavigator

@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyProfilePostDetailScreen(
    postTabKey: String,
    postIndex: Int,
    viewModel: MyProfileViewModel,
    onBack: () -> Unit,
    profileNavigate: ProfileNavigator,
) {
    val detailScopeKey = "MY_PROFILE_DETAIL_${postTabKey}"

    BaseProfilePostDetailScreen(
        detailScopeKey = detailScopeKey,
        viewModel = viewModel,
        postTabKey = postTabKey,
        postIndex = postIndex,
        onBack = onBack,
        profileNavigate = profileNavigate
    )
}