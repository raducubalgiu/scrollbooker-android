package com.example.scrollbooker.feature.myBusiness.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule
import com.example.scrollbooker.feature.myBusiness.domain.useCase.GetSchedulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchedulesViewModel @Inject constructor(
    private val getSchedulesUseCase: GetSchedulesUseCase
): ViewModel() {
    private val _schedulesState = MutableStateFlow<FeatureState<List<Schedule>>>(FeatureState.Loading)
    val schedulesState: StateFlow<FeatureState<List<Schedule>>> = _schedulesState

    init {
        loadSchedules()
    }

    private fun loadSchedules() {
        viewModelScope.launch {
            _schedulesState.value = FeatureState.Loading
            _schedulesState.value = getSchedulesUseCase()
        }
    }
}