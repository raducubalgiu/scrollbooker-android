package com.example.scrollbooker.feature.myBusiness.services.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.myBusiness.services.domain.model.Service
import com.example.scrollbooker.feature.myBusiness.services.domain.useCase.GetServicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val getServicesUseCase: GetServicesUseCase
): ViewModel() {

    private val _servicesState = MutableStateFlow<FeatureState<List<Service>>>(FeatureState.Loading)
    val servicesState: StateFlow<FeatureState<List<Service>>> = _servicesState

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            _servicesState.value = FeatureState.Loading
            _servicesState.value = getServicesUseCase()
        }
    }
}