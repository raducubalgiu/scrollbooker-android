package com.example.scrollbooker.screens.auth.domain.usecase
import com.example.scrollbooker.screens.auth.domain.repository.PermissionRepository
import timber.log.Timber
import javax.inject.Inject

class GetUserPermissionsUseCase @Inject constructor(
    private val repository: PermissionRepository
) {
    suspend operator fun invoke(): List<String> {
        return try {
            repository.getUserPermissions().map { it.code }

        } catch (e: Exception) {
            Timber.tag("Permissions").e(e, "ERROR: on Fetching User Permissions")
            throw e
        }
    }
}