package com.example.scrollbooker.shared.user.userInfo.domain.useCase

import com.example.scrollbooker.shared.user.userInfo.domain.model.UserInfo
import com.example.scrollbooker.shared.user.userInfo.domain.repository.UserInfoRepository
import timber.log.Timber
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: UserInfoRepository
) {
    suspend operator fun invoke(): UserInfo {
        return try {
            repository.getUserInfo()
        } catch (e: Exception) {
            Timber.Forest.tag("User Info").e(e, "ERROR: on Fetching User Info")
            throw e
        }
    }
}