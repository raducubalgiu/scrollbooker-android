package com.example.scrollbooker.entity.search.domain.useCase
import com.example.scrollbooker.entity.search.domain.repository.SearchRepository
import javax.inject.Inject

class DeleteUserSearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(searchId: Int): Result<Unit> = runCatching {
        repository.deleteUserSearch(searchId)
    }
}