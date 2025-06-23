package com.example.scrollbooker.shared.user.userPermissions.data.mappers

import com.example.scrollbooker.shared.user.userPermissions.data.remote.PermissionDto
import com.example.scrollbooker.shared.user.userPermissions.domain.model.Permission

fun List<PermissionDto>.toDomain(): List<Permission> {
    return map { Permission(code = it.code) }
}