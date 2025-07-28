package com.example.scrollbooker.entity.search.domain.useCase
import com.example.scrollbooker.entity.search.domain.model.RecentlySearch
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import javax.inject.Inject

class CreateUserSearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(keyword: String): Result<RecentlySearch> = runCatching {
        repository.createUserSearch(keyword)
    }
}