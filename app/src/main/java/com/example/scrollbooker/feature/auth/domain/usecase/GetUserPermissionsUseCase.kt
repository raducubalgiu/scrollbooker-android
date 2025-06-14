package com.example.scrollbooker.feature.auth.domain.usecase
import com.example.scrollbooker.feature.auth.data.remote.AuthApiService
import timber.log.Timber
import javax.inject.Inject

class GetUserPermissionsUseCase @Inject constructor(
    private val userApi: AuthApiService
) {
    suspend operator fun invoke(): List<String> {
        try {
            return userApi.getUserPermissions().map { it.code }

        } catch (e: Exception) {
            Timber.tag("Permissions").e(e, "ERROR: on Fetching User Permissions")
            throw e
        }
    }
}