package com.example.scrollbooker.ui.myBusiness.myBusinessDetails

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.R
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.snackbar.UiText
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessByUserUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.UpdateBusinessGalleryUseCase
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.ui.onboarding.business.collectGallery.BusinessPhotoUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyBusinessDetailsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getBusinessByUserUseCase: GetBusinessByUserUseCase,
    private val updateBusinessGalleryUseCase: UpdateBusinessGalleryUseCase
): ViewModel() {
    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    private val _business = MutableStateFlow<FeatureState<Business>>(FeatureState.Loading)
    val business: StateFlow<FeatureState<Business>> = _business.asStateFlow()

    private val _photosState = MutableStateFlow(BusinessPhotoUIState())
    val photosState: StateFlow<BusinessPhotoUIState> = _photosState.asStateFlow()

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    fun loadBusiness() {
        viewModelScope.launch {
            _business.value = FeatureState.Loading
            _business.value = withVisibleLoading {
                getBusinessByUserUseCase()
            }
        }
    }

    init {
        loadBusiness()
    }

    fun setImage(slot: Int, uri: Uri?) {
        if(slot !in 0..4) return
        _photosState.update { s ->
            s.copy(images = s.images.toMutableList().also { it[slot] = uri })
        }
    }

    fun clearImage(slot: Int) = setImage(slot, null)

    fun saveBusinessGallery() {
        viewModelScope.launch {
            val businessId = authDataStore.getBusinessId().firstOrNull()
                ?: throw Exception("Business ID not found")

            _isSaving.value = FeatureState.Loading

            val photos = _photosState.value.images

            val result = withVisibleLoading {
                updateBusinessGalleryUseCase(
                    businessId = businessId,
                    photos = photos
                )
            }

            result
                .onFailure { e ->
                    _isSaving.value = FeatureState.Error(e)
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                    Timber.tag("Update Gallery").e("ERROR: on Updating Business Gallery $e")
                }
                .onSuccess {
                    _isSaving.value = FeatureState.Success(Unit)

                    _events.tryEmit(
                        SnackBarUiEvent.Show(
                            message = UiText.Resource(R.string.businessGalleryUpdatedSuccessfully)
                        )
                    )
                }
        }
    }
}