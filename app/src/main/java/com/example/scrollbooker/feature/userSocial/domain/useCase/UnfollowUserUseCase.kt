package com.example.scrollbooker.feature.userSocial.domain.useCase

import com.example.scrollbooker.feature.userSocial.domain.repository.UserSocialRepository

class UnfollowUserUseCase(
    private val repository: UserSocialRepository
) {
    suspend operator fun invoke(followeeId: Int) {
        repository.unfollowUser(followeeId)
    }
}