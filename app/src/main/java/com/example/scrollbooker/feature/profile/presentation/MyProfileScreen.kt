package com.example.scrollbooker.feature.profile.presentation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.BottomSheet
import com.example.scrollbooker.components.list.ItemList
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.profile.presentation.components.ProfileHeader
import com.example.scrollbooker.feature.profile.presentation.components.ProfileLayout
import com.example.scrollbooker.feature.profile.presentation.components.myProfile.MyProfileActions

@Composable
fun MyProfileScreen(
    viewModel: ProfileSharedViewModel,
    onNavigate: (String) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val user = viewModel.user
    val userId = user?.id
    val username = viewModel.user?.username ?: ""

    BottomSheet(
        onDismiss = { showBottomSheet = false },
        showBottomSheet = showBottomSheet,
        showHeader = false,
    ) {
        ItemList(
            headLine = stringResource(id = R.string.calendar),
            leftIcon = painterResource(R.drawable.ic_calendar),
            displayRightIcon = false,
            onClick = {
                showBottomSheet = false
                onNavigate(MainRoute.Calendar.route)
            }
        )
        ItemList(
            headLine = stringResource(id = R.string.myBusiness),
            leftIcon = painterResource(R.drawable.ic_business),
            displayRightIcon = false,
            onClick = {
                showBottomSheet = false
                onNavigate(MainRoute.MyBusiness.route)
            }
        )
        ItemList(
            headLine = stringResource(id = R.string.settings),
            leftIcon = painterResource(R.drawable.ic_settings),
            displayRightIcon = false,
            onClick = {
                showBottomSheet = false
                onNavigate(MainRoute.Settings.route)
            }
        )
    }

    ProfileLayout(
        ratingsCount = user?.counters?.ratingsCount ?: 0,
        followersCount = user?.counters?.followersCount ?: 0,
        followingsCount = user?.counters?.followingsCount ?: 0,
        fullName = user?.fullName ?: "",
        profession = user?.profession ?: "",
        onNavigateCounters = { onNavigate("$it/$userId/$username") },
        header = { 
            ProfileHeader(
                username = username,
                onOpenBottomSheet = { showBottomSheet = true }
            ) 
        }
    ) {
        MyProfileActions(
            onNavigate={ onNavigate(MainRoute.EditProfile.route) }
        )
    }
}