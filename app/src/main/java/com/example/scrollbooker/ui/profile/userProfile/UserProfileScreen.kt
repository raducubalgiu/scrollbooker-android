package com.example.scrollbooker.ui.profile.userProfile
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.profile.components.userInformation.ProfileShimmer

@Composable
fun UserProfileScreen(
    viewModel: ProfileViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    onNavigateToCalendar: (Product) -> Unit
) {
    val userProfileState by viewModel.userProfileState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        when(userProfileState) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> ProfileShimmer()
            is FeatureState.Success-> {
                val user = (userProfileState as FeatureState.Success).data

                Header(
                    title = user.username,
                    onBack = onBack
                )

//                ProfileLayout(
//                    user = user,
//                    onNavigate = onNavigate,
//                    onNavigateToCalendar = onNavigateToCalendar
//                ) {
//                    UserProfileActions(
//                        isFollow = user.isFollow,
//                        onNavigateToCalendar = { onNavigate(MainRoute.Calendar.route) }
//                    )
//                }
            }
        }
    }
}