package com.example.scrollbooker.screens.profile.userSocial.domain.useCase

import com.example.scrollbooker.screens.profile.userSocial.domain.repository.UserSocialRepository

class FollowUserUseCase(
    private val repository: UserSocialRepository
) {
    suspend operator fun invoke(followeeId: Int) {
        repository.followUser(followeeId)
    }
}