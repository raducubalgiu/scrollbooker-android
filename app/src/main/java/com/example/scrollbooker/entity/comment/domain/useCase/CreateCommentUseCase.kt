package com.example.scrollbooker.entity.comment.domain.useCase
import com.example.scrollbooker.entity.comment.data.mappers.toDomain
import com.example.scrollbooker.entity.comment.domain.model.Comment
import com.example.scrollbooker.entity.comment.domain.repository.CommentRepository
import timber.log.Timber
import javax.inject.Inject

class CreateCommentUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: Int, text: String, parentId: Int?): Comment? {
        return runCatching {
            repository.createComment(postId = postId, text = text, parentId = parentId)
        }.onFailure {
            Timber.tag("Create Comment").e("ERROR: on Creating Comment: $it")
        }.getOrNull()?.toDomain()
    }
}