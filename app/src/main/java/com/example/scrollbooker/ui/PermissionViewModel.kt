package com.example.scrollbooker.ui
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.permission.domain.model.MediaLibraryAccess
import com.example.scrollbooker.entity.permission.domain.model.MediaVideo
import com.example.scrollbooker.entity.permission.domain.repository.PermissionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PermissionState(
    val mediaAccess: MediaLibraryAccess = MediaLibraryAccess.NONE,
    val mediaRequestOnce: Boolean = false,

    val mediaThumbUri: Uri? = null,
    val isThumbLoading: Boolean = false,

    val videos: List<MediaVideo> = emptyList(),
    val isVideosLoading: Boolean = false,

    val lastRefreshAtMs: Long = 0L,
    val lastMediaAccess: MediaLibraryAccess = MediaLibraryAccess.NONE,
)

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val repo: PermissionRepository
): ViewModel() {
    private val _mediaState = MutableStateFlow(PermissionState())
    val mediaState: StateFlow<PermissionState> = _mediaState.asStateFlow()

    private val pagingTrigger = MutableStateFlow(0)

    init {
        refreshMedia(force = true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val videos: Flow<PagingData<MediaVideo>> =
        combine(
            mediaState.map { it.mediaAccess }.distinctUntilChanged(),
            pagingTrigger
        ) { access, _ -> access }
            .flatMapLatest { access ->
                if (access == MediaLibraryAccess.NONE) {
                    flowOf(PagingData.empty())
                } else {
                    repo.videosPagingSource(pageSize = 60).flow
                }
            }
            .cachedIn(viewModelScope)

    fun refreshMedia(force: Boolean = false) {
        val newAccess = repo.getMediaLibraryAccess()
        val requestedOnce = repo.wasMediaPermissionRequestedOnce()

        val now = System.currentTimeMillis()
        val current = _mediaState.value

        val accessUnchanged = newAccess == current.mediaAccess
        val tooSoon = (now - current.lastRefreshAtMs) < 500L

        if (!force && accessUnchanged && tooSoon) return

        _mediaState.update {
            it.copy(
                mediaAccess = newAccess,
                mediaRequestOnce = requestedOnce,
                lastRefreshAtMs = now
            )
        }

        if(current.mediaAccess != newAccess) {
            pagingTrigger.update { it + 1 }
        }

        when (newAccess) {
            MediaLibraryAccess.NONE -> {
                _mediaState.update {
                    it.copy(
                        mediaThumbUri = null,
                        isThumbLoading = false,
                        isVideosLoading = false,
                        lastMediaAccess = newAccess
                    )
                }
            }

            MediaLibraryAccess.FULL, MediaLibraryAccess.LIMITED -> {
                val shouldReloadThumb =
                    force ||
                    current.mediaThumbUri == null ||
                    current.lastMediaAccess != newAccess

                if (shouldReloadThumb) {
                    loadLatestVideoThumb(newAccess)
                } else {
                    _mediaState.update { it.copy(lastMediaAccess = newAccess) }
                }

                if (current.mediaAccess != newAccess) pagingTrigger.update { it + 1 }
            }
        }
    }

    fun markMediaPermissionRequested() {
        repo.markMediaPermissionRequestedOnce()
        _mediaState.update { it.copy(mediaRequestOnce = true) }
    }

    private fun loadLatestVideoThumb(access: MediaLibraryAccess) {
        _mediaState.update { it.copy(isThumbLoading = true) }

        viewModelScope.launch {
            val uri: Uri? = repo.getLatestVideoThumbUriOrNull()
            _mediaState.update {
                it.copy(
                    mediaThumbUri = uri,
                    isThumbLoading = false,
                    lastMediaAccess = access
                )
            }
        }
    }
}