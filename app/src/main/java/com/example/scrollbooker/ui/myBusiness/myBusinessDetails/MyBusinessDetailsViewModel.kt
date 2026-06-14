package com.example.scrollbooker.ui.myBusiness.myBusinessDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessByUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBusinessDetailsViewModel @Inject constructor(
    private val getBusinessByUserUseCase: GetBusinessByUserUseCase
): ViewModel() {
    private val _business = MutableStateFlow<FeatureState<Business>>(FeatureState.Loading)
    val business: StateFlow<FeatureState<Business>> = _business.asStateFlow()

    fun loadBusiness() {
        viewModelScope.launch {
            _business.value = FeatureState.Loading
            _business.value = withVisibleLoading {
                getBusinessByUserUseCase()
            }
        }
    }

    init {
        loadBusiness()
    }
}