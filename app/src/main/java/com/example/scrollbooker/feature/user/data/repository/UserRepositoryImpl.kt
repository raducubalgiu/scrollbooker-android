package com.example.scrollbooker.feature.user.data.repository
import com.example.scrollbooker.feature.auth.data.mappers.toDomain
import com.example.scrollbooker.feature.user.data.mappers.toDomain
import com.example.scrollbooker.feature.user.data.remote.UserApiService
import com.example.scrollbooker.feature.auth.domain.model.Permission
import com.example.scrollbooker.feature.user.domain.model.UpdateBioRequest
import com.example.scrollbooker.feature.user.domain.model.UpdateFullNameRequest
import com.example.scrollbooker.feature.user.domain.model.UpdateUsernameRequest
import com.example.scrollbooker.feature.user.domain.model.User
import com.example.scrollbooker.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
): UserRepository {
    override suspend fun getUserInfo(): User {
        return userApiService.getUserInfo().toDomain()
    }

    override suspend fun getUserPermissions(): List<Permission> {
        return userApiService.getUserPermissions().toDomain()
    }

    override suspend fun updateFullName(fullName: String) {
        return userApiService.updateUserFullName(UpdateFullNameRequest(
            fullname = fullName
        ))
    }

    override suspend fun updateUsername(username: String) {
        return userApiService.updateUsername(UpdateUsernameRequest(username))
    }

    override suspend fun updateBio(bio: String) {
        return userApiService.updateBio(UpdateBioRequest(bio))
    }
}