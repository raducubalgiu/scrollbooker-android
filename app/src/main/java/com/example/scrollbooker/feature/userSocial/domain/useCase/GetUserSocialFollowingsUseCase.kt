package com.example.scrollbooker.feature.userSocial.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.feature.userSocial.domain.model.UserSocial
import com.example.scrollbooker.feature.userSocial.domain.repository.UserSocialRepository
import kotlinx.coroutines.flow.Flow

class GetUserSocialFollowingsUseCase(
    private val repository: UserSocialRepository
) {
    operator fun invoke(userId: Int): Flow<PagingData<UserSocial>> {
        return repository.getUserFollowings(userId)
    }
}