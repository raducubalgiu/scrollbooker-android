package com.example.scrollbooker.ui.editPost

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.scrollbooker.components.customized.post.PostInteractionStore
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.toCoverDataUri
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetPostLinkedProductsUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.SelectedServiceDomainsWithServices
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.useCase.GetSelectedServiceDomainsWithServicesByBusinessIdUseCase
import com.example.scrollbooker.entity.social.post.domain.model.PostMediaFile
import com.example.scrollbooker.entity.social.post.domain.useCase.GetPostByIdUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UpdatePostUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    private val getSelectedServiceDomainsWithServicesByBusinessIdUseCase: GetSelectedServiceDomainsWithServicesByBusinessIdUseCase,
    private val getPostLinkedProductsUseCase: GetPostLinkedProductsUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val authDataStore: AuthDataStore,
    private val postInteractionStore: PostInteractionStore,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val postId: Int = savedStateHandle["postId"] ?: error("Missing postId")

    private val _description = MutableStateFlow<String?>(null)
    private val _linkedProducts = MutableStateFlow<Set<Product>>(emptySet())
    private val _catalogProducts = MutableStateFlow<FeatureState<UserProducts>>(FeatureState.Loading)
    private val _postMedia = MutableStateFlow<PostMediaFile?>(null)

    private val _selectedServiceDomainId = MutableStateFlow<Int?>(null)
    val selectedServiceDomainId: StateFlow<Int?> = _selectedServiceDomainId.asStateFlow()

    private val _pendingCoverUri = MutableStateFlow<Uri?>(null)
    private var pendingCoverDataUri: String? = null

    private val _pendingCoverTimeUs = MutableStateFlow<Long?>(null)
    val pendingCoverTimeUs: StateFlow<Long?> = _pendingCoverTimeUs.asStateFlow()

    val uiState: StateFlow<EditPostUiState> = combine(
        _description,
        _linkedProducts,
        _catalogProducts,
        _postMedia,
        _pendingCoverUri
    ) { desc, linked, catalog, media, pendingCoverUri ->
        when {
            catalog is FeatureState.Error -> EditPostUiState.Error(catalog.error)
            desc == null || catalog is FeatureState.Loading -> EditPostUiState.Loading

            catalog is FeatureState.Success -> {
                EditPostUiState.Success(
                    coverUrl = pendingCoverUri?.toString() ?: media?.customCoverUrl ?: media?.thumbnailUrl,
                    coverKey = pendingCoverUri?.toString(),
                    description = desc,
                    linkedProducts = linked,
                    catalogProducts = catalog.data
                )
            }
            else -> EditPostUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EditPostUiState.Loading
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val serviceDomainsState: StateFlow<FeatureState<List<SelectedServiceDomainsWithServices>>> = authDataStore
        .getBusinessId()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { businessId ->
            flow {
                emit(FeatureState.Loading)
                val result = getSelectedServiceDomainsWithServicesByBusinessIdUseCase(businessId)
                emit(result)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Loading
        )

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving.asStateFlow()

    private val _player = MutableStateFlow<Player?>(null)
    val player: StateFlow<Player?> = _player.asStateFlow()

    private val _isPlayerReady = MutableStateFlow(false)
    val isPlayerReady: StateFlow<Boolean> = _isPlayerReady.asStateFlow()

    private val _filmstrip = MutableStateFlow<List<Bitmap>>(emptyList())
    val filmstrip: StateFlow<List<Bitmap>> = _filmstrip.asStateFlow()

    private var exoPlayer: ExoPlayer? = null
    private var playerPrepared = false

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            _isPlayerReady.value = state == Player.STATE_READY
        }
    }

    init {
        initDataLoading()
    }

    private fun initDataLoading() {
        viewModelScope.launch {
            val postDeferred = async { runCatching { getPostByIdUseCase(postId) } }
            val linkedProductsDeferred = async { getPostLinkedProductsUseCase(postId) }

            launch { loadCatalogProducts() }

            postDeferred.await()
                .onSuccess { post ->
                    val currentUserId = authDataStore.getUserId().firstOrNull()

                    if (post.user.id != currentUserId) {
                        _catalogProducts.value = FeatureState.Error(SecurityException("Neautorizat"))
                        _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                        return@onSuccess
                    }
                    _description.value = post.description ?: ""
                    _postMedia.value = post.mediaFiles.firstOrNull() ?: error("Post has no media files")
                    _selectedServiceDomainId.value = post.serviceDomain?.id
                }
                .onFailure { e ->
                    Timber.tag("EditPost").e(e, "Failed to load post details")
                    _catalogProducts.value = FeatureState.Error(e)
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                }

            val linkedResult = linkedProductsDeferred.await()
            if (linkedResult.isSuccess) {
                _linkedProducts.value = linkedResult.getOrDefault(emptyList()).toSet()
            } else {
                Timber.tag("EditPost").e(linkedResult.exceptionOrNull(), "Failed to load linked products")
                _linkedProducts.value = emptySet()
            }
        }
    }

    private suspend fun loadCatalogProducts() {
        _catalogProducts.value = FeatureState.Loading
        try {
            val userId = authDataStore.getUserId().firstOrNull()
            val businessId = authDataStore.getBusinessId().firstOrNull()
            val businessOwnerId = authDataStore.getBusinessOwnerId().firstOrNull()

            if (businessId == null) {
                _catalogProducts.value = FeatureState.Error(IllegalStateException("Missing Business ID"))
                return
            }

            val resolvedEmployeeId = if (businessOwnerId == userId) null else userId
            val catalogResult = getProductsByBusinessIdAndEmployeeIdUseCase(
                businessId = businessId,
                employeeId = resolvedEmployeeId,
                onlyServicesWithProducts = true,
                productsLimitPerService = null
            )
            _catalogProducts.value = catalogResult
        } catch (e: Exception) {
            _catalogProducts.value = FeatureState.Error(e)
        }
    }

    fun setDescription(desc: String) {
        _description.value = desc
    }

    fun updateLinkedProducts(products: Set<Product>) {
        _linkedProducts.value = products
    }

    fun removeLinkedProduct(product: Product) {
        _linkedProducts.value = _linkedProducts.value.minus(product)
    }

    fun setSelectedServiceDomainId(id: Int?) {
        _selectedServiceDomainId.value = id
    }

    fun ensurePlayerPrepared() {
        if (playerPrepared) return
        val url = _postMedia.value?.url ?: return
        playerPrepared = true

        val p = exoPlayer ?: ExoPlayer.Builder(context).build().also {
            it.playWhenReady = false
            it.addListener(playerListener)
            exoPlayer = it
            _player.value = it
        }
        p.setMediaItem(MediaItem.fromUri(url))
        p.prepare()
    }

    fun setFilmstrip(frames: List<Bitmap>) {
        _filmstrip.value = frames
    }

    /**
     * Saves the frame the user picked in the cover screen: keeps a local copy for an
     * immediate preview in the header (same UX as the create-post cover picker) and the
     * base64 data URI that will actually be sent to BE once the user taps save.
     */
    fun setPendingCover(bitmap: Bitmap, timeUs: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val dataUri = bitmap.toCoverDataUri()

            val previousFile = _pendingCoverUri.value?.toFile()
            val file = File(context.cacheDir, "edit_cover_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out -> bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out) }
            runCatching { if (previousFile != null && previousFile.exists()) previousFile.delete() }

            pendingCoverDataUri = dataUri
            _pendingCoverUri.value = file.toUri()
            _pendingCoverTimeUs.value = timeUs
        }
    }

    private fun releasePlayer() {
        exoPlayer?.removeListener(playerListener)
        exoPlayer?.release()
        exoPlayer = null
        playerPrepared = false
        _player.value = null
        _isPlayerReady.value = false
    }

    override fun onCleared() {
        releasePlayer()
        super.onCleared()
    }

    fun editPost() {
        viewModelScope.launch {
            _isSaving.value = FeatureState.Loading

            val description = _description.value ?: ""
            val linkedProductIds = _linkedProducts.value.map { it.id }

            val result = withVisibleLoading {
                updatePostUseCase(
                    postId = postId,
                    description = description,
                    linkedProductIds = linkedProductIds,
                    customCover = pendingCoverDataUri,
                    serviceDomainId = _selectedServiceDomainId.value
                )
            }

            result
                .onSuccess {
                    postInteractionStore.updateDescription(postId, description)
                    _isSaving.value = FeatureState.Success(Unit)
                }
                .onFailure { e ->
                    Timber.tag("EditPost").e(e, "Failed to update post")
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())

                    _isSaving.value = FeatureState.Error(e)
                }
        }
    }
}
