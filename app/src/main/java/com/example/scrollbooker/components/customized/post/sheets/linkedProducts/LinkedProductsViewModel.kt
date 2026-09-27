package com.example.scrollbooker.components.customized.post.sheets.linkedProducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.location.UserLocationService
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetAppointmentByUserAndPostUseCase
import com.example.scrollbooker.entity.booking.products.domain.model.LinkedProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetPostLinkedProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

private data class LinkedProductsPostRef(
    val postId: Int,
    val postUserId: Int,
    val isVideoReview: Boolean
)

@HiltViewModel
class LinkedProductsViewModel @Inject constructor(
    private val getPostLinkedProductsUseCase: GetPostLinkedProductsUseCase,
    private val getAppointmentByUserAndPostUseCase: GetAppointmentByUserAndPostUseCase,
    private val userLocationService: UserLocationService
) : ViewModel() {

    private val _postRef = MutableStateFlow<LinkedProductsPostRef?>(null)

    fun setPost(postId: Int, postUserId: Int, isVideoReview: Boolean) {
        val newRef = LinkedProductsPostRef(postId, postUserId, isVideoReview)
        if (_postRef.value != newRef) _postRef.value = newRef
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productsState: StateFlow<FeatureState<LinkedProducts>> = _postRef
        .filterNotNull()
        .distinctUntilChanged()
        .filter { !it.isVideoReview }
        .flatMapLatest { ref ->
            flow {
                emit(FeatureState.Loading)

                val userLocation = userLocationService.currentLocation()
                val result = withVisibleLoading {
                    getPostLinkedProductsUseCase(
                        postId = ref.postId,
                        allowFallback = true,
                        lat = userLocation?.lat,
                        lng = userLocation?.lng
                    )
                }

                result
                    .onSuccess { linkedProducts ->
                        emit(FeatureState.Success(linkedProducts))
                    }
                    .onFailure { error ->
                        Timber.tag("Linked Products").e(error, "ERROR: on Fetching Post Linked Products")
                        emit(FeatureState.Error(error))
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val reviewAppointmentState: StateFlow<FeatureState<Appointment>> = _postRef
        .filterNotNull()
        .distinctUntilChanged()
        .filter { it.isVideoReview }
        .flatMapLatest { ref ->
            flow {
                emit(FeatureState.Loading)

                val userLocation = userLocationService.currentLocation()
                val result = withVisibleLoading {
                    getAppointmentByUserAndPostUseCase(ref.postUserId, ref.postId, userLocation?.lat, userLocation?.lng)
                }

                result
                    .onSuccess { appointment ->
                        emit(FeatureState.Success(appointment))
                    }
                    .onFailure { error ->
                        Timber.tag("Review Appointment").e(error, "ERROR: on Fetching Appointment By User And Post")
                        emit(FeatureState.Error(error))
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Loading
        )
}
