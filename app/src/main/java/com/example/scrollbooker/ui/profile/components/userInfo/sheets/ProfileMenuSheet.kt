package com.example.scrollbooker.ui.profile.components.userInfo.sheets
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.list.ItemList
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.enums.has
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState

data class ProfileMenuItem(
    val headLine: String,
    val leftIcon: Painter,
    val onNavigate: () -> Unit,
    val permission: PermissionEnum
)

@Composable
fun ProfileMenuSheet(
    permissionsState:  FeatureState<List<PermissionEnum>>,
    onNavigateToMyBusiness: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToCreatePost: () -> Unit
) {
    val links = listOf(
        ProfileMenuItem(
            headLine = stringResource(R.string.createPost),
            leftIcon = painterResource(R.drawable.ic_camera_outline),
            onNavigate = onNavigateToCreatePost,
            permission = PermissionEnum.POST_CREATE
        ),
        ProfileMenuItem(
            headLine = stringResource(id = R.string.myBusiness),
            leftIcon = painterResource(R.drawable.ic_business_outline),
            onNavigate = onNavigateToMyBusiness,
            permission = PermissionEnum.MY_BUSINESS_ROUTES_VIEW
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BasePadding)
    ) {
        when(permissionsState) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Success<*> -> {
                val permissions = (permissionsState as FeatureState.Success).data
                val visiblePages = links.filter { permissions.has(it.permission) }

                LazyColumn {
                    items(visiblePages) { link ->
                        ItemList(
                            headLine = link.headLine,
                            leftIcon = link.leftIcon,
                            displayRightIcon = false,
                            onClick = link.onNavigate
                        )
                    }

                    item {
                        ItemList(
                            headLine = stringResource(id = R.string.settings),
                            leftIcon = painterResource(R.drawable.ic_settings_outline),
                            displayRightIcon = false,
                            onClick = onNavigateToSettings
                        )
                    }
                }

            }
        }
    }
}