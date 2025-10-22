package com.example.scrollbooker.ui.shared.posts.sheets.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessByIdUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getBusinessByIdUseCase: GetBusinessByIdUseCase,
    private val authDataStore: AuthDataStore
): ViewModel() {
    private val _businessState = MutableStateFlow<FeatureState<Business>>(FeatureState.Loading)
    val businessState: StateFlow<FeatureState<Business>> = _businessState.asStateFlow()

    private val _isMapReady = MutableStateFlow<Boolean>(false)
    val isMapReady: StateFlow<Boolean> = _isMapReady.asStateFlow()

    fun setIsMapReady(isReady: Boolean) {
        _isMapReady.value = isReady
    }

    fun loadBusiness() {
        viewModelScope.launch {
//            val businessId = authDataStore.getBusinessId().firstOrNull()
//
//            if(businessId == null) {
//                Timber.tag("Business").e("ERROR: Business Id not found")
//                _businessState.value = FeatureState.Error()
//            }
//
//            businessId?.let { bId ->
//                _businessState.value = FeatureState.Loading
//                _businessState.value = withVisibleLoading {
//                    getBusinessByIdUseCase(7)
//                }
//            }

            _businessState.value = FeatureState.Loading
            _businessState.value = withVisibleLoading {
                getBusinessByIdUseCase(7)
            }
        }
    }

    init {
        loadBusiness()
    }
}