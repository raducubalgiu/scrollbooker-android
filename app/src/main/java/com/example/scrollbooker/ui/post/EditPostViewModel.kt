package com.example.scrollbooker.ui.post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetPostLinkedProductsUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import com.example.scrollbooker.entity.social.post.domain.model.PostMediaFile
import com.example.scrollbooker.entity.social.post.domain.useCase.GetPostByIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed interface EditPostUiState {
    object Loading : EditPostUiState
    data class Success(
        val postMedia: PostMediaFile?,
        val description: String,
        val linkedProducts: Set<Product>,
        val catalogProducts: UserProducts
    ) : EditPostUiState
    data class Error(val error: Throwable?) : EditPostUiState
}

@HiltViewModel
class EditPostViewModel @Inject constructor(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    private val getPostLinkedProductsUseCase: GetPostLinkedProductsUseCase,
    private val authDataStore: AuthDataStore,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val postId: Int = savedStateHandle["postId"] ?: error("Missing postId")

    private val _description = MutableStateFlow<String?>(null)
    private val _linkedProducts = MutableStateFlow<Set<Product>>(emptySet())
    private val _catalogProducts = MutableStateFlow<FeatureState<UserProducts>>(FeatureState.Loading)
    private val _postMedia = MutableStateFlow<PostMediaFile?>(null)

    val uiState: StateFlow<EditPostUiState> = combine(
        _description,
        _linkedProducts,
        _catalogProducts,
        _postMedia
    ) { desc, linked, catalog, media ->
        when {
            catalog is FeatureState.Error -> EditPostUiState.Error(catalog.error)
            desc == null || catalog is FeatureState.Loading -> EditPostUiState.Loading

            catalog is FeatureState.Success -> {
                EditPostUiState.Success(
                    postMedia = media,
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

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving.asStateFlow()

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

    fun editPost() { /* ... */ }
}