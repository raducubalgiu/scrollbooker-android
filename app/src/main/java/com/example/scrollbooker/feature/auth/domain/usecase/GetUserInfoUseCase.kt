package com.example.scrollbooker.feature.auth.domain.usecase
import com.example.scrollbooker.feature.auth.domain.model.UserInfo
import com.example.scrollbooker.feature.auth.domain.repository.UserInfoRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: UserInfoRepository
) {
    suspend operator fun invoke(): UserInfo {
        return repository.getUserInfo()
    }
}