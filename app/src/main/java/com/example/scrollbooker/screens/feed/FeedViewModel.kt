package com.example.scrollbooker.screens.feed

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetBookNowPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetFollowingPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    application: Application,
    getBookNowPostsUseCase: GetBookNowPostsUseCase,
    getFollowingPostsUseCase: GetFollowingPostsUseCase
): ViewModel() {
    private val _selectedBusinessTypes = MutableStateFlow<Set<Int>>(emptySet())
    val selectedBusinessTypes: StateFlow<Set<Int>> = _selectedBusinessTypes

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookNowPosts: Flow<PagingData<Post>> = selectedBusinessTypes
        .map { it.toList() }
        .flatMapLatest { selectedTypes ->
            getBookNowPostsUseCase(selectedTypes)
        }
        .cachedIn(viewModelScope)

    private val _followingPosts: Flow<PagingData<Post>> by lazy {
        getFollowingPostsUseCase()
            .cachedIn(viewModelScope)
    }
    val followingPosts: Flow<PagingData<Post>> get() = _followingPosts

    fun setBusinessType(id: Int) {
        _selectedBusinessTypes.update { current ->
            if(current.contains(id)) current - id else current + id
        }
    }

    fun clearBusinessTypes() {
        _selectedBusinessTypes.value = emptySet()
    }

//    private val _exoPlayer = ExoPlayer.Builder(application)
//        .setHandleAudioBecomingNoisy(true)
//        .build().apply {
//            addListener(
//                object: Player.Listener {
//                    override fun onPlayerError(error: PlaybackException) {
//                        Timber.tag("Player").e("Exo Player error: ${error.message}")
//                        _errorState.value = error.message
//                    }
//
//                    override fun onPlaybackStateChanged(playbackState: Int) {
//                        _isBuffering.value = playbackState == Player.STATE_BUFFERING
//                    }
//                }
//            )
//        }
//    val exoPlayer: ExoPlayer get() = _exoPlayer
//
//    private val _currentPage = MutableStateFlow(0)
//    val currentPage: StateFlow<Int> get() = _currentPage
//
//    private val _currentTab = MutableStateFlow(0)
//    val currentTab: StateFlow<Int> get() = _currentTab
//
//    private val _errorState = MutableStateFlow<String?>(null)
//    val errorState: StateFlow<String?> get() = _errorState
//
//    private val _isBuffering = MutableStateFlow<Boolean>(false)
//    val isBuffering: StateFlow<Boolean> get() = _isBuffering

//    fun updateVideoForPage(page: Int, post: Post?, nextPost: Post?) {
//        viewModelScope.launch {
//            _currentPage.value = page
//            _errorState.value = null
//
//            _exoPlayer.stop()
//            _exoPlayer.clearMediaItems()
//
//            post?.let {
//                val url = it.mediaFiles.firstOrNull()?.url
//                if(url != null) {
//                    _exoPlayer.setMediaItem(MediaItem.fromUri(url))
//                }
//            }
//
//            nextPost?.let {
//                val url = it.mediaFiles.firstOrNull()?.url
//                if(url != null) {
//                    _exoPlayer.setMediaItem(MediaItem.fromUri(url))
//                }
//            }
//
//            _exoPlayer.prepare()
//            _exoPlayer.playWhenReady = true
//        }
//    }
//
//    fun updateTab(tabIndex: Int) {
//        _currentTab.value = tabIndex
//        _exoPlayer.stop()
//        _exoPlayer.clearMediaItems()
//        _currentPage.value = 0
//    }
//
//    fun pausePlayback() {
//        _exoPlayer.pause()
//    }
//
//    fun resumePlayback() {
//        if(_exoPlayer.mediaItemCount > 0 && !_exoPlayer.isPlaying) {
//            _exoPlayer.playWhenReady = true
//        }
//    }
//
//    override fun onCleared() {
//        _exoPlayer.release()
//        super.onCleared()
//    }
}