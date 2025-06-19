package com.example.scrollbooker.screens.auth.domain.usecase
import com.example.scrollbooker.screens.auth.domain.model.UserInfo
import com.example.scrollbooker.screens.auth.domain.repository.UserInfoRepository
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