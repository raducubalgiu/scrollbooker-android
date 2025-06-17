package com.example.scrollbooker.feature.profile.presentation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.sheet.BottomSheet
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.feature.profile.presentation.components.common.ProfileLayout
import com.example.scrollbooker.feature.profile.presentation.components.myProfile.MyProfileActions
import com.example.scrollbooker.feature.profile.presentation.components.myProfile.MyProfileHeader
import com.example.scrollbooker.feature.profile.presentation.components.myProfile.MyProfileMenuList

@Composable
fun MyProfileScreen(
    viewModel: ProfileSharedViewModel,
    onNavigate: (String) -> Unit
) {
    var showMenuSheet by remember { mutableStateOf(false) }
    val userProfileState by viewModel.userProfileState.collectAsState()
    val userPosts = viewModel.userPosts.collectAsLazyPagingItems()

    BottomSheet(
        onDismiss = { showMenuSheet = false },
        showBottomSheet = showMenuSheet,
        showHeader = false,
    ) {
        MyProfileMenuList(
            onNavigate = {
                onNavigate(it)
                showMenuSheet = false
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when(userProfileState) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Success-> {
                val user = (userProfileState as FeatureState.Success).data

                MyProfileHeader(
                    username = user.username,
                    onOpenBottomSheet = { showMenuSheet = true }
                )

                ProfileLayout(
                    user = user,
                    onNavigate = onNavigate,
                    userPosts = userPosts
                ) {
                    MyProfileActions(
                        onEditProfile = { onNavigate(MainRoute.EditProfile.route) }
                    )
                }
            }
        }
    }
}