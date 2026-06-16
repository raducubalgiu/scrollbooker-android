package com.example.scrollbooker.ui.profile

import androidx.paging.PagingData
import com.example.scrollbooker.components.customized.post.PostActionUiState
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ProfilePostDetailViewModelContract {
    val profile: StateFlow<FeatureState<UserProfile>>
    val posts: Flow<PagingData<Post>>
    val bookmarks: Flow<PagingData<Post>>
    val employees: Flow<PagingData<Employee>>
    val products: StateFlow<FeatureState<UserProducts>>
    val about: StateFlow<FeatureState<UserProfileAbout>>
    val schedules: StateFlow<FeatureState<List<Schedule>>>

    val userPausedPostIds: StateFlow<Set<Int>>
    fun setDetailScreenActive(isActive: Boolean, scopeKey: String, initialIndex: Int, getPost: (Int) -> Post?)
    fun onDetailSessionFinished(scopeKey: String)
    fun onPostSettled(scopeKey: String, index: Int, getPost: (Int) -> Post?)
    fun getPlayerForIndex(scopeKey: String, index: Int): androidx.media3.common.Player?
    fun observePostUi(postId: Int): StateFlow<PostActionUiState>
    fun togglePlayPause(scopeKey: String, index: Int)
    fun toggleLike(post: Post)
    fun toggleBookmark(post: Post)
}