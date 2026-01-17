package com.example.scrollbooker.ui.myBusiness.myBusinessLocation
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.useCase.UpdateBusinessGalleryUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

data class BusinessPhotoUIState(
    val images: List<Uri?> = List(5) { null }
)

@HiltViewModel
class MyBusinessLocationViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val updateBusinessGalleryUseCase: UpdateBusinessGalleryUseCase
): ViewModel() {
    private val _photosState = MutableStateFlow(BusinessPhotoUIState())
    val photosState: StateFlow<BusinessPhotoUIState> = _photosState

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    fun setImage(slot: Int, uri: Uri?) {
        if(slot !in 0..4) return
        _photosState.update { s ->
            s.copy(images = s.images.toMutableList().also { it[slot] = uri })
        }
    }

    fun clearImage(slot: Int) = setImage(slot, null)

    suspend fun updateBusinessGallery(): Result<Unit> {
        _isSaving.value = FeatureState.Loading

        val businessId = authDataStore.getBusinessId().firstOrNull()

        if(businessId == null) {
            Timber.tag("Update Business Gallery").e("Business Id not found in AuthDataStore")
            return Result.failure(exception = Throwable("Business Id not found in AuthDataStore"))
        }

        val result = withVisibleLoading {
            updateBusinessGalleryUseCase(
                businessId = businessId,
                photos = _photosState.value.images
            )
        }

        return result
            .onFailure { error ->
                _isSaving.value = FeatureState.Error(error)
                Timber.Forest.tag("Create Business").e("ERROR: on creating Business $error")
            }
            .onSuccess { response ->
                _isSaving.value = FeatureState.Success(Unit)
            }
    }
}