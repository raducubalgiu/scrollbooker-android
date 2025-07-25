package com.example.scrollbooker.navigation.host
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.navigators.myBusinessGraph
import com.example.scrollbooker.navigation.navigators.settingsGraph
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.profile.calendar.AppointmentConfirmationScreen
import com.example.scrollbooker.ui.profile.calendar.CalendarScreen
import com.example.scrollbooker.ui.profile.tab.posts.ProfilePostsTabViewModel
import com.example.scrollbooker.ui.profile.myProfile.MyProfileScreen
import com.example.scrollbooker.ui.profile.myProfile.ProfileSharedViewModel
import com.example.scrollbooker.ui.profile.myProfile.edit.EditBioScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditFullNameScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditGenderScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditProfessionScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditProfileScreen
import com.example.scrollbooker.ui.profile.myProfile.edit.EditUsernameScreen
import com.example.scrollbooker.ui.profile.postDetail.ProfilePostDetailScreen
import com.example.scrollbooker.ui.profile.social.UserSocialScreen
import com.example.scrollbooker.ui.profile.social.UserSocialViewModel
import com.example.scrollbooker.ui.profile.userProfile.ProfileViewModel
import com.example.scrollbooker.ui.profile.userProfile.UserProfileScreen
import com.example.scrollbooker.ui.sharedModules.calendar.CalendarViewModel

@Composable
fun ProfileNavHost(
    myProfileData: FeatureState<UserProfile>,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.MyProfile.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            )
        }
    ) {

    }
}