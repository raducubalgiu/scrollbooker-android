package com.example.scrollbooker.screens.auth.data.mappers

import com.example.scrollbooker.screens.auth.data.remote.userPermissions.PermissionDto
import com.example.scrollbooker.screens.auth.domain.model.Permission

fun List<PermissionDto>.toDomain(): List<Permission> {
    return map { Permission(code = it.code) }
}