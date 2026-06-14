package com.example.scrollbooker.components.customized.protected

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.ui.LocalUserPermissions

@Composable
fun Protected(
    permission: PermissionEnum,
    content: @Composable () -> Unit
) {
    val permissionController = LocalUserPermissions.current
    val isAllowed = remember(permission, permissionController.values) {
        permission == PermissionEnum.NO_PROTECTION || permissionController.hasPermission(permission)
    }

    if(isAllowed) {
        content()
    }
}