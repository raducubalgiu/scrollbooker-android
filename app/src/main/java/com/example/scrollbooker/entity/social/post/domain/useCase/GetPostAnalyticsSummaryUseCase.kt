package com.example.scrollbooker.entity.social.post.domain.useCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.social.post.domain.model.PostAnalyticsSummary
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository

class GetPostAnalyticsSummaryUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Result<PostAnalyticsSummary> {
        return runSuspendCatching {
            repository.getPostAnalyticsSummary(postId)
        }
    }
}