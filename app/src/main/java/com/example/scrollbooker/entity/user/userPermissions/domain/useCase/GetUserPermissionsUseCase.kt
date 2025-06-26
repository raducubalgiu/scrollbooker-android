package com.example.scrollbooker.entity.user.userPermissions.domain.useCase

import com.example.scrollbooker.entity.user.userPermissions.domain.repository.PermissionRepository
import timber.log.Timber
import javax.inject.Inject

class GetUserPermissionsUseCase @Inject constructor(
    private val repository: PermissionRepository
) {
    suspend operator fun invoke(): List<String> {
        return try {
            repository.getUserPermissions().map { it.code }

        } catch (e: Exception) {
            Timber.Forest.tag("Permissions").e(e, "ERROR: on Fetching User Permissions")
            throw e
        }
    }
}