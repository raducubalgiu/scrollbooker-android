package com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.model.EmploymentRequest
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.useCase.GetEmploymentRequestsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmploymentRequestsViewModel @Inject constructor(
    private val getEmploymentRequestsUseCase: GetEmploymentRequestsUseCase
): ViewModel() {
    private val _employmentRequests = MutableStateFlow<FeatureState<List<EmploymentRequest>>>(FeatureState.Loading)
    val employmentRequests: StateFlow<FeatureState<List<EmploymentRequest>>> = _employmentRequests

    init {
        loadEmploymentRequests()
    }

    fun loadEmploymentRequests() {
        viewModelScope.launch {
            _employmentRequests.value = FeatureState.Loading
            _employmentRequests.value = getEmploymentRequestsUseCase()
        }
    }
}