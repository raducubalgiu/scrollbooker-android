package com.example.scrollbooker.core.presentation

import android.net.Uri
import com.example.scrollbooker.entity.social.post.domain.useCase.CreateVideoPostUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

sealed interface GlobalUploadStatus {
    object Idle : GlobalUploadStatus
    data class Uploading(val progress: Float, val coverUri: Uri?) : GlobalUploadStatus
    object Success : GlobalUploadStatus
    data class Error(val message: String) : GlobalUploadStatus
}

@Singleton
class UploadManager @Inject constructor(
    private val createVideoPostUseCase: CreateVideoPostUseCase
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uploadStatus = MutableStateFlow<GlobalUploadStatus>(GlobalUploadStatus.Idle)
    val uploadStatus: StateFlow<GlobalUploadStatus> = _uploadStatus.asStateFlow()

    fun startUpload(
        videoUri: Uri,
        coverUri: Uri?,
        description: String?,
        businessOrEmployeeId: Int? = null,
        isVideoReview: Boolean = false,
        videoReviewMessage: String? = null,
        rating: Int? = null
    ) {
        if (_uploadStatus.value is GlobalUploadStatus.Uploading) return

        scope.launch {
            _uploadStatus.value = GlobalUploadStatus.Uploading(progress = 0f, coverUri = coverUri)

            val result = createVideoPostUseCase(
                videoUri = videoUri,
                description = description,
                businessOrEmployeeId = businessOrEmployeeId,
                isVideoReview = isVideoReview,
                videoReviewMessage = videoReviewMessage,
                rating = rating,
                onProgress = { percentage ->
                    _uploadStatus.value = GlobalUploadStatus.Uploading(
                        progress = (percentage / 100).toFloat(),
                        coverUri = coverUri
                    )
                }
            )

            _uploadStatus.value = result.fold(
                onSuccess = { GlobalUploadStatus.Success },
                onFailure = { exception ->
                    Timber.tag("GlobalUpload").e(exception, "Upload failed in background")
                    GlobalUploadStatus.Error(exception.localizedMessage ?: "Upload failed.")
                }
            )
        }
    }

    fun resetStatus() {
        _uploadStatus.value = GlobalUploadStatus.Idle
    }
}