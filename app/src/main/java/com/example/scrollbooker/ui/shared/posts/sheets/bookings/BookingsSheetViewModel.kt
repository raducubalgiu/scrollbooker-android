package com.example.scrollbooker.ui.shared.posts.sheets.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentScrollBookerCreate
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.CreateScrollBookerAppointmentUseCase
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByAppointmentIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookingsSheetViewModel @Inject constructor(
    private val createScrollBookerAppointmentUseCase: CreateScrollBookerAppointmentUseCase,
    private val getProductsByAppointmentIdUseCase: GetProductsByAppointmentIdUseCase
): ViewModel() {
    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _createState = MutableStateFlow<FeatureState<Unit>?>(null)
    val createState: StateFlow<FeatureState<Unit>?> = _createState.asStateFlow()

    private val _appointmentProducts = MutableStateFlow<FeatureState<List<Product>>>(FeatureState.Loading)
    val appointmentProducts: StateFlow<FeatureState<List<Product>>> = _appointmentProducts.asStateFlow()

    fun createAppointment(request: AppointmentScrollBookerCreate) {
        if(_isSaving.value) return

        viewModelScope.launch {
            _isSaving.value = true
            _createState.value = FeatureState.Loading

            val result = withVisibleLoading {
                createScrollBookerAppointmentUseCase(request)
            }

            result
                .onFailure { e ->
                    _isSaving.value = false
                    _createState.value = FeatureState.Error(e)
                    Timber.tag("Appointment").e("ERROR: on Creating ScrollBooker Appointment $e")
                }
                .onSuccess {
                    _isSaving.value = false
                    _createState.value = FeatureState.Success(Unit)
                }
        }
    }

    fun loadAppointmentProducts(appointmentId: Int) {
        viewModelScope.launch {
            _appointmentProducts.value = FeatureState.Loading
            _appointmentProducts.value = withVisibleLoading {
                getProductsByAppointmentIdUseCase(appointmentId)
            }
        }
    }

    fun consumeCreateState() {
        _createState.value = null
    }
}