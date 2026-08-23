package com.example.scrollbooker.entity.search.domain.useCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.search.domain.model.RecentSearch
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import javax.inject.Inject

class GetRecentSearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(): Result<List<RecentSearch>> {
        return runSuspendCatching {
            repository.getRecentSearch()
        }
    }
}