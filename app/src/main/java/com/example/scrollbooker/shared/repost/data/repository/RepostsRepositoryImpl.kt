package com.example.scrollbooker.shared.repost.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.shared.post.domain.model.Post
import com.example.scrollbooker.shared.repost.data.remote.RepostsApiService
import com.example.scrollbooker.shared.repost.data.remote.RepostsPagingSource
import com.example.scrollbooker.shared.repost.domain.repository.RepostsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepostsRepositoryImpl @Inject constructor(
    private val apiService: RepostsApiService
): RepostsRepository {
    override fun getUserRepostsPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { RepostsPagingSource(apiService) }
        ).flow
    }
}