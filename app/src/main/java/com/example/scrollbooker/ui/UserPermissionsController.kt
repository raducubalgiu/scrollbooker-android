package com.example.scrollbooker.ui

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.scrollbooker.core.enums.PermissionEnum

data class UserPermissionsController(
    val values: Set<String>,
    val hasEmployees: Boolean
) {
    fun has(code: PermissionEnum) = values.contains(code.key)
    fun verifyHasEmployees(has: Boolean): Boolean = has == hasEmployees
}

val LocalUserPermissions = staticCompositionLocalOf {
    UserPermissionsController(emptySet(), false)
}