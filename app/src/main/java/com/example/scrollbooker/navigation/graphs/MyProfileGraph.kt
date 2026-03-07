package com.example.scrollbooker.navigation.graphs
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.LocalUserPermissions
import com.example.scrollbooker.ui.profile.MyProfileScreen
import com.example.scrollbooker.ui.profile.MyProfileViewModel

fun NavGraphBuilder.myProfileGraph(
    navController: NavHostController,
    myProfileData: FeatureState<UserProfile>,
    myPosts: LazyPagingItems<Post>
) {
    navigation(
        route = MainRoute.MyProfileNavigator.route,
        startDestination = MainRoute.MyProfile.route
    ) {
        composable(MainRoute.MyProfile.route) { backStackEntry ->
            val viewModel = hiltViewModel<MyProfileViewModel>(backStackEntry)
            val permissionController = LocalUserPermissions.current

            val profileNavigate = remember(navController) {
                ProfileNavigator(navController)
            }

            MyProfileScreen(
                viewModel = viewModel,
                permissionController = permissionController,
                myProfileData = myProfileData,
                myPosts = myPosts,
                profileNavigate = profileNavigate,
            )
        }
    }
}