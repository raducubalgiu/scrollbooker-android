package com.example.scrollbooker.entity.social.repost.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.repost.data.remote.RepostsApiService
import com.example.scrollbooker.entity.social.repost.data.remote.RepostsPagingSource
import com.example.scrollbooker.entity.social.repost.domain.repository.RepostsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepostsRepositoryImpl @Inject constructor(
    private val apiService: RepostsApiService
): RepostsRepository {
    override fun getUserRepostsPosts(userId: Int): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { RepostsPagingSource(apiService, userId) }
        ).flow
    }
}