package com.example.scrollbooker.feature.reposts.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.feature.posts.domain.model.Post
import com.example.scrollbooker.feature.reposts.data.remote.RepostsApiService
import com.example.scrollbooker.feature.reposts.data.remote.RepostsPagingSource
import com.example.scrollbooker.feature.reposts.domain.repository.RepostsRepository
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