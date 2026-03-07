package com.example.scrollbooker.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.permission.domain.model.MediaVideo
import com.example.scrollbooker.entity.permission.domain.repository.PermissionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class CameraGalleryViewModel @Inject constructor(
    private val permissionRepository: PermissionRepository
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val videos: Flow<PagingData<MediaVideo>> = refreshTrigger
        .flatMapLatest {
            try {
                permissionRepository.videosPagingSource(pageSize = 60).flow
            } catch (e: Exception) {
                flowOf(PagingData.empty())
            }
        }
        .cachedIn(viewModelScope)

    fun refreshVideos() {
        refreshTrigger.value += 1
    }
}

