package com.example.scrollbooker.ui.profile
import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.customized.Protected.Protected
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.util.Dimens.IconSizeXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.UserPermissionsController
import com.example.scrollbooker.ui.profile.components.ProfileLayout
import com.example.scrollbooker.ui.profile.components.ProfileLayoutViewModel
import com.example.scrollbooker.ui.profile.components.sheets.ProfileMenuSheet
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    viewModel: MyProfileViewModel,
    permissionController: UserPermissionsController,
    myProfileData: FeatureState<UserProfile>,
    myPosts: LazyPagingItems<Post>,
    profileNavigate: ProfileNavigator,
) {
    val layoutViewModel: ProfileLayoutViewModel = hiltViewModel()
    val userData = (myProfileData as? FeatureState.Success)?.data

    LaunchedEffect(Unit) {
        layoutViewModel.setUserId(userData?.id)
    }

    val scope = rememberCoroutineScope()
    val menuSheetState = rememberModalBottomSheetState()

    if(menuSheetState.isVisible) {
        ProfileMenuSheet(
            sheetState = menuSheetState,
            profileNavigate = profileNavigate,
            permissionController = permissionController
        )
    }

    Scaffold(
        topBar = {
            MyProfileHeader(
                username = userData?.username ?: "",
                onNavigateToCamera = { profileNavigate.toCamera() },
                onOpenMenu = { scope.launch { menuSheetState.show() } }
            )
        },
        bottomBar = { BottomBar() },
        containerColor = Background
    ) { innerPadding ->
        ProfileLayout(
            layoutViewModel = layoutViewModel,
            innerPadding = innerPadding,
            profile = myProfileData,
            profileNavigate = profileNavigate,
            onNavigateToPost = { postUi, post -> navigateToPost(viewModel, profileNavigate, postUi, post) },
            posts = myPosts
        )
    }
}

@Composable
private fun MyProfileHeader(
    username: String,
    onNavigateToCamera: () -> Unit,
    onOpenMenu: () -> Unit
) {
    Header(
        title = "@$username",
        actions = {
            Row {
                Protected(permission = PermissionEnum.POST_CREATE) {
                    CustomIconButton(
                        painter = R.drawable.ic_circle_plus_outline,
                        onClick = onNavigateToCamera,
                        iconSize = IconSizeXL
                    )
                }

                CustomIconButton(
                    imageVector = Icons.Default.Menu,
                    onClick = onOpenMenu,
                    iconSize = IconSizeXL
                )
            }
        }
    )
}

private fun navigateToPost(
    viewModel: MyProfileViewModel,
    profileNavigate: ProfileNavigator,
    postUi: SelectedPostUi,
    post: Post
) {
    viewModel.onPageSettled(postUi.index)

    viewModel.ensureImmediate(
        centerIndex = postUi.index,
        getPost = { i -> if(i == postUi.index) post else null }
    )

    viewModel.setSelectedPost(postUi)
    profileNavigate.toPostDetail()
}