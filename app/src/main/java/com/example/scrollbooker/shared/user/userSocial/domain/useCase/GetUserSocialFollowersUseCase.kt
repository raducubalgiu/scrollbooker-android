package com.example.scrollbooker.shared.user.userSocial.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.shared.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.shared.user.userSocial.domain.repository.UserSocialRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class GetUserSocialFollowersUseCase(
    private val repository: UserSocialRepository,
) {
    operator fun invoke(userId: Int): Flow<PagingData<UserSocial>> {
        return repository.getUserFollowers(userId)
    }
}