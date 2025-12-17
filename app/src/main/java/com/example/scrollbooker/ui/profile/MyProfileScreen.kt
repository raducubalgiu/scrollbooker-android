package com.example.scrollbooker.ui.profile
import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import com.example.scrollbooker.ui.profile.components.sheets.ProfileMenuSheet
import com.example.scrollbooker.ui.profile.tabs.ProfileTabViewModel
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
    profileNavigate: ProfileNavigator
) {
    val profileTabViewModel: ProfileTabViewModel = hiltViewModel()
    val menuSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val isInitLoading by viewModel.isInitLoading.collectAsState()

    if(menuSheetState.isVisible) {
        ProfileMenuSheet(
            sheetState = menuSheetState,
            profileNavigate = profileNavigate,
            permissionController = permissionController
        )
    }

    val userData = (myProfileData as? FeatureState.Success)?.data

    Scaffold(
        topBar = {
            Header(
                title = "@${userData?.username ?: ""}",
                actions = {
                    Row {
                        Protected(permission = PermissionEnum.POST_CREATE) {
                            CustomIconButton(
                                painter = R.drawable.ic_circle_plus_outline,
                                onClick = { profileNavigate.toCamera() },
                                iconSize = IconSizeXL
                            )
                        }

                        CustomIconButton(
                            imageVector = Icons.Default.Menu,
                            onClick = { scope.launch { menuSheetState.show() } },
                            iconSize = IconSizeXL
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar() },
        containerColor = Background
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            ProfileLayout(
                profileTabViewModel = profileTabViewModel,
                isInitLoading = isInitLoading,
                profileData = myProfileData,
                isFollowEnabled = false,
                posts = myPosts,
                profileNavigate = profileNavigate
            )
        }
    }
}