package com.example.scrollbooker.feature.auth.data.mappers

import com.example.scrollbooker.feature.auth.data.remote.userPermissions.PermissionDto
import com.example.scrollbooker.feature.auth.domain.model.Permission

fun List<PermissionDto>.toDomain(): List<Permission> {
    return map { Permission(code = it.code) }
}