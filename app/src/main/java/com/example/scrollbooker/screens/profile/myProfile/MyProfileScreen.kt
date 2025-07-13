package com.example.scrollbooker.screens.profile.myProfile
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.components.customized.ProductCardNavigationData
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.products.domain.model.Product
import com.example.scrollbooker.screens.profile.components.ProfileLayout
import com.example.scrollbooker.screens.profile.components.userInformation.ProfileShimmer
import com.example.scrollbooker.screens.profile.components.myProfile.MyProfileActions
import com.example.scrollbooker.screens.profile.components.myProfile.MyProfileHeader
import com.example.scrollbooker.screens.profile.components.myProfile.MyProfileMenuList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    viewModel: ProfileSharedViewModel,
    onNavigate: (String) -> Unit,
    onNavigateToCalendar: (Product) -> Unit
) {
    val userProfileState by viewModel.userProfileState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    if(sheetState.isVisible) {
        Sheet(
            sheetState = sheetState,
            onClose = {
                coroutineScope.launch {
                    sheetState.hide()
                }
            }
        ) {
            Spacer(Modifier.height(BasePadding))

            MyProfileMenuList(
                onNavigate = {
                    coroutineScope.launch {
                        sheetState.hide()

                        if(!sheetState.isVisible) {
                            onNavigate(it)
                        }
                    }
                }
            )

            Spacer(Modifier.height(SpacingXXL))
        }
    }

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        when(userProfileState) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> ProfileShimmer()
            is FeatureState.Success-> {
                val user = (userProfileState as FeatureState.Success).data

                MyProfileHeader(
                    username = user.username,
                    onOpenBottomSheet = {
                        coroutineScope.launch {
                            sheetState.show()
                        }
                    }
                )

                ProfileLayout(
                    user = user,
                    onNavigate = onNavigate,
                    onNavigateToCalendar = onNavigateToCalendar
                ) {
                    MyProfileActions(
                        onEditProfile = { onNavigate(MainRoute.EditProfile.route) },
                    )
                }
            }
        }
    }
}