package com.example.scrollbooker.feature.auth.domain.usecase
import com.example.scrollbooker.feature.auth.domain.model.UserInfo
import com.example.scrollbooker.feature.auth.domain.repository.UserInfoRepository
import timber.log.Timber
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: UserInfoRepository
) {
    suspend operator fun invoke(): UserInfo {
        return try {
            repository.getUserInfo()
        } catch (e: Exception) {
            Timber.tag("User Info").e(e, "ERROR: on Fetching User Info")
            throw e
        }
    }
}