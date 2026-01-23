package com.example.scrollbooker.ui

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.scrollbooker.core.enums.PermissionEnum

data class UserPermissionsController(
    val values: Set<String>,
    val hasEmployees: Boolean
) {
    fun hasPermission(code: PermissionEnum) = values.contains(code.key)
}

val LocalUserPermissions = staticCompositionLocalOf {
    UserPermissionsController(emptySet(), false)
}