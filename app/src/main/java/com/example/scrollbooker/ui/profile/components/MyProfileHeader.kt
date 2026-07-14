package com.example.scrollbooker.ui.profile.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.customized.protected.Protected
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.util.Dimens.IconSizeXL

@Composable
fun MyProfileHeader(
    username: String,
    onNavigateToCamera: () -> Unit,
    onOpenMenu: () -> Unit
) {
    Header(
        title = "@$username",
        actions = {
            Row {
                Protected(permission = PermissionEnum.POST_CREATE) {
                    CustomIconButton(
                        painter = R.drawable.ic_circle_plus_outline,
                        onClick = onNavigateToCamera,
                        iconSize = IconSizeXL
                    )
                }

                CustomIconButton(
                    imageVector = Icons.Default.Menu,
                    onClick = onOpenMenu,
                    iconSize = IconSizeXL
                )
            }
        }
    )
}