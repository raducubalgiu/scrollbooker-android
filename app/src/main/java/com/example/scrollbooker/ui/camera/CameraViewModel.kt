package com.example.scrollbooker.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.scrollbooker.core.extensions.uriToCacheFile
import com.example.scrollbooker.entity.social.cloudflare.domain.useCase.CreatePostWithCloudflareUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val createPostWithCloudflareUseCase: CreatePostWithCloudflareUseCase,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    // Camera
    val cameraController: LifecycleCameraController by lazy {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(LifecycleCameraController.VIDEO_CAPTURE)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    private val _isBound = MutableStateFlow(false)
    val isBound: StateFlow<Boolean> = _isBound.asStateFlow()

    fun bindIfNeeded(owner: LifecycleOwner) {
        if(_isBound.value) return
        cameraController.bindToLifecycle(owner)
        _isBound.value = true
    }

    fun switchCamera() {
        cameraController.cameraSelector =
            if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
    }

    private val _isCameraMounted = MutableStateFlow<Boolean>(false)
    val isCameraMounted: StateFlow<Boolean> = _isCameraMounted.asStateFlow()

    fun setIsCameraMounted(mounted: Boolean) {
        _isCameraMounted.value = mounted
    }

    // Video Player
    data class UiState(
        val selectedUri: Uri? = null,
        val selectedKey: String? = null,
        val preparingUri: Uri? = null,
        val isReady: Boolean = false,
        val error: Throwable? = null,
        val coverUri: Uri? = null,
        val coverKey: String? = null,
        val isCoverLoading: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _player = MutableStateFlow<Player?>(null)
    val player = _player.asStateFlow()

    private var exoPlayer: ExoPlayer? = null
    private var prepareJob: Job? = null
    private var coverJob: Job? = null

    private fun videoKey(uri: Uri): String = uri.toString()

    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            val p = exoPlayer ?: return
            val currentUri = p.currentMediaItem?.localConfiguration?.uri
            val selectedUri = _uiState.value.selectedUri

            val readyForSelected =
                state == Player.STATE_READY && currentUri != null && currentUri == selectedUri

            _uiState.update {
                it.copy(
                    isReady = readyForSelected,
                    preparingUri = if (readyForSelected) null else it.preparingUri,
                    error = null
                )
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            _uiState.update { it.copy(error = error, preparingUri = null, isReady = false) }
        }
    }

    private fun ensurePlayer(): ExoPlayer {
        return exoPlayer ?: ExoPlayer.Builder(context).build().also { p ->
            p.repeatMode = Player.REPEAT_MODE_ONE
            p.playWhenReady = false
            p.addListener(listener)
            exoPlayer = p
            _player.value = p
        }
    }

    fun selectVideo(uri: Uri) {
        val key = videoKey(uri)

        if(_uiState.value.selectedKey == key) return

        _uiState.value.coverUri?.let { old ->
            runCatching {
                val f = old.toFile()
                if(f.exists()) f.delete()
            }
        }

        coverJob?.cancel()

        _uiState.update {
            it.copy(
                selectedUri = uri,
                selectedKey = key,
                isCoverLoading = false
            )
        }

        prepareSelected()
    }

    fun prepareSelected() {
        val uri = _uiState.value.selectedUri ?: return

        _uiState.update { it.copy(preparingUri = uri, isReady = false, error = null) }

        prepareJob?.cancel()
        prepareJob = viewModelScope.launch(Dispatchers.Main.immediate) {
            val p = ensurePlayer()
            p.setMediaItem(MediaItem.fromUri(uri))
            p.prepare()
        }
    }

    fun play() {
        exoPlayer?.playWhenReady = true
    }

    fun pause() {
        exoPlayer?.playWhenReady = false
        exoPlayer?.pause()
    }

    fun onBackToGallery() {
        pause()

        _uiState.update {
            it.copy(
                isReady = false,
                preparingUri = null,
                selectedUri = null,
                error = null
            )
        }

        releasePlayer()
    }

    fun releasePlayer() {
        prepareJob?.cancel()
        prepareJob = null
        exoPlayer?.removeListener(listener)
        exoPlayer?.release()
        exoPlayer = null
        _player.value = null
        _uiState.update { it.copy(isReady = false, preparingUri = null) }
    }

    override fun onCleared() {
        releasePlayer()
        super.onCleared()
    }

    fun generateCoverIfNeeded() {
        val uri = _uiState.value.selectedUri ?: return
        val key = _uiState.value.selectedKey ?: return

        if(_uiState.value.coverKey == key && _uiState.value.coverUri != null) return

        coverJob?.cancel()
        coverJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isCoverLoading = true) }

            val cover = runCatching { createVideoCover(context, uri) }.getOrNull()

            _uiState.update {
                it.copy(
                    coverUri = cover,
                    coverKey = key,
                    isCoverLoading = false
                )
            }
        }
    }

    private fun createVideoCover(context: Context, uri: Uri): Uri? {
        val retriever = MediaMetadataRetriever()
        try {
            context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
                retriever.setDataSource(pfd.fileDescriptor)

                val bmp = retriever.getFrameAtTime(
                    500_000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                ) ?: return null

                val file = File(context.cacheDir, "cover_${System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { out ->
                    bmp.compress(Bitmap.CompressFormat.JPEG, 85, out)
                }

                return file.toUri()
            }
            return null
        } finally {
            retriever.release()
        }
    }

    fun createPost(
        description: String?,
        videoUri: Uri
    ) {
        viewModelScope.launch {
            _isSaving.value = true

            runCatching {
                val file = uriToCacheFile(context, videoUri)

                createPostWithCloudflareUseCase(
                    videoFile = file,
                    description = description
                )
            }
                .onSuccess {
                    _isSaving.value = false
                }
                .onFailure { e ->
                    _isSaving.value = false
                    Timber.tag("Create Post").e("Error: on creating post: $e")
                }
        }
    }
}