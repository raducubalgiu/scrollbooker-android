package com.example.scrollbooker.shared.user.userSocial.domain.useCase

import com.example.scrollbooker.shared.user.userSocial.domain.repository.UserSocialRepository

class FollowUserUseCase(
    private val repository: UserSocialRepository
) {
    suspend operator fun invoke(followeeId: Int) {
        repository.followUser(followeeId)
    }
}