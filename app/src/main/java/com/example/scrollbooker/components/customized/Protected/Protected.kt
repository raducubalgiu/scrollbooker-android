package com.example.scrollbooker.components.customized.Protected

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
        permissionController.has(permission)
    }

    if(isAllowed) {
        content()
    }
}