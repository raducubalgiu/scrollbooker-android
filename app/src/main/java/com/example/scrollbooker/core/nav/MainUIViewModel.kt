package com.example.scrollbooker.core.nav

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.entity.appointment.domain.useCase.GetUserAppointmentsNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainUIViewModel @Inject constructor(
    private val getUserAppointmentsNumberUseCase: GetUserAppointmentsNumberUseCase
): ViewModel() {

    var appointmentsState by mutableIntStateOf(0)
        private set

    init {
        loadInitialData()
    }

    fun increaseAppointmentsNumber() {
        appointmentsState = appointmentsState + 1
    }

    fun decreaseAppointmentsNumber() {
        if(appointmentsState > 0) {
            appointmentsState = appointmentsState - 1
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            appointmentsState = getUserAppointmentsNumberUseCase()
        }
    }
}