package com.example.scrollbooker.feature.myBusiness.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.feature.myBusiness.domain.model.Service
import com.example.scrollbooker.feature.myBusiness.domain.useCase.GetServicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val getServicesUseCase: GetServicesUseCase
): ViewModel() {
    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = getServicesUseCase()

            if(result.isSuccess) {
                _services.value = result.getOrThrow()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Unknown error"
            }

            _isLoading.value = false
        }
    }
}