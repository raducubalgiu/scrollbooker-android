package com.example.scrollbooker.ui.profile
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.NavigateBookingParam
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.profile.components.ProfileLayout
import com.example.scrollbooker.ui.profile.components.userInfo.components.MyProfileActions
import com.example.scrollbooker.ui.profile.components.userInfo.components.UserProfileActions
import com.example.scrollbooker.ui.profile.sheets.UserScheduleSheet
import com.example.scrollbooker.ui.theme.Background

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    profileNavigate: ProfileNavigator
) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val userData = (profile as? FeatureState.Success)?.data
    val isBusinessOrEmployee = userData?.isBusinessOrEmployee == true

    val isFollow by viewModel.isFollowState.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    val scheduleSheetState = rememberModalBottomSheetState()
    if(scheduleSheetState.isVisible) {
        UserScheduleSheet(
            sheetState = scheduleSheetState,
            schedulesFlow = viewModel.schedules,
        )
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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = innerPadding.calculateTopPadding())
        ) {
            ProfileLayout(
                profile = profile,
                profileNavigate = profileNavigate,
                postsState = viewModel.posts,
                productsState = viewModel.products,
                employeesState = viewModel.employees,
                bookmarksState = viewModel.bookmarks,
                aboutState = viewModel.about,
                onNavigateToPost = {
                    val userId = userData?.id ?: return@ProfileLayout
                    profileNavigate.toUserPostDetail(PostTabEnum.POSTS, it, userId)
                }
            ) {
                if(userData?.isOwnProfile == true) {
                    MyProfileActions(
                        isBusinessOrEmployee = isBusinessOrEmployee,
                        onEditProfile = { profileNavigate.toEditProfile() },
                        onNavigateToMyCalendar = { profileNavigate.toMyCalendar() },
                    )
                } else {
                    UserProfileActions(
                        isBusinessOrEmployee = isBusinessOrEmployee,
                        isFollow = isFollow,
                        isFollowEnabled = !isSaving,
                        onFollow = { viewModel.follow() },
                        onNavigateToBooking = {
                            val userId = userData?.id ?: return@UserProfileActions
                            val businessId = userData.businessId ?: return@UserProfileActions
                            val businessOwnerId = userData.businessOwner?.id ?: return@UserProfileActions

                            profileNavigate.toBookingFromProfile(
                                NavigateBookingParam(
                                    userId = userId,
                                    businessId = businessId,
                                    businessOwnerId = businessOwnerId,
                                    selectedProductId = null,
                                    source = BookingSourceEnum.PROFILE
                                )
                            )
                        },
                    )
                }
            }
        }
    }
}