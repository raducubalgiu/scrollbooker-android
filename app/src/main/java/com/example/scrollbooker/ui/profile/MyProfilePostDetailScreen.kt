@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.scrollbooker.ui.profile
import androidx.annotation.OptIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.core.enums.PostViewSourceEnum
import com.example.scrollbooker.navigation.navigators.ProfileNavigator

@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyProfilePostDetailScreen(
    postTabKey: String,
    postIndex: Int,
    viewModel: MyProfileViewModel,
    profileNavigate: ProfileNavigator,
) {
    val postTab = PostTabEnum.fromKey(postTabKey)
    val detailScopeKey = when (postTab) {
        PostTabEnum.POSTS -> "my_profile_${PostViewSourceEnum.POST_DETAIL.key}"
        PostTabEnum.BOOKMARKS -> "my_profile_${PostViewSourceEnum.BOOKMARK_POST_DETAIL.key}"
        null -> PostViewSourceEnum.OTHER.key
    }

    BaseProfilePostDetailScreen(
        detailScopeKey = detailScopeKey,
        viewModel = viewModel,
        postTabKey = postTabKey,
        postIndex = postIndex,
        profileNavigate = profileNavigate
    )
}