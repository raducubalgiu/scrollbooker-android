package com.example.scrollbooker.screens.profile.userSocial.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.screens.profile.userSocial.domain.model.UserSocial
import com.example.scrollbooker.screens.profile.userSocial.domain.repository.UserSocialRepository
import kotlinx.coroutines.flow.Flow

class GetUserSocialFollowersUseCase(
    private val repository: UserSocialRepository,
) {
    operator fun invoke(userId: Int): Flow<PagingData<UserSocial>> {
        return repository.getUserFollowers(userId)
    }
}