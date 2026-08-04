package com.example.scrollbooker.ui.camera

import android.content.Context
import android.graphics.Bitmap
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
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import com.example.scrollbooker.entity.permission.domain.repository.PermissionRepository
import com.example.scrollbooker.entity.social.post.domain.useCase.CreateVideoPostUseCase
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

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val permissionRepository: PermissionRepository,
    private val createVideoPostUseCase: CreateVideoPostUseCase,
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    private val authDataStore: AuthDataStore,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
): ViewModel() {
    val isVideoReview: Boolean = savedStateHandle.get<Boolean>("isVideoReview") == true
    val businessOrEmployeeId: Int = savedStateHandle.get<Int>("businessOrEmployeeId") ?: -1

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
    ) { desc: String, linked: Set<Product>, catalog: FeatureState<UserProducts> ->
        val result: EditPostUiState = when (catalog) {
            is FeatureState.Error -> EditPostUiState.Error(catalog.error)
            is FeatureState.Loading -> EditPostUiState.Loading
            is FeatureState.Success -> {
                EditPostUiState.Success(
                    coverUrl = null,
                    coverKey = null,
                    description = desc,
                    linkedProducts = linked,
                    catalogProducts = catalog.data
                )
            }
        }
        result
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

        _cameraVideoUiState.update {
            it.copy(
                selectedUri = uri,
                selectedKey = key,
                isCoverLoading = false
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

    fun createPost(videoUri: Uri) {
        viewModelScope.launch {
            _isSaving.value = FeatureState.Loading

            val result = withVisibleLoading {
                createVideoPostUseCase(
                    videoUri = videoUri,
                    description = _description.value,
                    linkedProductIds = _linkedProducts.value.map { it.id },
                    onProgress = {}
                )
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