package com.example.scrollbooker.ui.myBusiness.myBusinessDetails

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.R
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.snackbar.UiText
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessDetails
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetMyBusinessDetailsUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.UpdateBusinessGalleryUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.UpdateSchedulesUseCase
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
import androidx.core.net.toUri

@HiltViewModel
class MyBusinessDetailsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getMyBusinessDetailsUseCase: GetMyBusinessDetailsUseCase,
    private val updateBusinessGalleryUseCase: UpdateBusinessGalleryUseCase,
    private val updateSchedulesUseCase: UpdateSchedulesUseCase
): ViewModel() {
    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    private val _photosState = MutableStateFlow(BusinessPhotoUIState())
    val photosState: StateFlow<BusinessPhotoUIState> = _photosState.asStateFlow()

    private val _businessDetailsState = MutableStateFlow<FeatureState<BusinessDetails>>(FeatureState.Loading)
    val businessDetailsState: StateFlow<FeatureState<BusinessDetails>> = _businessDetailsState.asStateFlow()

    private val _schedulesState = MutableStateFlow<FeatureState<List<Schedule>>>(FeatureState.Loading)
    val schedulesState: StateFlow<FeatureState<List<Schedule>>> = _schedulesState.asStateFlow()

    private val _isSavingSchedules = MutableStateFlow(false)
    val isSavingSchedules: StateFlow<Boolean> = _isSavingSchedules.asStateFlow()

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    init {
        fetchBusinessDetails()
    }

    fun fetchBusinessDetails() {
        viewModelScope.launch {
            _businessDetailsState.value = FeatureState.Loading

            val result = withVisibleLoading { getMyBusinessDetailsUseCase() }

            result.fold(
                onSuccess = { businessDetails ->
                    _businessDetailsState.value = FeatureState.Success(businessDetails)

                    val existingImages = businessDetails.mediaFiles
                        .sortedBy { it.orderIndex }
                        .map { it.url.toUri() }

                    val slots = List(5) { i -> existingImages.getOrNull(i) }

                    _photosState.value = BusinessPhotoUIState(
                        images = slots,
                        initialImages = slots
                    )

                    _schedulesState.value = FeatureState.Success(businessDetails.schedules)
                },
                onFailure = { e ->
                    _businessDetailsState.value = FeatureState.Error(e)
                    Timber.tag("Business Details").e(e, "ERROR: on fetching Business Details")
                }
            )
        }
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

            result.fold(
                onSuccess = {
                    _isSaving.value = FeatureState.Success(Unit)
                    _photosState.update { s -> s.copy(initialImages = s.images) }

                    _events.tryEmit(
                        SnackBarUiEvent.Show(
                            message = UiText.Resource(R.string.businessGalleryUpdatedSuccessfully)
                        )
                    )
                },
                onFailure = { e ->
                    _isSaving.value = FeatureState.Error(e)
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                    Timber.tag("Update Gallery").e(e, "ERROR: on Updating Business Gallery")
                }
            )
        }
    }

    fun updateScheduleTime(schedule: Schedule) {
        val current = _schedulesState.value

        if(current is FeatureState.Success) {
            _schedulesState.value = FeatureState.Success(
                current.data.map {
                    if(it.id == schedule.id) it.copy(
                        startTime = schedule.startTime,
                        endTime = schedule.endTime
                    ) else it
                }
            )
        }
    }

    fun saveBusinessSchedules() {
        viewModelScope.launch {
            val schedules = (_schedulesState.value as? FeatureState.Success)?.data
                ?: return@launch

            _isSavingSchedules.value = true

            val result = withVisibleLoading { updateSchedulesUseCase(schedules) }

            result.fold(
                onSuccess = { updated ->
                    _isSavingSchedules.value = false
                    _schedulesState.value = FeatureState.Success(updated)

                    _events.tryEmit(
                        SnackBarUiEvent.Show(
                            message = UiText.Resource(R.string.scheduleSaved)
                        )
                    )
                },
                onFailure = { e ->
                    _isSavingSchedules.value = false
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                    Timber.tag("Business Schedules").e(e, "ERROR: on Updating Business Schedules")
                }
            )
        }
    }
}