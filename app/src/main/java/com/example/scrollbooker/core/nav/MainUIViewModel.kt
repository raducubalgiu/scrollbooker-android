package com.example.scrollbooker.core.nav

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.appointment.domain.useCase.GetUserAppointmentsNumberUseCase
import com.example.scrollbooker.entity.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.businessType.domain.useCase.GetAllBusinessTypesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainUIViewModel @Inject constructor(
    private val getUserAppointmentsNumberUseCase: GetUserAppointmentsNumberUseCase,
    private val getAllBusinessTypesUseCase: GetAllBusinessTypesUseCase
): ViewModel() {

    var appointmentsState by mutableIntStateOf(0)
        private set

    private val _businessTypesState = MutableStateFlow<FeatureState<List<BusinessType>>>(FeatureState.Loading)
    val businessTypesState: StateFlow<FeatureState<List<BusinessType>>> = _businessTypesState

    init {
        loadAppointmentsNumber()
        loadAllBusinessTypes()
    }

    fun increaseAppointmentsNumber() {
        appointmentsState = appointmentsState + 1
    }

    fun decreaseAppointmentsNumber() {
        if(appointmentsState > 0) {
            appointmentsState = appointmentsState - 1
        }
    }

    private fun loadAllBusinessTypes() {
        viewModelScope.launch {
            _businessTypesState.value = FeatureState.Loading
            _businessTypesState.value = getAllBusinessTypesUseCase()
        }
    }

    private fun loadAppointmentsNumber() {
        viewModelScope.launch {
            appointmentsState = getUserAppointmentsNumberUseCase()
        }
    }
}