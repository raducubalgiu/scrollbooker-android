package com.example.scrollbooker.ui.profile
import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.UserPermissionsController
import com.example.scrollbooker.ui.profile.components.MyProfileHeader
import com.example.scrollbooker.ui.profile.components.ProfileLayout
import com.example.scrollbooker.ui.profile.components.userInfo.components.MyProfileActions
import com.example.scrollbooker.ui.profile.sheets.ProfileMenuSheet
import com.example.scrollbooker.ui.profile.sheets.UserScheduleSheet
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    viewModel: MyProfileViewModel,
    permissionController: UserPermissionsController,
    profileNavigate: ProfileNavigator,
) {
    val profileData by viewModel.profile.collectAsStateWithLifecycle()
    val userData = (profileData as? FeatureState.Success)?.data

    val scope = rememberCoroutineScope()
    val menuSheetState = rememberModalBottomSheetState()
    val scheduleSheetState = rememberModalBottomSheetState()

    if(scheduleSheetState.isVisible) {
        UserScheduleSheet(
            sheetState = scheduleSheetState,
            schedulesFlow = viewModel.schedules
        )
    }

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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            ProfileLayout(
                profile = profileData,
                profileNavigate = profileNavigate,
                postsState = viewModel.posts,
                productsState = viewModel.products,
                employeesState = viewModel.employees,
                bookmarksState = viewModel.bookmarks,
                aboutState = viewModel.about,
                onNavigateToPost = { profileNavigate.toMyPostDetail(PostTabEnum.POSTS, it) }
            ) {
                MyProfileActions(
                    isBusinessOrEmployee = userData?.isBusinessOrEmployee == true,
                    onEditProfile = { profileNavigate.toEditProfile() },
                    onNavigateToMyCalendar = { profileNavigate.toMyCalendar() },
                )
            }
        }
    }
}