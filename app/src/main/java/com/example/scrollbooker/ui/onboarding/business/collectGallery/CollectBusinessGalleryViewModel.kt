package com.example.scrollbooker.ui.onboarding.business.collectGallery

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectBusinessGalleryUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

data class BusinessPhotoUIState(
    val images: List<Uri?> = List(5) { null },
    val initialImages: List<Uri?> = List(5) { null }
) {
    val hasChanges: Boolean get() = images != initialImages
}

@HiltViewModel
class CollectBusinessGalleryViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val collectBusinessGalleryUseCase: CollectBusinessGalleryUseCase
): ViewModel() {
    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    private val _photosState = MutableStateFlow(BusinessPhotoUIState())
    val photosState: StateFlow<BusinessPhotoUIState> = _photosState

    fun setImage(slot: Int, uri: Uri?) {
        if(slot !in 0..4) return
        _photosState.update { s ->
            s.copy(images = s.images.toMutableList().also { it[slot] = uri })
        }
    }

    fun clearImage(slot: Int) = setImage(slot, null)

    suspend fun collectBusinessGallery(skipUpdateGallery: Boolean): Result<AuthState> {
        val businessId = authDataStore.getBusinessId().firstOrNull()
            ?: return Result.failure(Exception("Business ID not found"))

        _isSaving.value = FeatureState.Loading

        val photos = _photosState.value.images

        val result = withVisibleLoading {
            collectBusinessGalleryUseCase(businessId, skipUpdateGallery, photos)
        }

        result
            .onFailure { e ->
                _isSaving.value = FeatureState.Error(e)
                Timber.Forest.tag("Collect Gallery").e("ERROR: on Collecting Business Gallery $e")
            }
            .onSuccess {
                _isSaving.value = FeatureState.Success(Unit)
            }

        return result
    }
}