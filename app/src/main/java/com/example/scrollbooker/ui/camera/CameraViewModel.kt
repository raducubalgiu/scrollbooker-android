package com.example.scrollbooker.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.toCoverDataUri
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import com.example.scrollbooker.entity.permission.domain.repository.PermissionRepository
import com.example.scrollbooker.entity.social.post.domain.useCase.CreateVideoPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.CreateVideoReviewUseCase
import com.example.scrollbooker.navigation.navigators.NavigationEvent
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.ui.editPost.EditPostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

private const val DEFAULT_COVER_TIME_US = 500_000L
private const val FILMSTRIP_FRAME_HEIGHT = 220
private const val PREVIEW_FRAME_HEIGHT = 720

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val permissionRepository: PermissionRepository,
    private val createVideoPostUseCase: CreateVideoPostUseCase,
    private val createVideoReviewUseCase: CreateVideoReviewUseCase,
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    private val authDataStore: AuthDataStore,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
): ViewModel() {
    val appointmentId: Int = savedStateHandle.get<Int>("appointmentId") ?: -1
    val businessOrEmployeeId: Int = savedStateHandle.get<Int>("businessOrEmployeeId") ?: -1
    val isVideoReview: Boolean = appointmentId > 0 && businessOrEmployeeId > 0

    private val _description = MutableStateFlow<String>("")
    private val _linkedProducts = MutableStateFlow<Set<Product>>(emptySet())
    private val _userProducts = MutableStateFlow<FeatureState<UserProducts>>(FeatureState.Loading)

    private val _mediaThumbUri = MutableStateFlow<String?>(null)
    val mediaThumbUri: StateFlow<String?> = _mediaThumbUri.asStateFlow()

    private val _cameraVideoUiState = MutableStateFlow(CameraVideoUiState())
    val cameraVideoUiState: StateFlow<CameraVideoUiState> = _cameraVideoUiState.asStateFlow()

    val editUiState: StateFlow<EditPostUiState> = combine(
        _description,
        _linkedProducts,
        _userProducts
    ) { desc, linked, catalog ->
        if (isVideoReview) {
            EditPostUiState.Success(
                coverUrl = null,
                coverKey = null,
                description = desc,
                linkedProducts = emptySet(),
                catalogProducts = null
            )
        } else {
            when (catalog) {
                is FeatureState.Error -> EditPostUiState.Error(catalog.error)
                is FeatureState.Loading -> EditPostUiState.Loading
                is FeatureState.Success -> EditPostUiState.Success(
                    coverUrl = null,
                    coverKey = null,
                    description = desc,
                    linkedProducts = linked,
                    catalogProducts = catalog.data
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EditPostUiState.Loading
    )

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    private val _navigationEvents = Channel<NavigationEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    fun loadUserProducts() {
        viewModelScope.launch {
            _userProducts.value = FeatureState.Loading
            try {
                val userId = authDataStore.getUserId().firstOrNull()
                val businessId = authDataStore.getBusinessId().firstOrNull()
                val businessOwnerId = authDataStore.getBusinessOwnerId().firstOrNull()

                if (businessId == null) {
                    _userProducts.value = FeatureState.Error(
                        IllegalStateException("Business ID is missing.")
                    )
                    return@launch
                }

                val resolvedEmployeeId = if (businessOwnerId == userId) null else userId

                val result = getProductsByBusinessIdAndEmployeeIdUseCase(
                    businessId = businessId,
                    employeeId = resolvedEmployeeId,
                    onlyServicesWithProducts = true,
                    productsLimitPerService = null
                )
                _userProducts.value = result

            } catch (e: Exception) {
                Timber.tag("BookingProducts").e(e, "ERROR: Failed to read auth data or fetch products")
                _userProducts.value = FeatureState.Error(e)
            }
        }
    }

    fun setDescription(desc: String) {
        _description.value = desc
    }

    fun updateLinkedProducts(products: Set<Product>) {
        _linkedProducts.value = products
    }

    fun removeLinkedProduct(product: Product) {
        _linkedProducts.update { currentSet ->
            currentSet - product
        }
    }

    fun loadMediaThumb() {
        viewModelScope.launch(Dispatchers.IO) {
            val uri = permissionRepository.getLatestVideoThumbUriOrNull()
            _mediaThumbUri.value = uri?.toString()
        }
    }

    // Video Player
    private val _player = MutableStateFlow<Player?>(null)
    val player = _player.asStateFlow()

    private var exoPlayer: ExoPlayer? = null
    private var prepareJob: Job? = null
    private var coverJob: Job? = null

    // Filmstrip thumbnails for the cover picker, cached per video so revisiting the
    // cover screen doesn't re-extract the same frames from disk every time.
    private val _filmstrip = MutableStateFlow<List<Bitmap>>(emptyList())
    val filmstrip: StateFlow<List<Bitmap>> = _filmstrip.asStateFlow()

    private var filmstripKey: String? = null
    private var filmstripJob: Job? = null

    private fun videoKey(uri: Uri): String = uri.toString()

    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            val p = exoPlayer ?: return
            val currentUri = p.currentMediaItem?.localConfiguration?.uri
            val selectedUri = _cameraVideoUiState.value.selectedUri

            val readyForSelected =
                state == Player.STATE_READY && currentUri != null && currentUri == selectedUri

            _cameraVideoUiState.update {
                it.copy(
                    isReady = readyForSelected,
                    preparingUri = if (readyForSelected) null else it.preparingUri,
                    error = null
                )
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            _cameraVideoUiState.update { it.copy(error = error, preparingUri = null, isReady = false) }
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

        if(_cameraVideoUiState.value.selectedKey == key) return

        _cameraVideoUiState.value.coverUri?.let { old ->
            runCatching {
                val f = old.toFile()
                if(f.exists()) f.delete()
            }
        }

        coverJob?.cancel()
        filmstripJob?.cancel()
        filmstripKey = null
        _filmstrip.value = emptyList()

        _cameraVideoUiState.update {
            it.copy(
                selectedUri = uri,
                selectedKey = key,
                isCoverLoading = false,
                coverTimeUs = null,
                isCustomCover = false
            )
        }

        prepareSelected()
    }

    fun prepareSelected() {
        val uri = _cameraVideoUiState.value.selectedUri ?: return

        _cameraVideoUiState.update { it.copy(preparingUri = uri, isReady = false, error = null) }

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

        _cameraVideoUiState.update {
            it.copy(
                isReady = false,
                preparingUri = null,
                selectedUri = null,
                selectedKey = null,
                error = null
            )
        }

        filmstripJob?.cancel()
        filmstripKey = null
        _filmstrip.value = emptyList()

        releasePlayer()
    }

    fun releasePlayer() {
        prepareJob?.cancel()
        prepareJob = null
        exoPlayer?.removeListener(listener)
        exoPlayer?.release()
        exoPlayer = null
        _player.value = null
        _cameraVideoUiState.update { it.copy(isReady = false, preparingUri = null) }
    }

    override fun onCleared() {
        releasePlayer()
        super.onCleared()
    }

    fun generateCoverIfNeeded() {
        val uri = _cameraVideoUiState.value.selectedUri ?: return
        val key = _cameraVideoUiState.value.selectedKey ?: return

        if(_cameraVideoUiState.value.coverKey == key && _cameraVideoUiState.value.coverUri != null) return

        coverJob?.cancel()
        coverJob = viewModelScope.launch(Dispatchers.IO) {
            _cameraVideoUiState.update { it.copy(isCoverLoading = true) }

            val cover = runCatching { createVideoCover(context, uri) }.getOrNull()

            _cameraVideoUiState.update {
                it.copy(
                    coverUri = cover,
                    coverKey = key,
                    coverTimeUs = DEFAULT_COVER_TIME_US,
                    isCoverLoading = false
                )
            }
        }
    }

    /**
     * Extracts (once per video) a small set of evenly-spaced thumbnails used by the cover
     * picker's filmstrip, and caches them so re-opening the screen doesn't redo the work.
     */
    fun ensureFilmstrip(durationMs: Long, count: Int = 10) {
        val uri = _cameraVideoUiState.value.selectedUri ?: return
        val key = _cameraVideoUiState.value.selectedKey ?: return

        if (durationMs <= 0L) return
        if (filmstripKey == key && _filmstrip.value.isNotEmpty()) return

        filmstripJob?.cancel()
        filmstripJob = viewModelScope.launch(Dispatchers.IO) {
            val frames = extractFilmstrip(uri, durationMs, count)
            filmstripKey = key
            _filmstrip.value = frames
        }
    }

    private fun extractFilmstrip(uri: Uri, durationMs: Long, count: Int): List<Bitmap> {
        val retriever = MediaMetadataRetriever()
        return try {
            context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
                retriever.setDataSource(pfd.fileDescriptor)

                val intervalUs = (durationMs * 1_000L) / count
                (0 until count).mapNotNull { index ->
                    val timeUs = intervalUs * index + intervalUs / 2
                    extractScaledFrame(retriever, timeUs, FILMSTRIP_FRAME_HEIGHT)
                }
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        } finally {
            retriever.release()
        }
    }

    /**
     * Extracts a single, higher-resolution frame for the cover picker's big preview.
     * Called only once the user settles on a position (debounced on the screen side),
     * never on every drag frame, so it stays cheap despite the larger target size than
     * the filmstrip thumbnails.
     */
    suspend fun extractPreviewFrame(timeUs: Long): Bitmap? {
        val uri = _cameraVideoUiState.value.selectedUri ?: return null

        return withContext(Dispatchers.IO) {
            val retriever = MediaMetadataRetriever()
            try {
                context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
                    retriever.setDataSource(pfd.fileDescriptor)
                    extractScaledFrame(retriever, timeUs, PREVIEW_FRAME_HEIGHT)
                }
            } catch (e: Exception) {
                null
            } finally {
                retriever.release()
            }
        }
    }

    private fun extractScaledFrame(retriever: MediaMetadataRetriever, timeUs: Long, targetHeight: Int): Bitmap? {
        val frame = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC) ?: return null
        if (frame.height <= targetHeight) return frame

        val scale = targetHeight / frame.height.toFloat()
        return Bitmap.createScaledBitmap(
            frame,
            (frame.width * scale).toInt().coerceAtLeast(1),
            targetHeight,
            true
        )
    }

    private fun createVideoCover(context: Context, uri: Uri, timeUs: Long = DEFAULT_COVER_TIME_US): Uri? {
        val retriever = MediaMetadataRetriever()
        try {
            context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
                retriever.setDataSource(pfd.fileDescriptor)

                val bmp = retriever.getFrameAtTime(
                    timeUs,
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

    /**
     * Replaces the post cover with the frame at [timeUs] (microseconds) picked by the user
     * in the cover-selection screen. The previously generated cover file is deleted once the
     * new one is ready, mirroring the cleanup already done in [selectVideo].
     */
    fun setCoverAtTimestamp(timeUs: Long) {
        val uri = _cameraVideoUiState.value.selectedUri ?: return
        val key = _cameraVideoUiState.value.selectedKey ?: return

        coverJob?.cancel()
        coverJob = viewModelScope.launch(Dispatchers.IO) {
            _cameraVideoUiState.update { it.copy(isCoverLoading = true) }

            val previousCover = _cameraVideoUiState.value.coverUri
            val cover = runCatching { createVideoCover(context, uri, timeUs) }.getOrNull()

            if (cover != null && previousCover != null) {
                runCatching {
                    val f = previousCover.toFile()
                    if (f.exists()) f.delete()
                }
            }

            _cameraVideoUiState.update {
                it.copy(
                    coverUri = cover ?: it.coverUri,
                    // A unique key (not just the video key) so Coil doesn't reuse a stale
                    // cache entry when the user picks a different frame for the same video.
                    coverKey = if (cover != null) "${key}_$timeUs" else it.coverKey,
                    coverTimeUs = if (cover != null) timeUs else it.coverTimeUs,
                    isCoverLoading = false,
                    isCustomCover = if (cover != null) true else it.isCustomCover
                )
            }
        }
    }

    /**
     * Encodes the user-picked cover as a "data:image/jpeg;base64,..." URI for
     * [CreatePostRequest.customCover] / [CreateVideoReviewRequest.customCover]. Returns null
     * when the cover currently set is just the automatic default (BE then falls back to
     * Cloudflare Stream's own thumbnail). Downscaled before encoding since this rides along
     * in the JSON body, not a file upload.
     */
    private suspend fun getCustomCoverBase64(): String? {
        val state = _cameraVideoUiState.value
        if (!state.isCustomCover) return null
        val uri = state.coverUri ?: return null

        return withContext(Dispatchers.IO) {
            runCatching {
                BitmapFactory.decodeFile(uri.toFile().path)?.toCoverDataUri()
            }.getOrNull()
        }
    }

    fun createPost(videoUri: Uri) {
        viewModelScope.launch {
            _isSaving.value = FeatureState.Loading

            val customCover = getCustomCoverBase64()

            val result = if (appointmentId > 0 && businessOrEmployeeId > 0) {
                withVisibleLoading {
                    createVideoReviewUseCase(
                        businessOrEmployeeId = businessOrEmployeeId,
                        appointmentId = appointmentId,
                        videoUri = videoUri,
                        description = _description.value,
                        review = "Totul a fost excelent!",
                        rating = 5,
                        customCover = customCover,
                        onProgress = {},
                    )
                }
            } else {
                withVisibleLoading {
                    createVideoPostUseCase(
                        videoUri = videoUri,
                        description = _description.value,
                        linkedProductIds = _linkedProducts.value.map { it.id },
                        customCover = customCover,
                        onProgress = {}
                    )
                }
            }

            result
                .onFailure { e ->
                    _isSaving.value = FeatureState.Error(e)
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())

                    Timber.tag("CreatePost").e(e, "ERROR: on creating video post")
                }
                .onSuccess {
                    _isSaving.value = FeatureState.Success(Unit)
                    _navigationEvents.send(NavigationEvent.NavigateToProfile)
                }
        }
    }
}