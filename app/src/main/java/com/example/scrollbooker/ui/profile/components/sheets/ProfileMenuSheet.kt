package com.example.scrollbooker.ui.profile.components.sheets
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.list.ItemList
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.enums.has
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.UserPermissionsController
import kotlinx.coroutines.launch

data class ProfileMenuItem(
    val headLine: String,
    val leftIcon: Painter,
    val onNavigate: () -> Unit,
    val permission: PermissionEnum
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenuSheet(
    sheetState: SheetState,
    profileNavigate: ProfileNavigator,
    permissionController: UserPermissionsController
) {
    val scope = rememberCoroutineScope()

    val links = listOf(
        ProfileMenuItem(
            headLine = stringResource(R.string.createPost),
            leftIcon = painterResource(R.drawable.ic_camera_outline),
            onNavigate = {
                scope.launch {
                    sheetState.hide()

                    if (!sheetState.isVisible) {
                        profileNavigate.toCamera()
                    }
                }
            },
            permission = PermissionEnum.POST_CREATE
        ),
        ProfileMenuItem(
            headLine = stringResource(id = R.string.myBusiness),
            leftIcon = painterResource(R.drawable.ic_business_outline),
            onNavigate = {
                scope.launch {
                    sheetState.hide()

                    if (!sheetState.isVisible) {
                        profileNavigate.toMyBusiness()
                    }
                }
            },
            permission = PermissionEnum.MY_BUSINESS_ROUTES_VIEW
        )
    )

    Sheet(
        sheetState = sheetState,
        onClose = { scope.launch { sheetState.hide() } }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = BasePadding)
        ) {
            val visiblePages = links.filter { permissionController.has(it.permission) }

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
                        onClick = {
                            scope.launch {
                                sheetState.hide()

                                if (!sheetState.isVisible) {
                                    profileNavigate.toSettings()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}