package com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.employmentRequest.data.remote.EmploymentRequestCreateDto
import com.example.scrollbooker.entity.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.entity.employmentRequest.domain.useCase.CreateEmploymentRequestUseCase
import com.example.scrollbooker.entity.employmentRequest.domain.useCase.GetEmploymentRequestsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmploymentRequestsViewModel @Inject constructor(
    private val getEmploymentRequestsUseCase: GetEmploymentRequestsUseCase,
    private val createEmploymentRequestUseCase: CreateEmploymentRequestUseCase
): ViewModel() {
    private val _employmentRequests = MutableStateFlow<FeatureState<List<EmploymentRequest>>>(FeatureState.Loading)
    val employmentRequests: StateFlow<FeatureState<List<EmploymentRequest>>> = _employmentRequests

    private val _createRequestState = MutableStateFlow<FeatureState<Unit>?>(null)
    val createRequestState: StateFlow<FeatureState<Unit>?> = _createRequestState

    init {
        loadEmploymentRequests()
    }

    fun loadEmploymentRequests() {
        viewModelScope.launch {
            _employmentRequests.value = FeatureState.Loading
            _employmentRequests.value = getEmploymentRequestsUseCase()
        }
    }

    fun createEmploymentRequest(dto: EmploymentRequestCreateDto) {
        viewModelScope.launch {
            _createRequestState.value = FeatureState.Loading
            _createRequestState.value = createEmploymentRequestUseCase(dto)

            if(createRequestState.value is FeatureState.Success) {
                loadEmploymentRequests()
            }
        }
    }
}