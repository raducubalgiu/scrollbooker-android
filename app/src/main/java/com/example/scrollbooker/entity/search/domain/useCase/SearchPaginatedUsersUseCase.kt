package com.example.scrollbooker.entity.search.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import kotlinx.coroutines.flow.Flow

class SearchPaginatedUsersUseCase(
    private val repository: SearchRepository
) {
    operator fun invoke(query: String): Flow<PagingData<UserSocial>> {
        return repository.searchPaginatedUsers(query)
    }
}