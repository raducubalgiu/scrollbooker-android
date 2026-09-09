package com.example.scrollbooker.entity.social.post.domain.useCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.social.post.domain.model.PostMediaStatus
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository

class GetPostsMediaStatusUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(ids: List<Int>): Result<List<PostMediaStatus>> {
        return runSuspendCatching {
            repository.getPostsMediaStatus(ids)
        }
    }
}
