package com.example.scrollbooker.entity.user.userSocial.domain.useCase

import com.example.scrollbooker.entity.user.userSocial.domain.repository.UserSocialRepository

class FollowUserUseCase(
    private val repository: UserSocialRepository
) {
    suspend operator fun invoke(followeeId: Int) {
        repository.followUser(followeeId)
    }
}