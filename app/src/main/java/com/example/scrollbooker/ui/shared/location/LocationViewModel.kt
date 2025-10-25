package com.example.scrollbooker.ui.shared.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessLocation
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessByIdUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessLocationUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getBusinessLocationUseCase: GetBusinessLocationUseCase
): ViewModel() {
    private val _businessLocation = MutableStateFlow<FeatureState<BusinessLocation>>(FeatureState.Loading)
    val businessLocation: StateFlow<FeatureState<BusinessLocation>> = _businessLocation.asStateFlow()

    fun loadBusiness() {
        viewModelScope.launch {
            _businessLocation.value = FeatureState.Loading
            _businessLocation.value = getBusinessLocationUseCase(
                businessId = 7,
                userLat = null,
                userLng = null
            )
        }
    }

    init {
        loadBusiness()
    }
}