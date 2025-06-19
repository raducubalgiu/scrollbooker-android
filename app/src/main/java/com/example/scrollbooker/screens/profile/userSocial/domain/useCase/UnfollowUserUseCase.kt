package com.example.scrollbooker.screens.profile.userSocial.domain.useCase

import com.example.scrollbooker.screens.profile.userSocial.domain.repository.UserSocialRepository

class UnfollowUserUseCase(
    private val repository: UserSocialRepository
) {
    suspend operator fun invoke(followeeId: Int) {
        repository.unfollowUser(followeeId)
    }
}