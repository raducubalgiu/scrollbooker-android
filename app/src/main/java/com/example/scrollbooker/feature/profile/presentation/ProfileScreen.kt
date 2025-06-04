package com.example.scrollbooker.feature.profile.presentation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.BottomSheet
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.components.list.ItemList
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.profile.presentation.components.ProfileHeader
import com.example.scrollbooker.feature.profile.presentation.components.ProfileTabs
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun ProfileScreen(
    viewModel: ProfileSharedViewModel,
    navController: NavHostController
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    BottomSheet(
        onDismiss = { showBottomSheet = false },
        showBottomSheet = showBottomSheet,
        showHeader = false,
        content = {
            ItemList(
                headLine = stringResource(id = R.string.calendar),
                leftIcon = painterResource(R.drawable.ic_calendar),
                displayRightIcon = false,
                onClick = {
                    showBottomSheet = false
                    //navController.navigate("calendar")
                }
            )
            ItemList(
                headLine = stringResource(id = R.string.myBusiness),
                leftIcon = painterResource(R.drawable.ic_business),
                displayRightIcon = false,
                onClick = {
                    showBottomSheet = false
                    //navController.navigate("myBusiness")
                }
            )
            ItemList(
                headLine = stringResource(id = R.string.settings),
                leftIcon = painterResource(R.drawable.ic_settings),
                displayRightIcon = false,
                onClick = {
                    showBottomSheet = false
                    //navController.navigate("settings")
                }
            )
        }
    )

    Layout {
        ProfileHeader(onOpenBottomSheet = { showBottomSheet = true })
        Column(modifier = Modifier
            .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = viewModel.user?.username ?: "", color = OnBackground)
        }
        Button(onClick = { navController.navigate(MainRoute.EditProfile.route) }) {
            Text("Edit Profile")
        }
        Spacer(Modifier.height(BasePadding))
        Button(onClick = { viewModel.logout() }) {
            Text("Logout")
        }
        ProfileTabs()
    }
}