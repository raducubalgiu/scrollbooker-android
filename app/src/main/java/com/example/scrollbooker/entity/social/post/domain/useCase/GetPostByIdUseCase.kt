package com.example.scrollbooker.entity.social.post.domain.useCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository

class GetPostByIdUseCase(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Post {
        return repository.getPostById(postId)
    }
}